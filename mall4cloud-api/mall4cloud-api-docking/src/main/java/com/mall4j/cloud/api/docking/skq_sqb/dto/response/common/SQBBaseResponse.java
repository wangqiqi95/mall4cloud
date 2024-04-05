package com.mall4j.cloud.api.docking.skq_sqb.dto.response.common;

import lombok.Data;

@Data
public class SQBBaseResponse {

	/**
	 * 本次业务返回结果体
	 */
	private SQBResponse response;
	
	/**
	 * 签名，RSA加密
	 */
	private String signature;
}
