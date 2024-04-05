package com.mall4j.cloud.api.coupon.constant;

/**
 * 类描述：优惠券来源类型
 *
 * @date 2022/1/26 16:04：11
 */
public enum CouponSourceType {

	SELF_ADD(1, "小程序添加"),
	CRM_SYNC(2, "CRM同步优惠券");

	private Integer type;

	private String desc;

	CouponSourceType(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
