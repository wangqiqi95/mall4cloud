package com.mall4j.cloud.common.exception;

import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;

/**
 * @author Citrus
 * @date 2021/8/13 13:31
 */
public class SensitiveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Object object;

	private ResponseEnum responseEnum;

	private ServerResponseEntity<?> serverResponseEntity;

	public SensitiveException(String msg) {
		super(msg);
	}

	public SensitiveException(String msg, Object object) {
		super(msg);
		this.object = object;
	}

}
