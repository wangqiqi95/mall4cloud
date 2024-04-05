package com.mall4j.cloud.api.auth.constant;

/**
 * 第三方系统类型
 * @author FrozenWatermelon
 * @date 2021/01/16
 */
public enum SocialType {

	/**
	 * 小程序
	 */
	MA(1),

	/**
	 * 公众号
	 */
	MP(2),

	/**
	 * 企微
	 */
	QW(3),

	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	SocialType(Integer value) {
		this.value = value;
	}

}
