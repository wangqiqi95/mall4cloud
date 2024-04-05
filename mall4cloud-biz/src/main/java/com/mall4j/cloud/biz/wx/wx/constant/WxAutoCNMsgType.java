package com.mall4j.cloud.biz.wx.wx.constant;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * 微信回复消息类型
 * @author gmq
 * @date 2022/01/18
 */
@Getter
public enum WxAutoCNMsgType {

	/**
	 * 消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)
	 */

	/**
	 * 文本消息
	 */
	text("text","文本"),

	/**
	 * 图文消息
	 */
	news("news","图文"),

	/**
	 * 音频消息
	 */
	voice("voice","音频"),

	/**
	 * 视频消息
	 */
	video("video","视频"),

	/**
	 * 图片消息
	 */
	image("image","图片"),

	/**
	 * 小程序
	 */
	wxma("wxma","小程序"),

	/**
	 * 外部链接
	 */
	link("link","外部链接"),
	;

	private final String value;

	private final String title;

	public String value() {
		return value;
	}

	WxAutoCNMsgType(String value, String title) {
		this.value = value;
		this.title = title;
	}

	public static String value(String codes) {
		if(StringUtils.isEmpty(codes)) return null;
		WxAutoCNMsgType[] enums = values();    //获取所有枚举集合
		String[] codeArry = codes.split(",");    //获取所有枚举集合
		StringBuilder stringBuilder=new StringBuilder();
		for (WxAutoCNMsgType cnMsgType : enums) {
			for (String code : codeArry) {
				if (cnMsgType.getValue().toString().equals(code)) {
					stringBuilder.append(cnMsgType.getTitle()).append(",");
				}
			}
		}
		return stringBuilder.length()>0?stringBuilder.substring(0,stringBuilder.length()-1):null;
	}

}
