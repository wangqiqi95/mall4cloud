package com.mall4j.cloud.order.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.order.mapper.OrderMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.GROUP_ORDER_SUCCESS_TOPIC,consumerGroup = "GID_"+RocketMqConstant.GROUP_ORDER_SUCCESS_TOPIC)
public class GroupOrderSuccessConsumer implements RocketMQListener<List<Long>> {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 团购订单成团了，将订单状态变为待发货
     * @param orderIds
     */
    @Override
    public void onMessage(List<Long> orderIds) {
        orderMapper.updateGroupOrderSuccessStatus(orderIds);
    }
}
