package com.mall4j.cloud.product.listener;

import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.service.SkuStockLockService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 锁定视频号4.0 订单支付成功库存
 * @author FrozenWatermelon
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.EC_ORDER_NOTIFY_STOCK_TOPIC,consumerGroup = "GID_"+RocketMqConstant.EC_ORDER_NOTIFY_STOCK_TOPIC)
public class EcOrderNotifyStockConsumer implements RocketMQListener<Long> {

    @Autowired
    private SkuStockLockService skuStockLockService;

    /**
     * 视频号4.0订单支付成功 锁定库存
     */
    @Override
    public void onMessage(Long orderId) {
        log.info("视频号4订单支付成功，扣减库存，接收参数：{}",orderId);

        skuStockLockService.ecOrderDeduction(orderId);
    }
}
