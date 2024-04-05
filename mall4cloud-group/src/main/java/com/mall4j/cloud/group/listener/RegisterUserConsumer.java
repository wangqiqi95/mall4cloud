package com.mall4j.cloud.group.listener;

import com.mall4j.cloud.api.user.dto.UserRegisterGiftDTO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.group.service.RegisterActivityBizService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RocketMQMessageListener(topic = RocketMqConstant.USER_REGISTER_GIFT,consumerGroup = "GID_"+RocketMqConstant.USER_REGISTER_GIFT)
public class RegisterUserConsumer implements RocketMQListener<UserRegisterGiftDTO> {
    @Resource
    private RegisterActivityBizService registerActivityBizService;
    @Override
    public void onMessage(UserRegisterGiftDTO userRegisterGiftDTO) {
        registerActivityBizService.userRegisterActivity(userRegisterGiftDTO);
    }
}
