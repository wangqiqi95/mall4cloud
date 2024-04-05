package com.mall4j.cloud.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.user.config.ScoreClearingActivityConfig;
import com.mall4j.cloud.user.constant.ScoreLogTypeEnum;
import com.mall4j.cloud.user.dto.CountFriendAssistanceRespDto;
import com.mall4j.cloud.user.dto.PageFriendAssistanceRespDto;
import com.mall4j.cloud.user.dto.UserFriendAssistanceDto;
import com.mall4j.cloud.user.mapper.UserMapper;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.service.UserScoreActivityService;
import com.mall4j.cloud.user.service.UserScoreLogService;
import com.mall4j.cloud.user.vo.UserScoreLogVO;
import lombok.extern.slf4j.Slf4j;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @Description 用户积分活动
 * @Author axin
 * @Date 2022-10-17 14:24
 **/
@Slf4j
@Service
public class UserScoreActivityServiceImpl implements UserScoreActivityService {

    @Resource
    private UserScoreLogService userScoreLogService;
//    @Resource
//    private ScoreClearingActivityConfig config;
    @Resource
    private UserMapper userMapper;

    @Override
    public void friendAssistance(UserFriendAssistanceDto dto) {
        if(!isClearingPointsActivityDate()){
            throw new LuckException("不在活动时间内");
        }

        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException(ResponseEnum.ACCOUNT_NOT_REGISTER);
        }

        if(dto.getInviterUserId().equals(userInfoInTokenBO.getUserId())){
            throw new LuckException("不能给自己助力");
        }

        //1.校验用户是否已助力过
        if(userScoreLogService.isFriendAssistance(dto.getInviterUserId(), userInfoInTokenBO.getUserId())){
            throw new LuckException(ResponseEnum.USER_FRIED_ASSISTANCE);
        }

//        if(userScoreLogService.countFriendAssistanceByBizId(userInfoInTokenBO.getUserId())>=config.getMaxBigHelp()){
//            throw new LuckException("助力次数已达上限");
//        }

        //3.记录添加的积分
        UserScoreLog userScoreLog = new UserScoreLog();
        userScoreLog.setUserId(dto.getInviterUserId());
        userScoreLog.setSource(ScoreLogTypeEnum.USER_AWAKEN.value());
        userScoreLog.setIoType(1);
        userScoreLog.setBizId(userInfoInTokenBO.getUserId());
//        userScoreLog.setScore(config.getOldUserScore().longValue());
        userScoreLogService.save(userScoreLog);

    }

    @Override
    public PageVO<PageFriendAssistanceRespDto> pageFriendAssistance(PageDTO dto) {
        return userScoreLogService.pageFriendAssistance(dto);
    }

    @Override
    public CountFriendAssistanceRespDto countFriendAssistance() {
        return userScoreLogService.countFriendAssistance();
    }

    @Override
    public String friendAssistancePoster() {
//        return config.getActivityPoster();
        return null;
    }

    @Override
    public Boolean isFriendAssistance(Long inviterUserId) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if(Objects.isNull(userInfoInTokenBO)){
            throw new LuckException(ResponseEnum.ACCOUNT_NOT_REGISTER);
        }
        return userScoreLogService.isFriendAssistance(inviterUserId, userInfoInTokenBO.getUserId());
    }

    @Override
    public Boolean isClearingPointsActivityDate() {
//        long time = new Date().getTime();
//        if(time<config.getStartDateTime().getTime() || time>config.getEndDateTime().getTime()){
//            return false;
//        }
        return true;
    }
}
