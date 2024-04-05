package com.mall4j.cloud.common.exception;

import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;

/**
 * @author FrozenWatermelon
 * @date 2020/7/11
 */
public class LuckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Object object;

	private ResponseEnum responseEnum;

	private ServerResponseEntity<?> serverResponseEntity;

	public LuckException(String msg) {
		super(msg);
	}

	public LuckException(String msg, Object object) {
		super(msg);
		this.object = object;
	}

	public LuckException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public LuckException(ResponseEnum responseEnum) {
		super(responseEnum.getMsg());
		this.responseEnum = responseEnum;
	}

	public LuckException(ResponseEnum responseEnum,Object object) {
		super(responseEnum.getMsg());
		this.responseEnum = responseEnum;
		this.object = object;
	}

	public LuckException(ServerResponseEntity<?> serverResponseEntity) {
		this.serverResponseEntity = serverResponseEntity;
	}

	public ServerResponseEntity<?> getServerResponseEntity() {
		return serverResponseEntity;
	}

	public Object getObject() {
		return object;
	}

	public ResponseEnum getResponseEnum() {
		return responseEnum;
	}

	public LuckException(ResponseEnum responseEnum,String msg) {
		super(msg);
		this.responseEnum = responseEnum;
	}
}
