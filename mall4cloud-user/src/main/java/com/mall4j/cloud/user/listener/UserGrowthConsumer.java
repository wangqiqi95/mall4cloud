package com.mall4j.cloud.user.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.user.service.UserExtensionService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SEND_NOTIFY_TO_USER_EXTENSION_TOPIC, consumerGroup = "GID_"+RocketMqConstant.SEND_NOTIFY_TO_USER_EXTENSION_TOPIC)
public class UserGrowthConsumer implements RocketMQListener<Long> {

    @Autowired
    private UserExtensionService userExtensionService;

    @Override
    public void onMessage(Long orderId) {
        userExtensionService.updateScoreAndGrowth(orderId);
    }

}
