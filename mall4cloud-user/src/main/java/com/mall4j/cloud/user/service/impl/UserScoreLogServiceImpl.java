package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.dto.CountFriendAssistanceRespDto;
import com.mall4j.cloud.user.dto.PageFriendAssistanceRespDto;
import com.mall4j.cloud.user.mapper.UserScoreLogMapper;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.service.UserScoreLogService;
import com.mall4j.cloud.user.vo.UserScoreLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户积分记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@Service
public class UserScoreLogServiceImpl implements UserScoreLogService {

    @Autowired
    private UserScoreLogMapper userScoreLogMapper;

    @Override
    public PageVO<UserScoreLogVO> page(PageDTO pageDTO) {
        Long userId = AuthUserContext.get().getUserId();
        return PageUtil.doPage(pageDTO, () -> userScoreLogMapper.list(userId));
    }

    @Override
    public PageVO<UserScoreLogVO> pageByIoTypeAndSource(PageDTO pageDTO, Integer ioType, Integer source) {
        Long userId = AuthUserContext.get().getUserId();
        return PageUtil.doPage(pageDTO, () -> userScoreLogMapper.listByIoTypeAndSource(userId,ioType,source));
    }

    @Override
    public PageVO<UserScoreLogVO> pageByUserId(PageDTO pageDTO, Long userId) {
        return PageUtil.doPage(pageDTO, () -> userScoreLogMapper.list(userId));
    }

    @Override
    public UserScoreLog getByLogId(Long logId) {
        return userScoreLogMapper.getByLogId(logId);
    }

    @Override
    public void save(UserScoreLog userScoreLog) {
        userScoreLogMapper.save(userScoreLog);
    }

    @Override
    public void update(UserScoreLog userScoreLog) {
        userScoreLogMapper.update(userScoreLog);
    }

    @Override
    public void deleteById(Long logId) {
        userScoreLogMapper.deleteById(logId);
    }

    @Override
    public int saveBatch(List<UserScoreLog> userScoreLogs) {
        return userScoreLogMapper.saveBatch(userScoreLogs);
    }

    @Override
    public Integer countByUserIdAndDateTimeAndType(Integer type, Long userId, DateTime beginOfDay, DateTime endOfDay) {
        return userScoreLogMapper.countByUserIdAndDateTimeAndType(type,userId,beginOfDay,endOfDay);
    }

    @Override
    public Integer getConsecutiveDays(Long userId) {
        return userScoreLogMapper.getConsecutiveDays(userId);
    }

    @Override
    public UserScoreLog getOrderScoreLog(Long userId, Long bizId, Integer source, Integer ioType) {
        return userScoreLogMapper.getOrderScoreLog(userId,bizId,source,ioType);
    }

    @Override
    public PageVO<UserScoreLogVO> scoreProdPage(PageDTO pageDTO) {
        PageVO<UserScoreLogVO> userScoreLogPage = PageUtil.doPage(pageDTO, () -> userScoreLogMapper.listScoreLog(AuthUserContext.get().getUserId()));
        if (CollUtil.isEmpty(userScoreLogPage.getList())) {
            return userScoreLogPage;
        }
        return userScoreLogPage;
    }

    @Override
    public Boolean isFriendAssistance(Long userId, Long bizId) {
        return userScoreLogMapper.isFriendAssistance(userId,bizId)>0;
    }

    @Override
    public PageVO<PageFriendAssistanceRespDto> pageFriendAssistance(PageDTO dto) {
        Long userId = AuthUserContext.get().getUserId();
        return PageUtil.doPage(dto, () -> userScoreLogMapper.pageFriendAssistance(userId));
    }

    @Override
    public CountFriendAssistanceRespDto countFriendAssistance() {
        Long userId = AuthUserContext.get().getUserId();
        return userScoreLogMapper.countFriendAssistanceByUserId(userId);
    }

    @Override
    public Integer countFriendAssistanceByBizId(Long bizId){
        return userScoreLogMapper.countFriendAssistanceByBizId(bizId);
    }
}
