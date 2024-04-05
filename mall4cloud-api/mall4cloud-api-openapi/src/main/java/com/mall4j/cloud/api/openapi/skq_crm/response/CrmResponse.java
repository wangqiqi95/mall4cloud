package com.mall4j.cloud.api.openapi.skq_crm.response;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * @description 给crm接口统一的返回数据
 * @date 2021/12/28 14:15：33
 */
public class CrmResponse<T> {

	private static final long serialVersionUID = 913504308218709225L;
	@ApiModelProperty("响应码")
	private String code;

	@ApiModelProperty("响应描述")
	private String msg;

	@ApiModelProperty("响应数据")
	private T result;

	private static final String succCode = CrmResponseEnum.OK.value();
	private static final String errorCode = CrmResponseEnum.EXCEPTION.value();

	private static final String succMessage = "SUCCESS";

	public CrmResponse() {
	}

	public CrmResponse(String code, String message) {
		this.code = code;
		this.msg = message;
	}

	public CrmResponse(String code, String message, T result) {
		this.code = code;
		this.msg = message;
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public static <T> CrmResponse<T> success() {
		return new CrmResponse<>(succCode, succMessage);
	}

	public static <T> CrmResponse<T> success(T result) {
		return new CrmResponse<>(succCode, succMessage, result);
	}

	public static <T> CrmResponse<T> fail() {
		return new CrmResponse<>(errorCode, "服务异常,请联系管理员");
	}

	public static <T> CrmResponse<T> fail(String errorMsg) {
		return new CrmResponse<>(errorCode, errorMsg);
	}

	public static <T> CrmResponse<T> fail(String code, String errorMsg) {
		return new CrmResponse<>(code, errorMsg);
	}

	public static <T> CrmResponse<T> fail(CrmResponseEnum responseEnum) {
		return new CrmResponse<>(responseEnum.value(), responseEnum.getMsg());
	}

	public static <T> CrmResponse<T> fail(String errorMsg, T result) {
		return new CrmResponse<>(errorCode, errorMsg, result);
	}

	public boolean isSuccess() {
		return Objects.equals(CrmResponseEnum.OK.value(), this.code);
	}

	public boolean isFail() {
		return !Objects.equals(CrmResponseEnum.OK.value(), this.code);
	}

	@Override public String toString() {
		return "CrmResponse{" + "code='" + code + '\'' + ", message='" + msg + '\'' + ", result=" + result + '}';
	}
}
