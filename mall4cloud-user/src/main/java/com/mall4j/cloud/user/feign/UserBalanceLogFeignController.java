package com.mall4j.cloud.user.feign;

import com.mall4j.cloud.api.user.bo.BalancePayBO;
import com.mall4j.cloud.api.user.bo.BalanceRefundBO;
import com.mall4j.cloud.api.user.feign.UserBalanceLogClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.constant.RechargeIoTypeEnum;
import com.mall4j.cloud.user.constant.RechargeTypeEnum;
import com.mall4j.cloud.user.model.UserBalanceLog;
import com.mall4j.cloud.user.service.UserBalanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 充值信息
 * @author FrozenWatermelon
 */
@RestController
public class UserBalanceLogFeignController implements UserBalanceLogClient {

    @Autowired
    private UserBalanceLogService userBalanceLogService;

    @Override
    public ServerResponseEntity<Long> getPayAmount(Long rechargeLogId) {
        UserBalanceLog userBalanceLog = userBalanceLogService.getByBalanceLogId(rechargeLogId);
        return ServerResponseEntity.success(userBalanceLog.getChangeBalance());
    }

    @Override
    public ServerResponseEntity<Integer> getIsPay(Long rechargeLogId) {
        UserBalanceLog userBalanceLog = userBalanceLogService.getByBalanceLogId(rechargeLogId);
        return ServerResponseEntity.success(userBalanceLog.getIsPayed());
    }

    @Override
    public ServerResponseEntity<Void> insertBalancePayLog(BalancePayBO balancePayBO) {
        Long userId = AuthUserContext.get().getUserId();
        UserBalanceLog userRechargeLog = new UserBalanceLog();
        userRechargeLog.setUserId(userId);
        userRechargeLog.setChangeBalance(balancePayBO.getChangeBalance());
        userRechargeLog.setIoType(RechargeIoTypeEnum.EXPENDITURE.value());
        if (Objects.nonNull(balancePayBO.getIsVip()) && Objects.equals(balancePayBO.getIsVip(),true)){
            userRechargeLog.setType(RechargeTypeEnum.VIP.value());
        } else {
            userRechargeLog.setType(RechargeTypeEnum.PAY.value());
        }
        userRechargeLog.setIsPayed(0);
        userRechargeLog.setPayId(balancePayBO.getPayId());
        userBalanceLogService.save(userRechargeLog);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> doRefund(BalanceRefundBO balanceRefundBO) {
        userBalanceLogService.doRefund(balanceRefundBO);
        return ServerResponseEntity.success();
    }

}
