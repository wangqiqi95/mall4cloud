package com.mall4j.cloud.api.docking.skq_crm.dto;

/**
 * 类描述：Crm返回响应结果
 *
 * @date 2022/1/22 17:30：48
 */
public class CrmResult<T> {

	/**
	 * SUCCESS/ERROR
	 */
	private String code;

	/**
	 * 返回信息
	 */
	private String message;

	/**
	 * json格式的返回值
	 */
	private T jsondata;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getJsondata() {
		return jsondata;
	}

	public void setJsondata(T jsondata) {
		this.jsondata = jsondata;
	}

	public boolean success() {
		return "SUCCESS".equals(this.code);
	}

	@Override
	public String toString() {
		return "CrmResult{" + "code='" + code + '\'' + ", message='" + message + '\'' + ", jsondata=" + jsondata + '}';
	}
}
