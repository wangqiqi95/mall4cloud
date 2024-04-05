package com.mall4j.cloud.api.coupon.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 优惠券用户关联信息VO
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public class CouponUserApiVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券用户ID")
    private Long couponUserId;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("用户ID")
    private Long userId;

    public Long getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(Long couponUserId) {
		this.couponUserId = couponUserId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CouponUserVO{" +
				"couponUserId=" + couponUserId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",couponId=" + couponId +
				",userId=" + userId +
				'}';
	}
}
