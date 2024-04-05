package com.mall4j.cloud.user.controller.app;

import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.model.UserBalanceLog;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.service.UserBalanceLogService;
import com.mall4j.cloud.user.service.UserExtensionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FrozenWatermelon
 * @date 2021/5/11
 */
@RestController
@RequestMapping("/user_balance")
@Api(tags = "app-余额支付")
public class UserBalancePayController {

    @Autowired
    private UserExtensionService userExtensionService;

    @Autowired
    private OnsMQTransactionTemplate balancePayTemplate;

    @Autowired
    private UserBalanceLogService userBalanceLogService;

    @PostMapping("/balance_pay")
	@ApiOperation(value = "支付", notes = "余额支付")
	public ServerResponseEntity<Void> pay(@RequestBody Long payId) {
        Long userId = AuthUserContext.get().getUserId();

        UserExtension userExtension = userExtensionService.getByUserId(userId);

        // 获取用户可用余额
        Long balance = userExtension.getActualBalance();

        // 需要支付的金额
        UserBalanceLog userBalanceLog = userBalanceLogService.getByPayId(payId);
        if (userBalanceLog.getChangeBalance() > balance) {
            return ServerResponseEntity.showFailMsg("余额不足");
        }

        SendResult sendResult = balancePayTemplate.sendMessageInTransaction(payId, userBalanceLog);
        if (sendResult == null || sendResult.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
		return ServerResponseEntity.success();
	}

}
