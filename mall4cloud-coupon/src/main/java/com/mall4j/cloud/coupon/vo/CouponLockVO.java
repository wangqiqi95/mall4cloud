package com.mall4j.cloud.coupon.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 优惠券使用记录VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-28 10:04:50
 */
public class CouponLockVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券使用记录id")
    private Long couponUseRecordId;

    @ApiModelProperty("优惠券id")
    private Long couponId;

    @ApiModelProperty("用户优惠券id")
    private Long couponUserId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("订单号")
    private String orderIds;

    @ApiModelProperty("优惠金额")
    private Long amount;

    @ApiModelProperty("使用状态(状态-1已解锁 0待确定 1已锁定)")
    private Integer status;

	public Long getCouponUseRecordId() {
		return couponUseRecordId;
	}

	public void setCouponUseRecordId(Long couponUseRecordId) {
		this.couponUseRecordId = couponUseRecordId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(Long couponUserId) {
		this.couponUserId = couponUserId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CouponLockVO{" +
				"couponUseRecordId=" + couponUseRecordId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",couponId=" + couponId +
				",couponUserId=" + couponUserId +
				",userId=" + userId +
				",orderIds=" + orderIds +
				",amount=" + amount +
				",status=" + status +
				'}';
	}
}
