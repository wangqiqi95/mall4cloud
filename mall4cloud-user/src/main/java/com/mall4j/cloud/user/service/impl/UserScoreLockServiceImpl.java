package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.aliyun.openservices.ons.api.SendResult;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.user.bo.UserScoreBO;
import com.mall4j.cloud.api.user.dto.UserScoreLockDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.user.constant.ScoreGetLogStatusEnum;
import com.mall4j.cloud.user.constant.ScoreLogTypeEnum;
import com.mall4j.cloud.user.mapper.UserExtensionMapper;
import com.mall4j.cloud.user.mapper.UserScoreGetLogMapper;
import com.mall4j.cloud.user.mapper.UserScoreLockMapper;
import com.mall4j.cloud.user.model.UserScoreGetLog;
import com.mall4j.cloud.user.model.UserScoreLock;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.service.UserScoreGetLogService;
import com.mall4j.cloud.user.service.UserScoreLockService;
import com.mall4j.cloud.user.service.UserScoreLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 积分锁定信息
 *
 * @author FrozenWatermelon
 * @date 2021-05-19 19:54:55
 */
@Service
public class UserScoreLockServiceImpl implements UserScoreLockService {

    @Autowired
    private UserScoreLockMapper userScoreLockMapper;
    @Autowired
    private UserExtensionMapper userExtensionMapper;
    @Autowired
    private UserScoreGetLogMapper userScoreGetLogMapper;
    @Autowired
    private UserScoreLogService userScoreLogService;
    @Autowired
    private UserScoreGetLogService userScoreGetLogService;
    @Autowired
    private UserExtensionService userExtensionService;
//    @Autowired
//    private OnsMQTemplate userScoreTemplate;

