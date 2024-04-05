package com.mall4j.cloud.distribution.listener;

import com.mall4j.cloud.api.multishop.bo.OrderChangeShopWalletAmountBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.distribution.service.DistributionRefundService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 退款，店铺分账退款完成后，回退分销佣金
 * @author cl
 * @date 2021-08-20 15:34:30
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.REFUND_SHOP_NOTIFY_DISTRIBUTION_TOPIC, consumerGroup = "GID_"+RocketMqConstant.REFUND_SHOP_NOTIFY_DISTRIBUTION_TOPIC)
public class RefundShopNotifyDistributionConsumer implements RocketMQListener<OrderChangeShopWalletAmountBO> {
    private static final Logger logger = LoggerFactory.getLogger(RefundShopNotifyDistributionConsumer.class);

    @Autowired
    private DistributionRefundService distributionRefundService;

    @Override
    public void onMessage(OrderChangeShopWalletAmountBO message) {
        logger.info("回退分销佣金,回调开始... message: " + Json.toJsonString(message));
//        distributionRefundService.refundDistributionAmount(message);
    }
}
