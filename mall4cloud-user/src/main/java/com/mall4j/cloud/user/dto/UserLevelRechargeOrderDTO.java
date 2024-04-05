package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户会员购买
 *
 * @author FrozenWatermelon
 */
public class UserLevelRechargeOrderDTO {

	@ApiModelProperty(value = "会员等级id")
	private Long userLevelId;

	@ApiModelProperty(value = "期数id（月/季/年对应的付费金额id）")
	private Long levelTermId;

	public Long getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(Long userLevelId) {
		this.userLevelId = userLevelId;
	}

	public Long getLevelTermId() {
		return levelTermId;
	}

	public void setLevelTermId(Long levelTermId) {
		this.levelTermId = levelTermId;
	}

	@Override
	public String toString() {
		return "UserLevelRechargeOrderDTO{" +
				"userLevelId=" + userLevelId +
				", levelTermId=" + levelTermId +
				'}';
	}
}
