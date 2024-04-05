package com.mall4j.cloud.biz.wx.wx.constant;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * 微信回复消息类型
 * @author gmq
 * @date 2022/01/18
 */
@Getter
public enum WxAutoMsgType {

	/**
	 * 消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)
	 */

	/**
	 * 文本消息
	 */
	TEXT("text"),

	/**
	 * 图文消息
	 */
	NEWS("news"),

	/**
	 * 音频消息
	 */
	VOICE("voice"),

	/**
	 * 视频消息
	 */
	VIDEO("video"),

	/**
	 * 图片消息
	 */
	IMAGE("image"),

	/**
	 * 小程序
	 */
	WXMA("wxma"),

	/**
	 * 外部链接
	 */
	EXTERNAL_LINK("link"),

	/**
	 * 菜单时间
	 */
	EVENT("event"),

	/**
	 * 短视频消息
	 */
	SHORT_VIDEO("shortvideo"),

	/**
	 * 地理位置消息
	 */
	LOCATION("location"),

	/**
	 * 扫码
	 */
	EVENT_SCAN("SCAN"),

	/**
	 * 商城功能
	 */
	SHOP_MALL("shopmall"),
;

	private final String value;

	public String value() {
		return value;
	}

	WxAutoMsgType(String value) {
		this.value = value;
	}

	public static String value(String codes) {
		if(StringUtils.isEmpty(codes)) return null;
		WxAutoMsgType[] enums = values();    //获取所有枚举集合
		String[] codeArry = codes.split(",");    //获取所有枚举集合
		StringBuilder stringBuilder=new StringBuilder();
		for (WxAutoMsgType msgType : enums) {
			for (String code : codeArry) {
				if (msgType.getValue().toString().equals(code)) {
					stringBuilder.append(msgType.getValue()).append(",");
				}
			}
		}
		return stringBuilder.length()>0?stringBuilder.substring(0,stringBuilder.length()-1):null;
	}

}
