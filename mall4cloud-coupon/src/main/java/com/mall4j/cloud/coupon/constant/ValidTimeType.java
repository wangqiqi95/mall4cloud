package com.mall4j.cloud.coupon.constant;

/**
 * 优惠券生效时间
 * @author yxf
 * @date 2020/11/20
 */
public enum ValidTimeType {
	/**
	 * 固定时间
	 */
	FIXED(1),

	/**
	 * 领取后生效
	 */
	RECEIVE(2)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	ValidTimeType(Integer value) {
		this.value = value;
	}

}
