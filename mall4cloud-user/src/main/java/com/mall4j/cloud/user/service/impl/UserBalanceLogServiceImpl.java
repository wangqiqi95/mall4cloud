package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.user.bo.BalanceRefundBO;
import com.mall4j.cloud.api.user.bo.RechargeNotifyBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.user.constant.*;
import com.mall4j.cloud.user.mapper.UserBalanceLogMapper;
import com.mall4j.cloud.user.mapper.UserExtensionMapper;
import com.mall4j.cloud.user.mapper.UserGrowthLogMapper;
import com.mall4j.cloud.user.mapper.UserScoreLogMapper;
import com.mall4j.cloud.user.model.UserBalanceLog;
import com.mall4j.cloud.user.model.UserGrowthLog;
import com.mall4j.cloud.user.model.UserScoreLog;
import com.mall4j.cloud.user.service.UserBalanceLogService;
import com.mall4j.cloud.user.service.UserRechargeService;
import com.mall4j.cloud.user.vo.UserBalanceLogVO;
import com.mall4j.cloud.user.vo.UserRechargeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 余额记录
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@Service
public class UserBalanceLogServiceImpl implements UserBalanceLogService {

    @Autowired
    private UserBalanceLogMapper userBalanceLogMapper;
    @Autowired
    private UserRechargeService userRechargeService;
    @Autowired
    private UserExtensionMapper userExtensionMapper;
//    @Autowired
//    private OnsMQTemplate couponGiveTemplate;
    @Autowired
    private UserGrowthLogMapper userGrowthLogMapper;
    @Autowired
    private UserScoreLogMapper userScoreLogMapper;

