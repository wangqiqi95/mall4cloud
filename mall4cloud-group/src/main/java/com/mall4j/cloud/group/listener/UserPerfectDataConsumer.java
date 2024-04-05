package com.mall4j.cloud.group.listener;

import cn.hutool.core.bean.BeanUtil;
import com.mall4j.cloud.api.group.feign.dto.UserPerfectDataActivityDTO;
import com.mall4j.cloud.api.user.dto.PerfectDataDTO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.group.service.PerfectDataActivityBizService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RocketMQMessageListener(topic = RocketMqConstant.USER_PERFECT_DATA,consumerGroup = "GID_"+RocketMqConstant.USER_PERFECT_DATA)
public class UserPerfectDataConsumer implements RocketMQListener<PerfectDataDTO> {
    @Resource
    private PerfectDataActivityBizService perfectDataActivityBizService;
    @Override
    public void onMessage(PerfectDataDTO perfectDataDTO) {
        UserPerfectDataActivityDTO userPerfectDataActivityDTO = BeanUtil.copyProperties(perfectDataDTO, UserPerfectDataActivityDTO.class);
        perfectDataActivityBizService.userPerfectDataActivity(userPerfectDataActivityDTO);
    }
}
