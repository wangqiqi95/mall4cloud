package com.mall4j.cloud.api.auth.constant;

/**
 * 授权登录类型
 */
public enum AuthLoginType {

	/**
	 * 微信小程序
	 */
	MA(1),

	/**
	 * 微信公众号
	 */
	MP(2),

	/**
	 * 企微小程序
	 */
	QW(3),

	/**
	 * 企微网页授权
	 */
	CPOAUTH2(4),

	;

	private final Integer value;

	public Integer value() {
		return value;
	}

	AuthLoginType(Integer value) {
		this.value = value;
	}

}
