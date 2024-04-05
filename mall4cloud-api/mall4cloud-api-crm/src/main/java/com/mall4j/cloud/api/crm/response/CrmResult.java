package com.mall4j.cloud.api.crm.response;

import lombok.Data;

/**
 * 类描述：Crm返回响应结果
 *
 */
@Data
public class CrmResult<T> {

	/**
	 * SUCCESS/ERROR
	 */
	private boolean success;

	/**
	 * 失败时为失败原因
	 */
	private String message;

	/**
	 * 失败时为失败原因
	 */
	private String code;


	/**
	 * json格式的返回值
	 */
	private T data;

}
