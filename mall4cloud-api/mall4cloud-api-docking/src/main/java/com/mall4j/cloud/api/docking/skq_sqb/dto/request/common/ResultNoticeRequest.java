package com.mall4j.cloud.api.docking.skq_sqb.dto.request.common;

import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByResultNotice;
import lombok.Data;

/**
 * 销售类结果通知公共请求实体
 */
@Data
public class ResultNoticeRequest {
	
	/**
	 * 头部信息
	 */
	private SQBHead head;
	
	
	/**
	 * 业务执行请求体
	 */
	private SQBBodyByResultNotice body;
}
