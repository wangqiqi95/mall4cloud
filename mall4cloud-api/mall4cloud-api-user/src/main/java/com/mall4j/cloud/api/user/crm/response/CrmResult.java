package com.mall4j.cloud.api.user.crm.response;

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
	private String msg;


	/**
	 * json格式的返回值
	 */
	private T result;

}
