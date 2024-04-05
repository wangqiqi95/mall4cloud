package com.mall4j.cloud.api.auth.constant;

/**
 * 注册校验方式
 * @author FrozenWatermelon
 * @date 2021/01/19
 */
public enum VerifyType {

	/**
	 * 手机号
	 */
	MOBILE(1),

	/**
	 * 邮箱
	 */
	EMAIL(2),

	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	VerifyType(Integer value) {
		this.value = value;
	}

}
