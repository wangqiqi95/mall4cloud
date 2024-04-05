package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * VO
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserLevelTermVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long levelTermId;

    @ApiModelProperty("等级id")
    private Long userLevelId;

    @ApiModelProperty("期数类型(0:月,1:季,2:年)")
    private Integer termType;

    @ApiModelProperty("价格")
    private Long needAmount;

	@ApiModelProperty("状态(0: 禁用1: 启用)")
	private Integer status;

	@ApiModelProperty("原价(按月份算)，分")
	private Integer originalPrice;

	@ApiModelProperty("折扣")
	private Double discount;

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

	public Integer getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Integer originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "UserLevelTermVO{" +
				"levelTermId=" + levelTermId +
				",createTime=" + createTime +
				",userLevelId=" + userLevelId +
				",termType=" + termType +
				",needAmount=" + needAmount +
				",status=" + status +
				",originalPrice=" + originalPrice +
				",discount=" + discount +
				'}';
	}
}