    @Override
    public PageVO<UserScoreLock> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userScoreLockMapper.list());
    }

    @Override
    public UserScoreLock getById(Long id) {
        return userScoreLockMapper.getById(id);
    }

    @Override
    public void save(UserScoreLock userScoreLock) {
        userScoreLockMapper.save(userScoreLock);
    }

    @Override
    public void update(UserScoreLock userScoreLock) {
        userScoreLockMapper.update(userScoreLock);
    }

    @Override
    public void deleteById(Long id) {
        userScoreLockMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void  lock(List<UserScoreLockDTO> userScoreLocks) {
        Long userId = AuthUserContext.get().getUserId();
        Integer orderType = userScoreLocks.get(0).getOrderType();
        // 1.先把已经过期但还没标记的积分全部查出来，标记成已过期，更新数据
        List<UserScoreGetLog> expireUserScoreGetLogList = userScoreGetLogService.listByUserIdAndExpireTimeAndStatus(userId, new Date(), 1);
        if (!CollUtil.isEmpty(expireUserScoreGetLogList)) {
            Date date = new Date();
            for (UserScoreGetLog userScoreGetLog : expireUserScoreGetLogList) {
                userScoreGetLog.setUpdateTime(date);
                userScoreGetLog.setStatus(-1);
            }
            userScoreGetLogService.updateBatchById(expireUserScoreGetLogList);
            // 计算过期的积分总和
            long expireScore = expireUserScoreGetLogList.stream().mapToLong(UserScoreGetLog::getUsableScore).sum();
            // 将过期积分写入积分变动日志记录表
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setCreateTime(date);
            userScoreLog.setUpdateTime(date);
            userScoreLog.setUserId(userId);
            userScoreLog.setSource(ScoreLogTypeEnum.EXPIRE.value());
            userScoreLog.setScore(expireScore);
            userScoreLog.setBizId(null);
            userScoreLog.setIoType(0);
            userScoreLogService.save(userScoreLog);
            // 修改 user_extension 表里的积分
            List<Long> longList = new ArrayList<>();
            longList.add(userId);
            userExtensionService.updateUserScoreOrGrowth(longList, -expireScore, null);
        }

        // 2.开始锁定积分
        List<UserScoreLock> userScoreLockLogs = Lists.newArrayList();
        Set<Long> orderIds = new HashSet<>();
        Long useScore = 0L;
        useScore = addUserScoreLockLog(userScoreLocks, userId, userScoreLockLogs, orderIds, useScore);
        if (!userScoreLockLogs.isEmpty()){
            // 批量保存锁定记录
            userScoreLockMapper.saveBatch(userScoreLockLogs);
            // 扣减用户积分
            int updateStatus = userExtensionMapper.lockScoreBySubmitOrder(userId, useScore);
            if (updateStatus < 1) {
                throw new LuckException("积分不足");
            }

            UserScoreLog userScoreLog = new UserScoreLog();
            Date date = new Date();
            userScoreLog.setCreateTime(date);
            userScoreLog.setUpdateTime(date);
            userScoreLog.setUserId(userId);
            if (Objects.equals(orderType, OrderType.SCORE.value())) {
                userScoreLog.setSource(ScoreLogTypeEnum.SCORE_PRODUCT_EXCHANGE.value());
                userScoreLog.setBizId(userScoreLocks.get(0).getOrderId());
            } else {
                userScoreLog.setSource(ScoreLogTypeEnum.SCORE_CASH.value());
                userScoreLog.setBizId(null);
            }
            userScoreLog.setScore(useScore);
            userScoreLog.setIoType(0);
            userScoreLogService.save(userScoreLog);

            // 发送消息一个小时后解锁积分(包括哪些订单)
            List<Long> orderIds2 = new ArrayList<>(orderIds);
            UserScoreBO userScoreBo = new UserScoreBO();
            userScoreBo.setUserId(userId);
            userScoreBo.setOrderIds(orderIds2);
//            SendResult sendResult = userScoreTemplate.syncSend(userScoreBo, RocketMqConstant.CANCEL_ORDER_DELAY_LEVEL + 1);
//            if (sendResult == null || sendResult.getMessageId() == null) {
//                    // 消息发不出去就抛异常，发的出去无所谓
//                    throw new LuckException(ResponseEnum.EXCEPTION);
//            }
        }

    }

    /**
     * 添加用户积分锁定日志
     * @param userScoreLocks 锁定积分日志
     * @param userId 用户id
     * @param userScoreLockLogs 用户积分锁定日志
     * @param orderIds 订单id列表
     * @param useScore 用户积分
     * @return 用户积分
     */
    private Long addUserScoreLockLog(List<UserScoreLockDTO> userScoreLocks, Long userId, List<UserScoreLock> userScoreLockLogs, Set<Long> orderIds, Long useScore) {
        for (UserScoreLockDTO userScoreLockParam : userScoreLocks) {
            if (Objects.nonNull(userScoreLockParam.getScore())) {
                List<UserScoreGetLog> temp = userScoreGetLogService.listByCreateTime(userId, 1);
                long sumTotalUsableScore = temp.stream().mapToLong(UserScoreGetLog::getUsableScore).sum();
                if (sumTotalUsableScore < userScoreLockParam.getScore()) {
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
                if (!temp.isEmpty()) {
                    Date date = new Date();
                    // 按积分创建时间优先扣减
                    Long usableScore = userScoreLockParam.getScore();
                    List<UserScoreGetLog> userScoreGetLogList;
                    long sumUsableScore;
                    int i = 1;
                    List<Long> scoreGetLogIds = new ArrayList<>();
                    do {
                        userScoreGetLogList = userScoreGetLogService.listByCreateTimeAndPage(userId, 1, 0, i * 10);
                        sumUsableScore = userScoreGetLogList.stream().mapToLong(UserScoreGetLog::getUsableScore).sum();
                        if (usableScore > sumUsableScore) {
                            i = i + 1;
                        }
                    } while (usableScore > sumUsableScore);

                    for (UserScoreGetLog userScoreGetLog : userScoreGetLogList) {
                        if (usableScore > 0) {
                            if (usableScore >= userScoreGetLog.getUsableScore()) {
                                userScoreGetLog.setUpdateTime(date);
                                userScoreGetLog.setStatus(0);
                                userScoreGetLogService.update(userScoreGetLog);

                                usableScore = usableScore - userScoreGetLog.getUsableScore();
                                scoreGetLogIds.add(userScoreGetLog.getUserScoreGetLogId());
                            } else {
                                UserScoreGetLog userScoreGetLog2 = new UserScoreGetLog();
                                userScoreGetLog2.setCreateTime(userScoreGetLog.getCreateTime());
                                userScoreGetLog2.setUpdateTime(date);
                                userScoreGetLog2.setUserId(userScoreGetLog.getUserId());
                                userScoreGetLog2.setUsableScore(userScoreGetLog.getUsableScore() - usableScore);
                                userScoreGetLog2.setBizId(userScoreGetLog.getBizId());
                                userScoreGetLog2.setExpireTime(userScoreGetLog.getExpireTime());
                                userScoreGetLog2.setStatus(userScoreGetLog.getStatus());
                                userScoreGetLogService.save(userScoreGetLog2);
                                scoreGetLogIds.add(userScoreGetLog2.getUserScoreGetLogId());

                                userScoreGetLog.setUpdateTime(date);
                                userScoreGetLog.setUsableScore(usableScore);
                                userScoreGetLog.setStatus(0);
                                userScoreGetLogService.update(userScoreGetLog);
                                scoreGetLogIds.add(userScoreGetLog.getUserScoreGetLogId());
                                usableScore = 0L;
                            }
                        }
                    }

                    if (!scoreGetLogIds.isEmpty()) {
                        UserScoreLock userScoreLock = new UserScoreLock();
                        userScoreLock.setUserId(userId);
                        userScoreLock.setScore(userScoreLockParam.getScore());
                        userScoreLock.setOrderId(userScoreLockParam.getOrderId());
                        userScoreLock.setScoreGetLogIds(Json.toJsonString(scoreGetLogIds));
                        useScore += userScoreLock.getScore();
                        userScoreLock.setStatus(0);
                        userScoreLockLogs.add(userScoreLock);
                        orderIds.add(userScoreLockParam.getOrderId());
                    }
                }
            }
        }
        return useScore;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlock(UserScoreBO userScoreBo) {
    }
}
