package com.mall4j.cloud.distribution.listener;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.distribution.bo.DistributionUserUpdateBO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.distribution.model.DistributionUser;
import com.mall4j.cloud.distribution.service.DistributionUserService;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 修改分销员的昵称和分销员的头像
 * @author cl
 * @date 2021-08-20 09:49:52
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.USER_NOTIFY_DISTRIBUTION_USER_TOPIC, consumerGroup = "GID_"+RocketMqConstant.USER_NOTIFY_DISTRIBUTION_USER_TOPIC)
public class UserNotifyDistributionConsumer implements RocketMQListener<DistributionUserUpdateBO> {

    private static final Logger logger = LoggerFactory.getLogger(UserNotifyDistributionConsumer.class);

    @Autowired
    private DistributionUserService distributionUserService;

    @Override
    public void onMessage(DistributionUserUpdateBO message) {
        logger.info("修改分销员的昵称和分销员的头像回调开始... message: " + Json.toJsonString(message));
        if (Objects.isNull(message)) {
            return;
        }
        Long userId = message.getUserId();
        String nickName = message.getNickName();
        String pic = message.getPic();
        DistributionUserVO distributionUserVO = distributionUserService.getByUserId(userId);
        if (Objects.isNull(distributionUserVO)) {
            return;
        }
        // 修改分销员的昵称和分销员的头像
        DistributionUser update = new DistributionUser();
        update.setDistributionUserId(distributionUserVO.getDistributionUserId());
        update.setNickName(StrUtil.isBlank(nickName)? distributionUserVO.getNickName() : nickName);
        update.setPic(StrUtil.isBlank(pic)? distributionUserVO.getPic() : pic);
        distributionUserService.update(update);
        distributionUserService.removeCacheByUserId(userId);
    }
}
