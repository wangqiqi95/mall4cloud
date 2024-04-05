package com.mall4j.cloud.user.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserRightsCoupon extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 权益优惠券关联id
     */
    private Long rightsCouponId;

    /**
     * 权益id
     */
    private Long rightsId;

    /**
     * 优惠券id
     */
    private Long couponId;

	public Long getRightsCouponId() {
		return rightsCouponId;
	}

	public void setRightsCouponId(Long rightsCouponId) {
		this.rightsCouponId = rightsCouponId;
	}

	public Long getRightsId() {
		return rightsId;
	}

	public void setRightsId(Long rightsId) {
		this.rightsId = rightsId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	@Override
	public String toString() {
		return "UserRightsCoupon{" +
				"rightsCouponId=" + rightsCouponId +
				",createTime=" + createTime +
				",rightsId=" + rightsId +
				",couponId=" + couponId +
				'}';
	}
}
