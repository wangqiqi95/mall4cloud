package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：优惠券状态同步
 *
 * @date 2022/1/29 11:32：38
 */
public class CouponStateUpdateDto implements Serializable {

	private static final long serialVersionUID = 8359051993523443833L;
	@ApiModelProperty(value = "券id")
	private String coupon_id;

	@ApiModelProperty(value = "券码")
	private String coupon_code;

	@ApiModelProperty(value = "会员手机号")
	private String mobile;

	@ApiModelProperty(value = "操作(release:发放/expired:过期/writeOff核销)")
	private String oper;

	public String getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	@Override
	public String toString() {
		return "CouponStateUpdateDto{" + "coupon_id='" + coupon_id + '\'' + ", coupon_code='" + coupon_code + '\'' + ", mobile='" + mobile + '\'' + ", oper='"
				+ oper + '\'' + '}';
	}
}
