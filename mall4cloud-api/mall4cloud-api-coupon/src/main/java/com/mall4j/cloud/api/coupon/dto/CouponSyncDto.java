package com.mall4j.cloud.api.coupon.dto;

import java.io.Serializable;

public class CouponSyncDto implements Serializable {

	private static final long serialVersionUID = -3856643033978529608L;
	private Long wechat_coupon_rule_id;

	private String crm_coupon_rule_id;

	public CouponSyncDto() {
	}

	public CouponSyncDto(Long wechat_coupon_rule_id, String crm_coupon_rule_id) {
		this.wechat_coupon_rule_id = wechat_coupon_rule_id;
		this.crm_coupon_rule_id = crm_coupon_rule_id;
	}

	public Long getWechat_coupon_rule_id() {
		return wechat_coupon_rule_id;
	}

	public void setWechat_coupon_rule_id(Long wechat_coupon_rule_id) {
		this.wechat_coupon_rule_id = wechat_coupon_rule_id;
	}

	public String getCrm_coupon_rule_id() {
		return crm_coupon_rule_id;
	}

	public void setCrm_coupon_rule_id(String crm_coupon_rule_id) {
		this.crm_coupon_rule_id = crm_coupon_rule_id;
	}

	@Override public String toString() {
		return "CouponSyncDto{" + "wechat_coupon_rule_id=" + wechat_coupon_rule_id + ", crm_coupon_rule_id='" + crm_coupon_rule_id + '\'' + '}';
	}
}
