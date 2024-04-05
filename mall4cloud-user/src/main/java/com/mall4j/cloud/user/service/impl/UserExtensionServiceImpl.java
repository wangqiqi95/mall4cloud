package com.mall4j.cloud.user.service.impl;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.api.platform.feign.ConfigFeignClient;
import com.mall4j.cloud.api.user.bo.UserOrderScoreBo;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.OrderCloseType;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.user.constant.*;
import com.mall4j.cloud.user.mapper.UserExtensionMapper;
import com.mall4j.cloud.user.model.*;
import com.mall4j.cloud.user.service.*;
import com.mall4j.cloud.user.vo.GrowthCompleteConfigVO;
import com.mall4j.cloud.user.vo.ScoreCompleteConfigVO;
import com.mall4j.cloud.user.vo.UserExtensionVO;
import com.mall4j.cloud.user.vo.UserRightsVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户扩展信息
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
@Service
public class UserExtensionServiceImpl implements UserExtensionService {

    private static final Logger log = LoggerFactory.getLogger(UserExtensionServiceImpl.class);

    @Autowired
    private UserExtensionMapper userExtensionMapper;
    @Autowired
    private ConfigFeignClient configFeignClient;
    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private UserRightsService userRightsService;
    @Autowired
    private UserScoreLogService userScoreLogService;
    @Autowired
    private UserScoreGetLogService userScoreGetLogService;
    @Autowired
    private UserGrowthLogService userGrowthLogService;
    @Autowired
    private UserBalanceLogService userBalanceLogService;


