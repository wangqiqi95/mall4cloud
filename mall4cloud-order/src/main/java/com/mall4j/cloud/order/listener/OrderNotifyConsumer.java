package com.mall4j.cloud.order.listener;

import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.bo.SendNotifyBO;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2021/1/7
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_NOTIFY_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_NOTIFY_TOPIC)
public class OrderNotifyConsumer implements RocketMQListener<PayNotifyBO> {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OnsMQTemplate orderNotifyShopTemplate;
    @Autowired
    private OnsMQTemplate orderNotifyCouponTemplate;
    @Autowired
    private OnsMQTemplate orderNotifyStockTemplate;
    @Autowired
    private OnsMQTemplate orderNotifySeckillTemplate;
    @Autowired
    private OnsMQTemplate orderNotifyGroupTemplate;
    @Autowired
    private OnsMQTemplate stdOrderNotifyTemplate;


    private static final Logger LOG = LoggerFactory.getLogger(OrderNotifyConsumer.class);

    @Override
    public void onMessage(PayNotifyBO message) {
        LOG.info("订单回调开始... message: " + Json.toJsonString(message));
        List<OrderStatusBO> ordersStatus = orderService.getOrdersStatus(message.getOrderIds());

        orderService.updateByToPaySuccess(message,ordersStatus);

        // 发送消息，订单支付成功 通知商家分账
        SendResult sendResult = orderNotifyShopTemplate.syncSend(message);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，因为订单回调会有多次，几乎不可能每次都无法发送出去，发的出去无所谓因为接口是幂等的
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        Integer orderType = ordersStatus.get(0).getOrderType();

        // 普通订单才能使用优惠券
        if (Objects.equals(orderType, OrderType.ORDINARY.value()) || Objects.equals(orderType, OrderType.DAIKE.value())) {
            // 发送消息，订单支付成功 通知优惠券扣减
            sendResult = orderNotifyCouponTemplate.syncSend(message.getOrderIds());
            if (sendResult == null || sendResult.getMessageId() == null) {
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }

        // 非秒杀订单，共用的库存
        if (!Objects.equals(orderType, OrderType.SECKILL.value())) {
            // 发送消息，订单支付成功 通知库存扣减
            sendResult = orderNotifyStockTemplate.syncSend(message);
            if (sendResult == null || sendResult.getMessageId() == null) {
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }

        // 团购订单
        if (Objects.equals(orderType, OrderType.GROUP.value())) {
            // 团购支付成功，往团队添加成员
            sendResult = orderNotifyGroupTemplate.syncSend(message);
            if (sendResult == null || sendResult.getMessageId() == null) {
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }

        // 秒杀订单有自己操作
        if (Objects.equals(orderType, OrderType.SECKILL.value())) {
            // 秒杀订单支付成功
            sendResult = orderNotifySeckillTemplate.syncSend(message.getOrderIds().get(0));
            if (sendResult == null || sendResult.getMessageId() == null) {
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }

        // 支付成功后 推送订单明细至中台
            sendResult = stdOrderNotifyTemplate.syncSend(message.getOrderIds().get(0),RocketMqConstant.ORDER_NOTIFY_STD_TOPIC_TAG);
            if (sendResult == null || sendResult.getMessageId() == null) {
                throw new LuckException(ResponseEnum.EXCEPTION);
            }


        // 发送消息，订单支付成，推送通知给用户
        List<SendNotifyBO> notifyList = orderService.listByOrderIds(message.getOrderIds());
        for (SendNotifyBO sendNotifyBO : notifyList) {
            sendNotifyBO.setPrice(PriceUtil.toDecimalPrice(sendNotifyBO.getActualTotal()).toString());
            sendNotifyBO.setSendType(SendTypeEnum.PAY_SUCCESS.getValue());
        }
        // 订单支付成功推送消息给用户
        sendResult = orderNotifySeckillTemplate.syncSend(notifyList);
        if (sendResult == null || sendResult.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }
}
