package com.mall4j.cloud.group.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.group.bo.GroupOrderBO;
import com.mall4j.cloud.group.service.GroupOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.GROUP_ORDER_CREATE_TOPIC,consumerGroup = "GID_"+RocketMqConstant.GROUP_ORDER_CREATE_TOPIC)
public class GroupOrderCreateConsumer implements RocketMQListener<GroupOrderBO> {

    @Autowired
    private GroupOrderService groupOrderService;

    /**
     * 团购活动订单创建监听，先创建订单，然后创建团购活动订单
     */
    @Override
    public void onMessage(GroupOrderBO groupOrderBO) {
        groupOrderService.submit(groupOrderBO);
    }
}
