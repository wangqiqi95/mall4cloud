package com.mall4j.cloud.distribution.listener;

import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.distribution.service.DistributionOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单支付成功，分销通知
 *
 * @author cl
 * @date 2021-08-17 17:42:17
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_NOTIFY_DISTRIBUTION_TOPIC, consumerGroup = "GID_"+RocketMqConstant.ORDER_NOTIFY_DISTRIBUTION_TOPIC)
public class OrderNotifyDistributionConsumer implements RocketMQListener<PayNotifyBO> {

    private static final Logger logger = LoggerFactory.getLogger(OrderNotifyDistributionConsumer.class);

    @Autowired
    private DistributionOrderService distributionOrderService;

    /**
     * 订单支付成功, 分销通知
     */
    @Override
    public void onMessage(PayNotifyBO payNotifyBO) {
        logger.info("支付成功处理佣金 payNotifyBO:{}", payNotifyBO);
//        distributionOrderService.payNotifyDistributionOrder(payNotifyBO);
        distributionOrderService.paySuccessNotifyDistributionOrder(payNotifyBO);
    }
}
