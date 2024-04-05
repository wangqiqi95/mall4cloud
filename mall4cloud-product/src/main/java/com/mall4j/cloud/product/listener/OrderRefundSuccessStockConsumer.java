package com.mall4j.cloud.product.listener;

import com.mall4j.cloud.common.order.bo.RefundReductionStockBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.service.SkuStockService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 解锁库存的监听
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_REFUND_SUCCESS_STOCK_TOPIC,consumerGroup = "GID_"+RocketMqConstant.ORDER_REFUND_SUCCESS_STOCK_TOPIC)
public class OrderRefundSuccessStockConsumer implements RocketMQListener<List<RefundReductionStockBO>> {

    @Autowired
    private SkuStockService skuStockService;

    /**
     * 订单退款导致待发货订单取消，需要还原优惠券和库存
     */
    @Override
    public void onMessage(List<RefundReductionStockBO> refundReductionStocks) {
        skuStockService.reductionStock(refundReductionStocks);
    }
}
