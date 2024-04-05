package com.mall4j.cloud.distribution.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.distribution.service.DistributionSettlementService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author ZengFanChang
 * @Date 2021/12/12
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_SETTLED_SHOP_TOPIC, consumerGroup = "GID_"+RocketMqConstant.ORDER_SETTLED_SHOP_TOPIC)
public class OrderSettlementConsumer implements RocketMQListener<List<Long>> {

    private static final Logger logger = LoggerFactory.getLogger(OrderSettlementConsumer.class);

    @Autowired
    private DistributionSettlementService distributionSettlementService;


    @Override
    public void onMessage(List<Long> orderIds) {
        logger.info("开始佣金结算 orderIds:{}", orderIds);
        distributionSettlementService.processSettlement(orderIds);
        logger.info("佣金结算完成 orderIds:{}", orderIds);
    }
}
