package com.mall4j.cloud.payment.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 购买vip支付信息
 *
 * @author FrozenWatermelon
 */
public class BuyVipPayInfoDTO extends BasePayInfoDTO {

	@NotNull
	@ApiModelProperty(value = "用户等级id")
	private Long userLevelLogId;

	public Long getUserLevelLogId() {
		return userLevelLogId;
	}

	public void setUserLevelLogId(Long userLevelLogId) {
		this.userLevelLogId = userLevelLogId;
	}

	@Override
	public String toString() {
		return "BuyVipPayInfoDTO{" +
				"userLevelLogId=" + userLevelLogId +
				"} " + super.toString();
	}
}
