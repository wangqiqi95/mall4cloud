package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * DTO
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserLevelTermDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long levelTermId;

    @ApiModelProperty("等级id")
    private Long userLevelId;

    @NotNull(message = "期数类型不能为空")
    @ApiModelProperty("期数类型(0:月,1:季,2:年)")
    private Integer termType;

    @NotNull(message = "价格不能为空")
    @ApiModelProperty("价格")
    private Long needAmount;

	@NotNull(message = "状态不能为空")
	@ApiModelProperty("状态(0: 禁用1: 启用)")
	private Integer status;

	public Long getLevelTermId() {
		return levelTermId;
	}

	public void setLevelTermId(Long levelTermId) {
		this.levelTermId = levelTermId;
	}

	public Long getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(Long userLevelId) {
		this.userLevelId = userLevelId;
	}

	public Integer getTermType() {
		return termType;
	}

	public void setTermType(Integer termType) {
		this.termType = termType;
	}

	public Long getNeedAmount() {
		return needAmount;
	}

	public void setNeedAmount(Long needAmount) {
		this.needAmount = needAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserLevelTermDTO{" +
				"levelTermId=" + levelTermId +
				",userLevelId=" + userLevelId +
				",termType=" + termType +
				",needAmount=" + needAmount +
				",status=" + status +
				'}';
	}
}
