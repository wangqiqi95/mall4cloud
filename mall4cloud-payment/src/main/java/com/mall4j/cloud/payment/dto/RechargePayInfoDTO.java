package com.mall4j.cloud.payment.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 充值金额id
 *
 * @author FrozenWatermelon
 */
public class RechargePayInfoDTO extends BasePayInfoDTO {

	@NotNull
	@ApiModelProperty(value = "充值金额记录id")
	private Long balanceLogId;

	public Long getBalanceLogId() {
		return balanceLogId;
	}

	public void setBalanceLogId(Long balanceLogId) {
		this.balanceLogId = balanceLogId;
	}

	@Override
	public String toString() {
		return "RechargePayInfoDTO{" +
				"balanceLogId=" + balanceLogId +
				"} " + super.toString();
	}
}
