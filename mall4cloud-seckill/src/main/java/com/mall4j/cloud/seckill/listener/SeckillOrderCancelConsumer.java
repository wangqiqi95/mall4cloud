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
@RocketMQMessageListener(topic = RocketMqConstant.SECKILL_ORDER_CANCEL_TOPIC,consumerGroup = "GID_"+RocketMqConstant.SECKILL_ORDER_CANCEL_TOPIC)
public class SeckillOrderCancelConsumer implements RocketMQListener<Long> {

    @Autowired
    private SeckillOrderService seckillOrderService;

    /**
     * 秒杀订单取消，还原库存
     */
    @Override
    public void onMessage(Long orderId) {
        seckillOrderService.cancelUnpayOrderByOrderId(orderId);
    }
}
