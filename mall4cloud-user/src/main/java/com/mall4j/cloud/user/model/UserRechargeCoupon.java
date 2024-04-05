package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 余额优惠券关联表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public class UserRechargeCoupon extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 充值id
     */
    private Long rechargeId;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券数量
     */
    private Integer couponNum;

	public Long getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(Long rechargeId) {
		this.rechargeId = rechargeId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Integer getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}

	@Override
	public String toString() {
		return "UserRechargeCoupon{" +
				"rechargeId=" + rechargeId +
				",couponId=" + couponId +
				",createTime=" + createTime +
				",couponNum=" + couponNum +
				'}';
	}
}
