package com.mall4j.cloud.user.constant;

/**
 * 系统权益
 *
 * @author YXF
 * @date 2020-12-23 10:16:53
 */
public enum SystemUserRightsEnum {
	/** 打折 */
	DISCOUNT(1L, 100),
	/** 包邮 */
	FREE_FEE(2L, null),
	/** 送积分 */
	SCORE(3L, 0),
	/** 送优惠券 */
	COUPON(4L, null),
	/** 积分倍率 */
	RATE_SCORE(5L, 1),
	/** 折扣范围 */
	DISCOUNT_RANGE(6L,null)
	;

	private final Long id;
	private final Integer defaultVallue;

	public Long value() {
		return id;
	}

	public Integer getDefaultVallue() {
		return defaultVallue;
	}

	SystemUserRightsEnum(Long id, Integer defaultVallue){
		this.id = id;
		this.defaultVallue = defaultVallue;
	}
}
