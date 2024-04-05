package com.mall4j.cloud.group.listener;

import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.group.service.GroupOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 团购订单支付监听
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_NOTIFY_GROUP_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_NOTIFY_GROUP_TOPIC)
public class OrderNotifyGroupConsumer implements RocketMQListener<PayNotifyBO> {

    @Autowired
    private GroupOrderService groupOrderService;

    /**
     * 通知团购订单已经支付
     */
    @Override
    public void onMessage(PayNotifyBO message) {
        groupOrderService.payNotifyGroupOrder(message);
    }
}
