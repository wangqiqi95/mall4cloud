package com.mall4j.cloud.seckill.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.seckill.service.SeckillOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_NOTIFY_SECKILL_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_NOTIFY_SECKILL_TOPIC)
public class OrderNotifySeckillConsumer implements RocketMQListener<Long> {

    @Autowired
    private SeckillOrderService seckillOrderService;

    /**
     * 秒杀订单支付成功
     */
    @Override
    public void onMessage(Long orderId) {
        seckillOrderService.paySuccessOrderByOrderId(orderId);
    }
}
