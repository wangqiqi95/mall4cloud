package com.mall4j.cloud.group.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.group.service.GroupOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 取消团购订单
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.GROUP_ORDER_CANCEL_TOPIC,consumerGroup = "GID_"+RocketMqConstant.GROUP_ORDER_CANCEL_TOPIC)
public class GroupOrderCancelConsumer implements RocketMQListener<List<Long>> {

    @Autowired
    private GroupOrderService groupOrderService;

    /**
     * 取消团购订单
     */
    @Override
    public void onMessage(List<Long> orderIds) {
        groupOrderService.cancelGroupOrder(orderIds);
    }
}
