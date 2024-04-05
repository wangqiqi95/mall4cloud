package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.constant.ScoreIoTypeEnum;
import com.mall4j.cloud.user.constant.ScoreLogTypeEnum;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.model.UserScoreGetLog;
import com.mall4j.cloud.user.mapper.UserScoreGetLogMapper;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.service.UserScoreGetLogService;
import com.mall4j.cloud.user.service.UserScoreLogService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户积分获取记录
 *
 * @author FrozenWatermelon
 * @date 2021-05-17 17:17:14
 */
@Service
public class UserScoreGetLogServiceImpl implements UserScoreGetLogService {

    @Autowired
    private UserScoreGetLogMapper userScoreGetLogMapper;
    @Autowired
    private UserExtensionService userExtensionService;
    @Autowired
    private UserScoreLogService userScoreLogService;


    @Override
    public PageVO<UserScoreGetLog> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userScoreGetLogMapper.list());
    }

    @Override
    public UserScoreGetLog getByUserScoreGetLogId(Long userScoreGetLogId) {
        return userScoreGetLogMapper.getByUserScoreGetLogId(userScoreGetLogId);
    }

    @Override
    public void save(UserScoreGetLog userScoreGetLog) {
        userScoreGetLogMapper.save(userScoreGetLog);
    }

    @Override
    public void update(UserScoreGetLog userScoreGetLog) {
        userScoreGetLogMapper.update(userScoreGetLog);
    }

    @Override
    public void deleteById(Long userScoreGetLogId) {
        userScoreGetLogMapper.deleteById(userScoreGetLogId);
    }

    @Override
    public void saveBatch(List<UserScoreGetLog> userScoreGetLogs) {
        userScoreGetLogMapper.saveBatch(userScoreGetLogs);
    }

    @Override
    public UserScoreGetLog getLogByUserId(Long userId) {
        return userScoreGetLogMapper.getLogByUserId(userId);
    }

    @Override
    public List<UserScoreGetLog> listByUserIdAndExpireTimeAndStatus(Long userId, Date expireTime, Integer status) {
        return userScoreGetLogMapper.listByUserIdAndExpireTimeAndStatus(userId, expireTime, status);
    }

    @Override
    public void updateBatchById(List<UserScoreGetLog> userScoreGetLogList) {
        userScoreGetLogMapper.updateBatchById(userScoreGetLogList);
    }

    @Override
    public Long sumUsableScoreByPage(Long userId, Integer status, Integer current, Integer size) {
        return userScoreGetLogMapper.sumUsableScoreByPage(userId, status, current, size);
    }

    @Override
    public List<UserScoreGetLog> listByCreateTime(Long userId, Integer status) {
        return userScoreGetLogMapper.listByCreateTime(userId, status);
    }

    @Override
    public List<UserScoreGetLog> listByCreateTimeAndPage(Long userId, Integer status, Integer current, Integer size) {
        return userScoreGetLogMapper.listByCreateTimeAndPage(userId, status, current, size);
    }

    @Override
    public List<UserScoreGetLog> batchListByCreateTime(List<Long> userIds, Integer status) {
        return userScoreGetLogMapper.batchListByCreateTime(userIds,status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExpireScoreDetail(DateTime dateTime) {
        // 先修改状态为0的积分明细为过期状态
        userScoreGetLogMapper.updateExpireScoreDetail(dateTime);
        // 1.查询有积分过期的用户
        List<UserScoreGetLog> userScoreDetails = userScoreGetLogMapper.listExpireScoreDetail(dateTime);
        if(CollUtil.isEmpty(userScoreDetails)){
            return;
        }
        // 2.修改用户积分信息
        Map<Long, List<UserScoreGetLog>> userScoreMap = userScoreDetails.stream().collect(Collectors.groupingBy(UserScoreGetLog::getUserId));
        List<UserScoreLog> logList = new ArrayList<>();
        List<UserExtension> userExtensions = new ArrayList<>();
        for (Long userId : userScoreMap.keySet()) {
            List<UserScoreGetLog> detailList = userScoreMap.get(userId);
            long expireScore = detailList.stream().mapToLong(UserScoreGetLog::getUsableScore).sum();
            UserExtension userExtension = new UserExtension();
            userExtension.setUserId(userId);
            // 减少这么多积分
            userExtension.setScore(-expireScore);
            userExtensions.add(userExtension);

            // 添加积分日志
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userId);
            userScoreLog.setScore(expireScore);
            userScoreLog.setSource(ScoreLogTypeEnum.EXPIRE.value());
            userScoreLog.setIoType(ScoreIoTypeEnum.EXPENDITURE.value());
            logList.add(userScoreLog);
        }
        // 批量修改用户积分和添加积分日志
        if(CollUtil.isNotEmpty(userExtensions)){
            userExtensionService.batchUpdateScore(userExtensions);
        }
        if(CollUtil.isNotEmpty(logList)){
            userScoreLogService.saveBatch(logList);
        }
        // 3.修改积分明细
        DateTime now = DateUtil.date();
        for (UserScoreGetLog userScoreDetail : userScoreDetails) {
            userScoreDetail.setStatus(-1);
            userScoreDetail.setExpireTime(now);
        }
        userScoreGetLogMapper.updateBatchById(userScoreDetails);
    }

    @Override
    public UserScoreGetLog getLogByBizId(Long bizId) {
        return userScoreGetLogMapper.getLogByBizId(bizId);
    }
}