    @Override
    public PageVO<UserBalanceLog> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> userBalanceLogMapper.list());
    }

    @Override
    public UserBalanceLog getByBalanceLogId(Long balanceLogId) {
        return userBalanceLogMapper.getByBalanceLogId(balanceLogId);
    }

    @Override
    public void save(UserBalanceLog userBalanceLog) {
        userBalanceLogMapper.save(userBalanceLog);
    }

    @Override
    public void update(UserBalanceLog userBalanceLog) {
        userBalanceLogMapper.update(userBalanceLog);
    }

    @Override
    public void deleteById(Long balanceLogId) {
        userBalanceLogMapper.deleteById(balanceLogId);
    }

    @Override
    public PageVO<UserBalanceLogVO> getPageByUserId(PageDTO pageDTO, Long userId) {
        PageVO<UserBalanceLogVO> pageVO = new PageVO<>();
        List<UserBalanceLogVO> userBalanceLogs = userBalanceLogMapper.listByUserId(new PageAdapter(pageDTO), userId);
        // 通过支付单号或者退款单号查询订单号
        List<Long> refundIds = new ArrayList<>();
        List<Long> payIds = new ArrayList<>();
        userBalanceLogs.forEach(item -> {
            if (Objects.equals(item.getType(), RechargeTypeEnum.PAY.value()) && Objects.nonNull(item.getPayId())) {
                payIds.add(item.getPayId());
            } else if (Objects.equals(item.getType(), RechargeTypeEnum.REFUND.value()) && Objects.nonNull(item.getRefundId())) {
                refundIds.add(item.getRefundId());
            }
        });
        pageVO.setList(userBalanceLogs);
        pageVO.setTotal(userBalanceLogMapper.countByUserId(userId));
        pageVO.setPages(PageUtil.getPages(pageVO.getTotal(), pageDTO.getPageSize()));
        return pageVO;
    }

    @Override
    public Long saveBatch(List<UserBalanceLog> userBalanceLogs) {
        return userBalanceLogMapper.saveBatch(userBalanceLogs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechargeSuccess(RechargeNotifyBO message) {
        Long balanceLogId = message.getBalanceLogId();
        UserBalanceLog userBalanceLogDb = userBalanceLogMapper.getByBalanceLogId(balanceLogId);
        if (Objects.equals(userBalanceLogDb.getIsPayed(), 1)) {
            return;
        }
        Long rechargeId = userBalanceLogDb.getRechargeId();
        UserRechargeVO userRechargeVO = userRechargeService.getRechargeInfo(rechargeId);
        if (userRechargeVO != null) {
            // 增加成长值、积分、余额
            userExtensionMapper.updateByUserRecharge(userRechargeVO,userBalanceLogDb);
            addPreLog(userBalanceLogDb, userRechargeVO);
        } else {
            // 更新用户余额
            userExtensionMapper.addByUserBalanceLog(userBalanceLogDb);
        }
        // 更新支付状态
        int updateStatus = userBalanceLogMapper.updateToSuccess(balanceLogId, message.getPayId());
        if (updateStatus < 1) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        // 发送消息，赠送优惠券
    }

    /**
     * 添加余额充值赠送的余额、积分、成长值记录
     * @param userBalanceLogDb
     * @param userRechargeVO
     */
    private void addPreLog(UserBalanceLog userBalanceLogDb, UserRechargeVO userRechargeVO) {
        // 添加余额赠送记录
        if (userRechargeVO.getPresAmount() > 0) {
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setUserId(userBalanceLogDb.getUserId());
            userBalanceLog.setChangeBalance(userRechargeVO.getPresAmount());
            userBalanceLog.setIoType(RechargeIoTypeEnum.INCOME.value());
            userBalanceLog.setType(RechargeTypeEnum.PRESENT.value());
            userBalanceLogMapper.save(userBalanceLog);
        }
        // 添加积分赠送记录
        if (userRechargeVO.getPresScore() > 0) {
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userBalanceLogDb.getUserId());
            userScoreLog.setSource(ScoreLogTypeEnum.BALANCE.value());
            userScoreLog.setScore(userRechargeVO.getPresScore());
            userScoreLog.setBizId(userBalanceLogDb.getBalanceLogId());
            userScoreLog.setIoType(ScoreIoTypeEnum.INCOME.value());
            userScoreLogMapper.save(userScoreLog);
        }
        // 添加成长值赠送记录
        if (userRechargeVO.getPresGrowth() > 0) {
            UserGrowthLog userGrowthLog = new UserGrowthLog();
            userGrowthLog.setUserId(userBalanceLogDb.getUserId());
            userGrowthLog.setSource(GrowthLogSourceEnum.BALANCE.value());
            userGrowthLog.setBizId(userBalanceLogDb.getBalanceLogId());
            userGrowthLog.setChangeGrowth(userRechargeVO.getPresGrowth());
            userGrowthLog.setRemarks("余额充值赠送的成长值");
            userGrowthLogMapper.save(userGrowthLog);
        }
    }

    @Override
    public UserBalanceLog getByPayId(Long payId) {
        return userBalanceLogMapper.getByPayId(payId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateToOrderPaySuccess(Long payId, UserBalanceLog userBalanceLog) {
        // 扣减余额，并将余额记录的支付状态变为已支付
        Long userId = userBalanceLog.getUserId();
        int updateBalanceStatus = userExtensionMapper.deductionUserBalance(userBalanceLog.getChangeBalance(), userId);
        if (updateBalanceStatus < 1) {
            throw new LuckException("余额不足");
        }
        int updateLogStatus = userBalanceLogMapper.updateToSuccess(userBalanceLog.getBalanceLogId(), userBalanceLog.getPayId());
        if (updateLogStatus < 1) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doRefund(BalanceRefundBO balanceRefundBO) {
        int refundCount = userBalanceLogMapper.countByRefundId(balanceRefundBO.getRefundId());
        if (refundCount > 0) {
            return;
        }
        // 退款记录
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setUserId(balanceRefundBO.getUserId());
        userBalanceLog.setChangeBalance(balanceRefundBO.getChangeBalance());
        userBalanceLog.setIoType(RechargeIoTypeEnum.INCOME.value());
        userBalanceLog.setIsPayed(0);
        userBalanceLog.setPayId(balanceRefundBO.getPayId());
        userBalanceLog.setRefundId(balanceRefundBO.getRefundId());
        userBalanceLog.setType(RechargeTypeEnum.REFUND.value());
        userBalanceLogMapper.save(userBalanceLog);

        // 余额加钱
        userExtensionMapper.addByUserBalanceLog(userBalanceLog);
    }
}
