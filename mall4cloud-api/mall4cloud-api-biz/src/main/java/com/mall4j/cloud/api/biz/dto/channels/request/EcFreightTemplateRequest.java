package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcFreightTemplate;
import lombok.Data;

/**
 * 视频号4.0运费模板请求实体
 */
@Data
public class EcFreightTemplateRequest {
	
	//运费模板
	@JsonProperty("freight_template")
	private EcFreightTemplate ecFreightTemplate;
}
