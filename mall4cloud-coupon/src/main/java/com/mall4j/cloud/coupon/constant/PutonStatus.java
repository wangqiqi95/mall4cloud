package com.mall4j.cloud.coupon.constant;

import java.util.Objects;

/**
 * 优惠券投放状态
 * @author yxf
 * @date 2020/11/20
 */
public enum PutonStatus {
	/**
	 * 取消投放
	 */
	CANCEL(-1),

	/**
	 * 等待投放
	 */
	WAIT_PUT_ON(0),

	/**
	 * 投放
	 */
	PUT_ON(1),

	/**
	 * 违规下架
	 */
	OFFLINE(2),

	/**
	 * 等待审核
	 */
	WAIT_AUDIT(3)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	PutonStatus(Integer value) {
		this.value = value;
	}

	public static Boolean offline (Integer value) {
		if (Objects.equals(OFFLINE.value, value) || Objects.equals(WAIT_AUDIT.value, value) ) {
			return true;
		}
		return false;
	}

}
