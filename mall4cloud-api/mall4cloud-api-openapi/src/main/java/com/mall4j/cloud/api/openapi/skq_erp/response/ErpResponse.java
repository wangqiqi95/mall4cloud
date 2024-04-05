package com.mall4j.cloud.api.openapi.skq_erp.response;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @description 给crm接口统一的返回数据
 * @date 2021/12/28 14:15：33
 */
public class ErpResponse<T> {

	private static final long serialVersionUID = 913504308218709225L;
	@ApiModelProperty("响应码")
	private String code;

	@ApiModelProperty("响应描述")
	private String message;

	@ApiModelProperty("响应数据")
	private T result;

	private static final String succCode = ErpResponseEnum.OK.value();
	private static final String errorCode = ErpResponseEnum.EXCEPTION.value();

	private static final String succMessage = "SUCCESS";

	public ErpResponse() {
	}

	public ErpResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ErpResponse(String code, String message, T result) {
		this.code = code;
		this.message = message;
		this.result = result;
	}

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

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public static <T> ErpResponse<T> success() {
		return new ErpResponse<>(succCode, succMessage);
	}

	public static <T> ErpResponse<T> success(T result) {
		return new ErpResponse<>(succCode, succMessage, result);
	}

	public static <T> ErpResponse<T> fail() {
		return new ErpResponse<>(errorCode, "服务异常,请联系管理员");
	}

	public static <T> ErpResponse<T> fail(String errorMsg) {
		return new ErpResponse<>(errorCode, errorMsg);
	}

	public static <T> ErpResponse<T> fail(String code, String errorMsg) {
		return new ErpResponse<>(code, errorMsg);
	}

	public static <T> ErpResponse<T> paramError(String errorMsg) {
		return new ErpResponse<>(ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(),
				StringUtils.isNotBlank(errorMsg) ? errorMsg : ErpResponseEnum.HTTP_MESSAGE_NOT_READABLE.getMsg());
	}

	public static <T> ErpResponse<T> fail(ErpResponseEnum responseEnum) {
		return new ErpResponse<>(responseEnum.value(), responseEnum.getMsg());
	}

	public static <T> ErpResponse<T> fail(String errorMsg, T result) {
		return new ErpResponse<>(errorCode, errorMsg, result);
	}

	public boolean isSuccess() {
		return Objects.equals(ErpResponseEnum.OK.value(), this.code);
	}

	public boolean isFail() {
		return !Objects.equals(ErpResponseEnum.OK.value(), this.code);
	}

	@Override public String toString() {
		return "CrmResponse{" + "code='" + code + '\'' + ", message='" + message + '\'' + ", result=" + result + '}';
	}
}
