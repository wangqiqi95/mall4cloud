package com.mall4j.cloud.api.docking.skq_sqb.dto.response.common;

import lombok.Data;

@Data
public class SQBResponseBody {
	
	/**
	 * 通讯响应码	200，400，500	200：通讯成功，回调是否重试，根据这个状态判断；400：客户端错误；500:服务端错误
	 */
	private String result_code;
	
	/**
	 * 通讯错误码	见通信错误码表	通讯 失败 的时候才返回
	 */
	private String error_code;
	
	/**
	 * 通讯错误信息描述	见 通信错误码表	通讯 失败 的时候才返回
	 */
	private String error_message;
	
	/**
	 * 业务响应数据	JSON结构	通讯 成功 的时候才返回
	 */
	private SQBBizResponse biz_response;
}
