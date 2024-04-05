package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户余额充值记录
 *
 * @author FrozenWatermelon
 */
public class UserBalanceRechargeOrderDTO {

	@ApiModelProperty(value = "充值金额id")
	private Long rechargeId;
	@ApiModelProperty(value = "充值金额")
	private BigDecimal amount;

	public Long getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(Long rechargeId) {
		this.rechargeId = rechargeId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "userBalanceLogDTO{" +
				"rechargeId=" + rechargeId +
				", amount=" + amount +
				'}';
	}
}
