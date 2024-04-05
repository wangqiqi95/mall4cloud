package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：优惠券冻结解冻请求参数
 *
 * @date 2022/1/26 12:48：14
 */
public class CouponFreezeThawDto implements Serializable {
	private static final long serialVersionUID = 198335494452551459L;
	@ApiModelProperty(value = "优惠券码", required = true)
	private String coupon_code;

	@ApiModelProperty(value = "操作类型，0：冻结，1：解冻", required = true)
	private String type;

	@ApiModelProperty(value = "来源，默认传“小程序", required = true)
	private String source = "小程序";

	@ApiModelProperty(value = "冻结时返回，解冻时请求")
	private String token;

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "CouponFreezeThawDto{" + "coupon_code='" + coupon_code + '\'' + ", type='" + type + '\'' + ", source='" + source + '\'' + ", token='" + token
				+ '\'' + '}';
	}
}
