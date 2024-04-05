package com.mall4j.cloud.biz.wx.wx.constant;

/**
 * 微信菜单类型
 * @author gmq
 * @date 2022/01/18
 */
public enum WxMpMenuType {

	/**
	 * 网页类型
	 */
	VIEW("view"),

	/**
	 * 点击类
	 */
	CLICK("click"),

	/**
	 * 小程序
	 */
	MINIPROGRAM("miniprogram")
;

	private final String value;

	public String value() {
		return value;
	}

	WxMpMenuType(String value) {
		this.value = value;
	}

}
