package com.mall4j.cloud.order.listener;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.common.order.bo.RefundNotifyBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_REFUND_SUCCESS_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_REFUND_SUCCESS_TOPIC)
public class OrderRefundSuccessConsumer implements RocketMQListener<RefundNotifyBO> {

    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    OrderService orderService;

    @Override
    public void onMessage(RefundNotifyBO refundNotifyBO) {
        log.info("退款成功回调，执行参数信息:{}", JSONObject.toJSONString(refundNotifyBO));
        //查询订单类型，
        EsOrderBO orderBO = orderService.getEsOrder(refundNotifyBO.getOrderId());
        log.info("退款成功回调，查询订单参数:{}，订单查询结果：{}", JSONObject.toJSONString(refundNotifyBO),JSONObject.toJSONString(orderBO));
        // 因为未成团进行团购订单的退款
        if (Objects.equals(refundNotifyBO.getUnSuccessGroupOrder(), 1)) {
            // 创建退款订单然后再退款
            orderRefundService.createGroupUnSuccessRefundInfo(refundNotifyBO);
        } else {
            // 退款
            orderRefundService.refundSuccess(refundNotifyBO);
        }
    }
}
