package com.mall4j.cloud.coupon.constant;

/**
 * 用户优惠券状态
 * @author shijing
 */
public enum MyCouponStatus {

	FREEZE(0,"冻结"),

	EFFECTIVE(1,"生效"),

	FAILURE(2,"核销")
	;


	private final Integer value;

	private final String desc;

	public Integer value() {
		return value;
	}

	public String desc() {
		return desc;
	}

	MyCouponStatus(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public static MyCouponStatus instance(Integer value) {
		MyCouponStatus[] enums = values();
		for (MyCouponStatus statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
