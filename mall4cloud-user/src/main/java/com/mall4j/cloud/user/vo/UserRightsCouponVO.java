package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * VO
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserRightsCouponVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("权益优惠券关联id")
    private Long rightsCouponId;

    @ApiModelProperty("权益id")
    private Long rightsId;

    @ApiModelProperty("优惠券id")
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
		return "UserRightsCouponVO{" +
				"rightsCouponId=" + rightsCouponId +
				",createTime=" + createTime +
				",rightsId=" + rightsId +
				",couponId=" + couponId +
				'}';
	}
}
