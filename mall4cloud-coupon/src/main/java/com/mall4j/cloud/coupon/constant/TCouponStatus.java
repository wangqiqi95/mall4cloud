package com.mall4j.cloud.coupon.constant;

/**
 * 优惠券状态
 * @author shijing
 */
public enum TCouponStatus {

	NO_OVERDUE(-1,"未生效"),

	OVERDUE(0,"已生效"),

	/**
	 * 已失效
	 */
	DEL(1,"已失效")
	;


	private final Integer value;

	private final String desc;

	public Integer value() {
		return value;
	}

	public String desc() {
		return desc;
	}

	TCouponStatus(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public static TCouponStatus instance(Integer value) {
		TCouponStatus[] enums = values();
		for (TCouponStatus statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
