package com.mall4j.cloud.api.openapi.skq_crm.response;

/**
 * @description 提供给crm接口响应code枚举值
 * @date 2021/12/28 14:15：33
 */
public enum CrmResponseEnum {
	/**
	 * ok
	 */
	OK("S200", "成功"),
	/**
	 * 用于直接显示提示用户的错误，内容由输入内容决定
	 */
	SHOW_FAIL("A00001", ""),

	/**
	 * 方法参数没有校验，内容由输入内容决定
	 */
	METHOD_ARGUMENT_NOT_VALID("A00002", ""),

	/**
	 * 无法读取获取请求参数
	 */
	HTTP_MESSAGE_NOT_READABLE("A00003", "请求参数格式有误"),

	/**
	 * 未授权
	 */
	UNAUTHORIZED("A00004", "Unauthorized"),

	/**
	 * 服务器出了点小差
	 */
	EXCEPTION("A00005", "服务器出了点小差"),
	;

	private final String code;

	private final String msg;

	public String value() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	CrmResponseEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "CrmResponseEnum{" + "code='" + code + '\'' + ", msg='" + msg + '\'' + "} " + super.toString();
	}
}
