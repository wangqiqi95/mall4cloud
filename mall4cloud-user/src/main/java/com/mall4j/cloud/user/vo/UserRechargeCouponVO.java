package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 余额优惠券关联表VO
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public class UserRechargeCouponVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("充值id")
    private Long rechargeId;

    @ApiModelProperty("优惠券id")
    private Long couponId;

    @ApiModelProperty("优惠券数量")
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
		return "UserRechargeCouponVO{" +
				"rechargeId=" + rechargeId +
				",couponId=" + couponId +
				",createTime=" + createTime +
				",couponNum=" + couponNum +
				'}';
	}
}
