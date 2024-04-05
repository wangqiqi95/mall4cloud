package com.mall4j.cloud.user.listener;

import com.mall4j.cloud.api.auth.bo.UserRegisterNotifyBO;
import com.mall4j.cloud.api.auth.dto.UserRegisterDTO;
import com.mall4j.cloud.api.auth.dto.UserRegisterExtensionDTO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.user.model.User;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.service.UserService;
import ma.glasnost.orika.MapperFacade;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 账户注册成功监听
 * @author cl
 * @date 2021-05-14 14:49:32
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.BATCH_USER_REGISTER_TOPIC, consumerGroup = "GID_"+RocketMqConstant.BATCH_USER_REGISTER_TOPIC)
public class UserRegisterConsumer implements RocketMQListener<UserRegisterNotifyBO> {

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade mapperFacade;

    private static final Logger LOG = LoggerFactory.getLogger(UserRegisterConsumer.class);

    /**
     * 开始批量保存用户信息
     * @param message 批量注册消息
     */
    @Override
    public void onMessage(UserRegisterNotifyBO message) {
        LOG.info("批量注册用户信息开始... message: " + Json.toJsonString(message));
        List<UserRegisterDTO> userRegisterList = message.getUserRegisterList();
        List<UserRegisterExtensionDTO> registerExtensionDTOList = message.getUserRegisterExtensionDTOList();
        List<User> userList = mapperFacade.mapAsList(userRegisterList, User.class);
        List<UserExtension> userExtensionList = mapperFacade.mapAsList(registerExtensionDTOList, UserExtension.class);
        // 批量保存用户
        userService.batchUser(userList, userExtensionList);
    }
}
