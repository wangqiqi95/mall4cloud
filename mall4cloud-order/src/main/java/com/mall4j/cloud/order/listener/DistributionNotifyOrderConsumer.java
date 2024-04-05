package com.mall4j.cloud.order.listener;

import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.order.service.OrderItemService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分销修改订单项分销金额
 * @author cl
 * @date 2021-08-20 09:10:00
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.DISTRIBUTION_NOTIFY_ORDER_TOPIC,consumerGroup = "GID_"+RocketMqConstant.DISTRIBUTION_NOTIFY_ORDER_TOPIC)
public class DistributionNotifyOrderConsumer implements RocketMQListener<List<EsOrderItemBO>> {

    private static final Logger logger = LoggerFactory.getLogger(DistributionNotifyOrderConsumer.class);

    @Autowired
    private OrderItemService orderItemService;

    @Override
    public void onMessage(List<EsOrderItemBO> message) {
        logger.info("分销修改订单项分销金额回调开始... message: " + Json.toJsonString(message));
        orderItemService.updateBatchDistributionAmount(message);
    }
}
