package com.mall4j.cloud.common.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 优惠券VO
 *
 * @author YXF
 * @date 2020-12-08 17:22:56
 */
public class SpuCouponAppVO {
    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("优惠类型 1:代金券 2:折扣券 3:兑换券")
    private Integer couponType;

    @ApiModelProperty("使用条件")
    private Long cashCondition;

    @ApiModelProperty("减免金额")
    private Long reduceAmount;

    @ApiModelProperty("折扣额度")
    private Double couponDiscount;

    @ApiModelProperty("领券后X天起生效")
    private Integer afterReceiveDays;

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Long getCashCondition() {
		return cashCondition;
	}

	public void setCashCondition(Long cashCondition) {
		this.cashCondition = cashCondition;
	}

	public Long getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(Long reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public Double getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(Double couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public Integer getAfterReceiveDays() {
		return afterReceiveDays;
	}

	public void setAfterReceiveDays(Integer afterReceiveDays) {
		this.afterReceiveDays = afterReceiveDays;
	}

	@Override
	public String toString() {
		return "SpuCouponAppVO{" +
				"couponId=" + couponId +
				", couponType=" + couponType +
				", cashCondition=" + cashCondition +
				", reduceAmount=" + reduceAmount +
				", couponDiscount=" + couponDiscount +
				", afterReceiveDays=" + afterReceiveDays +
				'}';
	}
}
