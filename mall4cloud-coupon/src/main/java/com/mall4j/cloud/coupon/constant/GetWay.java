package com.mall4j.cloud.coupon.constant;

/**
 * 优惠券领取类型
 * @author yxf
 * @date 2020/11/20
 */
public enum GetWay {
	/**
	 * 用户领取
	 */
	USER(0),

	/**
	 * 平台发放
	 */
	PLATFORM(1)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	GetWay(Integer value) {
		this.value = value;
	}

}
