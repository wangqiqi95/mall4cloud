package com.mall4j.cloud.biz.wx.wx.constant;

/**
 * 微信回复消息类型
 * @author gmq
 * @date 2022/01/18
 */
public enum WxMPAssModeType {


	/**
	 * 开放平台接入
	 */
	WX_OPEN(1),


;

	private final Integer value;

	public Integer value() {
		return value;
	}

	WxMPAssModeType(Integer value) {
		this.value = value;
	}

}
