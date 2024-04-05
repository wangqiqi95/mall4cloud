package com.mall4j.cloud.api.docking.skq_sqb.dto.response.common;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class SQBBizResponse {
	
	/**
	 * 业务执行响应码
	 */
	private String result_code;
	
	/**
	 * 业务执行结果返回码
	 */
	private String error_code;
	
	/**
	 * 业务执行错误信息
	 */
	private String error_message;
	
	/**
	 * 业务执行返回参数
	 */
	private JSONObject data;

}
