package com.mall4j.cloud.user.listener;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.user.feign.crm.CrmUserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.user.config.ScoreClearingActivityConfig;
import com.mall4j.cloud.user.constant.ScoreLogTypeEnum;
import com.mall4j.cloud.user.dto.UserPullNewDTO;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.service.UserScoreLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description 用户拉新
 * @Author axin
 * @Date 2022-10-12 15:01
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.USER_PULL_NEW_TOPIC, consumerGroup = "GID_"+RocketMqConstant.USER_PULL_NEW_TOPIC)
public class UserPullNewConsumer implements RocketMQListener<UserPullNewDTO> {

    @Resource
    private CrmUserFeignClient crmUserFeignClient;
    @Resource
    private UserScoreLogService userScoreLogService;
//    @Resource
//    private ScoreClearingActivityConfig config;
    @Resource
    private UserMapper userMapper;


    @Override
    public void onMessage(UserPullNewDTO message) {
//        log.info("处理用户拉新消息开始，入参{}", JSON.toJSONString(message));
//        long time = new Date().getTime();
//        if(time<config.getStartDateTime().getTime() || time>config.getEndDateTime().getTime()){
//            log.info("不在活动时间内");
//            return;
//        }
//
//        //1.校验用户是否已助力过
//        if(userScoreLogService.isFriendAssistance(message.getInviterUserId(), message.getInviteeUserId())){
//            log.info("用户已助力过,结束消息{}",JSON.toJSONString(message));
//            return;
//        }
//        //2.调用crm系统添加积分
//        UserApiVO user = userMapper.getByUserId(message.getInviterUserId());
//
//        //3.记录添加的积分
//        UserScoreLog userScoreLog = new UserScoreLog();
//        userScoreLog.setUserId(message.getInviterUserId());
//        userScoreLog.setSource(ScoreLogTypeEnum.USER_PULL_NEW.value());
//        userScoreLog.setIoType(1);
//        userScoreLog.setBizId(message.getInviteeUserId());
//        userScoreLog.setScore(config.getNewUserScore().longValue());
//        userScoreLogService.save(userScoreLog);
    }
}
