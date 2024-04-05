package com.mall4j.cloud.user.vo.openapi;

import java.util.Objects;

/**
 * 类描述：返回中台响应内容
 *
 * @date 2022/1/6 22:57：05
 */
public class ApiResponse<T> {

	private Integer code;

	private Boolean isSuccess;

	private String errorMsg;

	private T data;


	private static final Integer succCode = 0;
	private static final Integer errorCode = 500;

	public ApiResponse() {
	}

	public ApiResponse(Integer code, Boolean isSuccess) {
		this.code = code;
		this.isSuccess = isSuccess;
	}

	public ApiResponse(Integer code, Boolean isSuccess, T data) {
		this.code = code;
		this.isSuccess = isSuccess;
		this.data = data;
	}

	public ApiResponse(Integer code, Boolean isSuccess, String errorMsg) {
		this.code = code;
		this.isSuccess = isSuccess;
		this.errorMsg = errorMsg;
	}

	public static ApiResponse success() {
		return new ApiResponse(succCode, true);
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse(succCode, true,data);
	}

	public static ApiResponse fail(String errorMsg) {
		return new ApiResponse(errorCode, false, errorMsg);
	}

	public boolean successful() {
		return Objects.equals(succCode, this.code);
	}

	public boolean fail() {
		return Objects.equals(errorCode, this.code);
	}



	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean success) {
		isSuccess = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getSuccess() {
		return isSuccess;
	}

	public void setSuccess(Boolean success) {
		isSuccess = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "StdResult{" + "code=" + code + ", isSuccess=" + isSuccess + ", errorMsg='" + errorMsg + '\'' + '}';
	}
}
