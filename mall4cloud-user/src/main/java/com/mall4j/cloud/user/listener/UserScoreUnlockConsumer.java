package com.mall4j.cloud.user.listener;

import com.mall4j.cloud.api.user.bo.UserScoreBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.user.service.UserScoreLockService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author FrozenWatermelon
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SCORE_UNLOCK_TOPIC,consumerGroup = "GID_"+RocketMqConstant.SCORE_UNLOCK_TOPIC)
public class UserScoreUnlockConsumer implements RocketMQListener<UserScoreBO> {

    @Autowired
    private UserScoreLockService userScoreLockService;

    /**
     * 1、积分锁定一定时间后，如果订单支付未支付，则解锁积分（有可能积分锁定成功，订单因为异常回滚导致订单未创建）
     * 2、取消订单，直接解锁积分
     */
    @Override
    public void onMessage(UserScoreBO userScoreBo) {
        userScoreLockService.unlock(userScoreBo);
    }
}
