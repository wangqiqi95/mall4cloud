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
@RocketMQMessageListener(topic = RocketMqConstant.FIXEDPRICE_ORDER_CREATE_TOPIC,consumerGroup = "GID_"+RocketMqConstant.FIXEDPRICE_ORDER_CREATE_TOPIC)
public class OnePriceOrderCreateConsumer implements RocketMQListener<ShopCartOrderMergerVO> {

    @Autowired
    private OrderService orderService;

    /**
     * 一口价订单，这个是最后步，创建订单
     */
    @Override
    public void onMessage(ShopCartOrderMergerVO shopCartOrderMerger) {
        orderService.submit(shopCartOrderMerger);
    }
}
