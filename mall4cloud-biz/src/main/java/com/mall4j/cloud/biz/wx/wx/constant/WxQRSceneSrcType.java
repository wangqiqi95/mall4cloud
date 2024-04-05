package com.mall4j.cloud.biz.wx.wx.constant;

/**
 * 微信回复消息类型
 * @author gmq
 * @date 2022/01/18
 */
public enum WxQRSceneSrcType {

	/**
	 * 扫码消息自动回复
	 */
	scan_auto_msg(1),

	/**
	 * 扫码关注
	 */
	scan_subscribe(2),
;

	private final Integer value;

	public Integer value() {
		return value;
	}

	WxQRSceneSrcType(Integer value) {
		this.value = value;
	}

}
