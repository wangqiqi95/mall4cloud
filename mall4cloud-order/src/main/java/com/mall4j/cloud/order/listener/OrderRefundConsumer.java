package com.mall4j.cloud.order.listener;

import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.multishop.bo.OrderChangeShopWalletAmountBO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.PayRefundBO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.order.constant.RefundType;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 *
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_REFUND_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_REFUND_TOPIC)
public class OrderRefundConsumer implements RocketMQListener<OrderRefundVO> {

    @Autowired
    private OnsMQTemplate orderRefundPaymentTemplate;
    @Autowired
    private OnsMQTemplate orderRefundShopTemplate;
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    private static final Logger LOG = LoggerFactory.getLogger(OrderRefundConsumer.class);

    @Override
    public void onMessage(OrderRefundVO orderRefundVo) {

        PayRefundBO payRefundBO = new PayRefundBO();
        payRefundBO.setRefundId(orderRefundVo.getRefundId());
        payRefundBO.setRefundNumber(orderRefundVo.getRefundNumber());
        payRefundBO.setRefundAmount(orderRefundVo.getRefundAmount());
        payRefundBO.setOrderId(orderRefundVo.getOrderId());
        payRefundBO.setPayId(orderRefundVo.getPayId());
//        payRefundBO.setPayId(orderRefundVo.getPayId());

        // 发送退款服务进行退款的通知
        SendResult sendResult = orderRefundPaymentTemplate.syncSend(payRefundBO);



        // 锁定商家钱包结算金额
        OrderChangeShopWalletAmountBO orderChangeShopWalletAmountBO = getOrderChangeShopWalletAmountBO(orderRefundVo);

        // 减少商家待结算金额
        SendResult sendShopStatus = orderRefundShopTemplate.syncSend(orderChangeShopWalletAmountBO);


        if (sendResult == null || sendResult.getMessageId() == null || sendShopStatus == null || sendShopStatus.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }


    /**
     * 获取订单待结算金额
     */
    private OrderChangeShopWalletAmountBO getOrderChangeShopWalletAmountBO(OrderRefundVO orderRefundVo) {

        // 同意退款，判断商家是否还有金额进行退款
        Order order = orderService.getByOrderId(orderRefundVo.getOrderId());
        if (order == null) {
            throw new LuckException("未找到该订单信息");
        }

        // 实付金额
        Long actualTotal;
//        Long platformAmount;
        Long platformCommission;
        Long changePlatformCommission;
        Double rate = 0.0;
        Long distributionAmount;
        if (Objects.equals(orderRefundVo.getRefundType(), RefundType.ALL.value())) {
            actualTotal = order.getActualTotal();
//            platformAmount = order.getPlatformAmount();
            platformCommission = order.getPlatformCommission();
            distributionAmount = order.getDistributionAmount();
        } else {
            OrderItemVO item = orderItemService.getByOrderItemId(orderRefundVo.getOrderItemId());
            actualTotal =  item.getActualTotal();
//            platformAmount = item.getPlatformShareReduce();
            // 平台佣金
            platformCommission = item.getPlatformCommission();
            rate = item.getRate();

            distributionAmount = item.getDistributionAmount();
        }


        OrderChangeShopWalletAmountBO orderChangeShopWalletAmountBO = new OrderChangeShopWalletAmountBO();
        orderChangeShopWalletAmountBO.setOrderStatus(order.getStatus());
        orderChangeShopWalletAmountBO.setActualTotal(actualTotal);
        orderChangeShopWalletAmountBO.setRefundAmount(orderRefundVo.getRefundAmount());
        orderChangeShopWalletAmountBO.setPlatformAllowanceAmount(orderRefundVo.getPlatformRefundAmount());
        orderChangeShopWalletAmountBO.setOrderId(order.getOrderId());
        orderChangeShopWalletAmountBO.setRefundId(orderRefundVo.getRefundId());
        orderChangeShopWalletAmountBO.setShopId(orderRefundVo.getShopId());
        orderChangeShopWalletAmountBO.setOrderItemId(orderRefundVo.getOrderItemId());
        orderChangeShopWalletAmountBO.setPlatformCommission(platformCommission);
        orderChangeShopWalletAmountBO.setChangePlatformCommission(orderRefundVo.getPlatformRefundCommission());
        orderChangeShopWalletAmountBO.setRate(rate);
        orderChangeShopWalletAmountBO.setDistributionAmount(distributionAmount);
        return orderChangeShopWalletAmountBO;
    }
}
