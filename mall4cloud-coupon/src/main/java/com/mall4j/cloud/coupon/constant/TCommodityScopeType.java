package com.mall4j.cloud.coupon.constant;

/**
 * 优惠券状态
 * @author shijing
 */
public enum TCommodityScopeType {

	/**
	 * 不限
	 */
	NOT_LIMIT(0),

	/**
	 * 指定商品
	 */
	LIMIT(1)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	TCommodityScopeType(Integer value) {
		this.value = value;
	}

}
