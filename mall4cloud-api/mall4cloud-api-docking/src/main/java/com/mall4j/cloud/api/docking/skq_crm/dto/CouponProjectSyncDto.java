package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：微信优惠券项目Id同步至CRM接口请求参数
 *
 * @date 2022/1/26 8:14：12
 */
public class CouponProjectSyncDto implements Serializable {

	private static final long serialVersionUID = -507994142050702631L;
	@ApiModelProperty(value = "CRM优惠券项目id", required = true)
	private String coupon_rule_id;

	@ApiModelProperty(value = "微信优惠券项目id", required = true)
	private String wechat_coupon_rule_id;

	@ApiModelProperty(value = "操作,add/update", required = true)
	private String oper;

	public String getCoupon_rule_id() {
		return coupon_rule_id;
	}

	public void setCoupon_rule_id(String coupon_rule_id) {
		this.coupon_rule_id = coupon_rule_id;
	}

	public String getWechat_coupon_rule_id() {
		return wechat_coupon_rule_id;
	}

	public void setWechat_coupon_rule_id(String wechat_coupon_rule_id) {
		this.wechat_coupon_rule_id = wechat_coupon_rule_id;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	@Override
	public String toString() {
		return "CouponProjectSyncDto{" + "coupon_rule_id='" + coupon_rule_id + '\'' + ", wechat_coupon_rule_id='" + wechat_coupon_rule_id + '\'' + ", oper='"
				+ oper + '\'' + '}';
	}
}