    @Override
    public PageVO<UserExtensionVO> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userExtensionMapper.list());
    }

    @Override
    public UserExtension getByUserExtensionId(Long userExtensionId) {
        return userExtensionMapper.getByUserExtensionId(userExtensionId);
    }

    @Override
    public void save(UserExtension userExtension) {
        UserExtension byUserId = userExtensionMapper.getByUserId(userExtension.getUserId());
        if (byUserId != null) {
            userExtension.setUserExtensionId(byUserId.getUserExtensionId());
            userExtensionMapper.update(userExtension);
        } else {
            userExtensionMapper.save(userExtension);
        }
    }

    @Override
    public void update(UserExtension userExtension) {
        userExtensionMapper.update(userExtension);
    }

    @Override
    public void deleteById(Long userExtensionId) {
        userExtensionMapper.deleteById(userExtensionId);
    }

    @Override
    public void updateUserLevel(Integer level, int minGrowth, Integer maxGrowth) {
        userExtensionMapper.updateUserLevel(level, minGrowth, maxGrowth);
    }

    @Override
    public void updateUserScoreOrGrowth(List<Long> userIds, Long score, Integer growth) {
        if (Objects.isNull(score) && Objects.isNull(growth)) {
            return;
        }
        //增加/减少积分或者成长值 , sql中处理积分和成长值不能为负数，最小为0
        userExtensionMapper.updateUserScoreOrGrowth(userIds, score, growth);
    }

    @Override
    public void reduceScore(Long userId, Long score) {
        if (Objects.isNull(score)) {
            return;
        }
        //减少积分或者成长值 , sql中处理积分和成长值不能为负数，最小为0
        userExtensionMapper.reduceScore(userId, score);
    }

    @Override
    public List<UserExtension> getByUserIdsAndLevelType(List<Long> userIds) {
        return userExtensionMapper.getByUserIdsAndLevelType(userIds);
    }

    @Override
    public UserExtension getByUserId(Long userId) {
        return userExtensionMapper.getByUserId(userId);
    }

    @Override
    public void batchUpdateLevelByUserIds(List<Long> userIds,Integer levelType) {
        userExtensionMapper.batchUpdateLevelByUserIds(userIds,levelType);
    }

    @Override
    public int updateBatchUserBalanceByUserIds(List<Long> userIds, Long balance, DateTime now) {
        return userExtensionMapper.updateBatchUserBalanceByUserIds(userIds,balance,now);
    }

    @Override
    public int saveBatch(List<UserExtension> userExtensionList) {
        return userExtensionMapper.saveBatch(userExtensionList);
    }

    @Override
    public int batchUpdateScore(List<UserExtension> userExtensions) {
        return userExtensionMapper.batchUpdateScore(userExtensions);
    }

    @Override
    public void updateScoreAndGrowth(Long orderId) {

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refundScore(UserOrderScoreBo userOrderScoreBo) {
        Long orderId = userOrderScoreBo.getOrderId();

        UserScoreGetLog userScoreGetLog = userScoreGetLogService.getLogByBizId(orderId);
        if (Objects.nonNull(userScoreGetLog)) {
            Long completeOrderBack = userScoreGetLog.getUsableScore();
            UserScoreLog userScoreLog2 = new UserScoreLog();
            userScoreLog2.setUserId(userOrderScoreBo.getUserId());
            userScoreLog2.setSource(ScoreLogTypeEnum.COMPLETE_ORDER_BACK.value());
            userScoreLog2.setScore(completeOrderBack);
            userScoreLog2.setBizId(userOrderScoreBo.getRefundId());
            userScoreLog2.setIoType(ScoreIoTypeEnum.EXPENDITURE.value());
            userScoreLogService.save(userScoreLog2);
            userScoreGetLog.setStatus(-1);
            userScoreGetLogService.update(userScoreGetLog);
            userExtensionMapper.updateUserScoreOrGrowth(Collections.singletonList(userOrderScoreBo.getUserId()), -completeOrderBack, null);
        }

        // 如果没有使用积分抵扣就不需要退回积分给用户
        if (Objects.isNull(userOrderScoreBo.getOrderScore()) || Objects.equals(0L, userOrderScoreBo.getOrderScore())) {
            return;
        }

        UserScoreLog userScoreLog = new UserScoreLog();
//        userScoreLog.setUserId(orderBO.getUserId());
        userScoreLog.setSource(ScoreLogTypeEnum.SCORE_CASH_BACK.value());
//        userScoreLog.setScore(orderScore);
//        userScoreLog.setBizId(refundId);
        userScoreLog.setIoType(ScoreIoTypeEnum.INCOME.value());

        UserScoreGetLog addDetail = new UserScoreGetLog();
        addDetail.setStatus(ScoreGetLogStatusEnum.NORMAL.value());
//        addDetail.setUserId(userId);
//        addDetail.setUsableScore(orderScore);

        userScoreLogService.save(userScoreLog);
        userScoreGetLogService.save(addDetail);
//        userExtensionMapper.updateUserScoreOrGrowth(Collections.singletonList(userId), orderScore,null);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerUserScoreGrowthBalanceLog(List<UserExtension> userExtensionList) {
        // 积分、成长值、余额日志
        if (CollUtil.isEmpty(userExtensionList)) {
            return;
        }
        List<UserScoreLog> userScoreLogs = new ArrayList<>();
        List<UserScoreGetLog> userScoreDetails = new ArrayList<>();
        List<UserGrowthLog> userGrowthLogs = new ArrayList<>();
        List<UserBalanceLog> userBalanceLogs = new ArrayList<>();
        for (UserExtension userExtension : userExtensionList) {
            Long userId = userExtension.getUserId();
            Integer growth = userExtension.getGrowth();
            Long score = userExtension.getScore();
            Long balance = userExtension.getBalance();
            // 积分日志
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userId);
            userScoreLog.setSource(ScoreLogTypeEnum.SYSTEM.value());
            userScoreLog.setScore(score);
            userScoreLog.setIoType(ScoreIoTypeEnum.INCOME.value());
            userScoreLogs.add(userScoreLog);
            // 积分明细
            UserScoreGetLog userScoreGetLog = new UserScoreGetLog();
            userScoreGetLog.setUserId(userId);
            userScoreGetLog.setUsableScore(score);
            userScoreGetLog.setStatus(ScoreGetLogStatusEnum.NORMAL.value());
            userScoreDetails.add(userScoreGetLog);
            // 成长值
            UserGrowthLog userGrowthLog = new UserGrowthLog();
            userGrowthLog.setUserId(userId);
            userGrowthLog.setSource(GrowthLogSourceEnum.SYSTEM.value());
            userGrowthLog.setChangeGrowth(growth);
            userGrowthLog.setRemarks("导入初始化设置定积分");
            userGrowthLogs.add(userGrowthLog);
            // 余额日志
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setUserId(userId);
            userBalanceLog.setChangeBalance(balance);
            userBalanceLog.setIoType(RechargeIoTypeEnum.INCOME.value());
            userBalanceLog.setType(RechargeTypeEnum.SYSTEM.value());
            userBalanceLogs.add(userBalanceLog);
        }
        if (CollUtil.isNotEmpty(userScoreLogs)) {
            userScoreLogService.saveBatch(userScoreLogs);
            userScoreGetLogService.saveBatch(userScoreDetails);
        }
        if (CollUtil.isNotEmpty(userGrowthLogs)) {
            userGrowthLogService.saveBatch(userGrowthLogs);
        }
        if (CollUtil.isNotEmpty(userBalanceLogs)) {
            userBalanceLogService.saveBatch(userBalanceLogs);
        }
    }

}
