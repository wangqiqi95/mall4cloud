package com.mall4j.cloud.api.docking.skq_crm.resp;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：优惠券发放（小程序—>CRM）响应
 *
 * @date 2022/1/26 9:14：26
 */
public class CouponReleaseResp implements Serializable {

	private static final long serialVersionUID = 8739146331550052602L;
	@ApiModelProperty(value = "优惠券码")
	private String coupon_code;

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	@Override
	public String toString() {
		return "CouponReleaseResp{" + "coupon_code='" + coupon_code + '\'' + '}';
	}
}
