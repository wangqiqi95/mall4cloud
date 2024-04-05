package com.mall4j.cloud.user.constant;

/**
 * 积分明细状态
 * @author yxf
 * @date 2020/11/20
 */
public enum ScoreGetLogStatusEnum {

	/**
	 * 过期
	 */
	EXPIRED(-1),
	/**
	 * 订单抵现
	 */
	OFFSET_CASH(0),
	/**
	 * 正常
	 */
	NORMAL(1)
	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	ScoreGetLogStatusEnum(Integer value) {
		this.value = value;
	}

}
