package com.mall4j.cloud.coupon.constant;

/**
 * 优惠券过期状态
 * @author yxf
 * @date 2020/11/20
 */
public enum CouponStatus {
	/**
	 * 删除
	 */
	DELETE(-1),

	/**
	 * 过期
	 */
	OVERDUE(0),

	/**
	 * 未过期
	 */
	NO_OVERDUE(1)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	CouponStatus(Integer value) {
		this.value = value;
	}

}
