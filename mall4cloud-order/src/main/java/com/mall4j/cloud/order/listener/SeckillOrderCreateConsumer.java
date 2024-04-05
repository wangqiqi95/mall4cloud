package com.mall4j.cloud.order.listener;

import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SECKILL_ORDER_CREATE_TOPIC,consumerGroup = "GID_"+RocketMqConstant.SECKILL_ORDER_CREATE_TOPIC)
public class SeckillOrderCreateConsumer implements RocketMQListener<ShopCartOrderMergerVO> {

    @Autowired
    private OrderService orderService;

    /**
     * 先扣减秒杀商品库存，这个是最后步，创建订单
     */
    @Override
    public void onMessage(ShopCartOrderMergerVO shopCartOrderMerger) {
        orderService.submit(shopCartOrderMerger);
    }
}
