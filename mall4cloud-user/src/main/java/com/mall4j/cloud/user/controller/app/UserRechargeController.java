package com.mall4j.cloud.user.controller.app;

import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.user.constant.RechargeIoTypeEnum;
import com.mall4j.cloud.user.constant.RechargeTypeEnum;
import com.mall4j.cloud.user.dto.UserBalanceRechargeOrderDTO;
import com.mall4j.cloud.user.model.UserBalanceLog;
import com.mall4j.cloud.user.model.UserExtension;
import com.mall4j.cloud.user.service.UserBalanceLogService;
import com.mall4j.cloud.user.service.UserExtensionService;
import com.mall4j.cloud.user.service.UserRechargeService;
import com.mall4j.cloud.user.vo.UserRechargeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 余额充值级别表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
@RestController("appUserRechargeController")
@RequestMapping("/user_recharge")
@Api(tags = "app-余额充值")
public class UserRechargeController {

    @Autowired
    private UserRechargeService userRechargeService;

    @Autowired
    private UserExtensionService userExtensionService;
	@Autowired
	private UserBalanceLogService userBalanceLogService;

	@GetMapping("/list")
	@ApiOperation(value = "获取余额充值级别表列表", notes = "分页获取余额充值级别表列表")
	public ServerResponseEntity<List<UserRechargeVO>> list() {
		return ServerResponseEntity.success(userRechargeService.list());
	}

	@PostMapping("/recharge")
	@ApiOperation(value = "充值订单", notes = "生成余额充值订单，保存充值记录")
	public ServerResponseEntity<Long> save(@Valid @RequestBody UserBalanceRechargeOrderDTO userBalanceRechargeOrderDTO) {
		Long amount = PriceUtil.toLongCent(userBalanceRechargeOrderDTO.getAmount());
		Long rechargeId = userBalanceRechargeOrderDTO.getRechargeId();
		if (rechargeId != null) {
			UserRechargeVO userRecharge = userRechargeService.getRechargeInfo(rechargeId);
			amount = userRecharge.getRechargeAmount();
		}
		if (amount == null || amount < 1) {
			throw new LuckException("充值金额有误");
		}
		UserExtension userExtension = userExtensionService.getByUserId(AuthUserContext.get().getUserId());
		long userBalance = userExtension.getActualBalance() + amount;
		if (userBalance > Constant.MAX_USER_BALANCE) {
			throw new LuckException("您的余额加充值的余额将大于最大余额，不能进行充值操作");
		}
		UserBalanceLog userBalanceLog = new UserBalanceLog();
		userBalanceLog.setUserId(AuthUserContext.get().getUserId());
		userBalanceLog.setChangeBalance(amount);
		userBalanceLog.setIoType(RechargeIoTypeEnum.INCOME.value());
		userBalanceLog.setIsPayed(0);
		userBalanceLog.setRechargeId(userBalanceRechargeOrderDTO.getRechargeId());
		userBalanceLog.setType(RechargeTypeEnum.RECHARGE.value());
		userBalanceLogService.save(userBalanceLog);
		return ServerResponseEntity.success(userBalanceLog.getBalanceLogId());
	}
}
