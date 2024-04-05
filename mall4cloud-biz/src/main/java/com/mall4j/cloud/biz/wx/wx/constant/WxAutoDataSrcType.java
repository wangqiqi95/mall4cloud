package com.mall4j.cloud.biz.wx.wx.constant;

/**
 * 微信消息类型
 * @author gmq
 * @date 2021/01/20
 */
public enum WxAutoDataSrcType {

	/**
	 * 内容来源： 1消息自动回复 2关键词回复 3扫码回复 4关注默认回复 5关注门店回复 6自定义菜单
	 */

	/**
	 * 消息自动回复
	 */
	AUTO_MSG(1),

	/**
	 * 关键词回复
	 */
	KEY_WORD(2),

	/**
	 * 扫码回复
	 */
	QR_SCAN(3),

	/**
	 * 关注默认回复
	 */
	SUBSCRIBE(4),

	/**
	 * 关注门店回复
	 */
	SUBSCRIBE_STORE(5),

	/**
	 * 自定义菜单
	 */
	MENU(6),
;

	private final Integer value;

	public Integer value() {
		return value;
	}

	WxAutoDataSrcType(Integer value) {
		this.value = value;
	}

}
