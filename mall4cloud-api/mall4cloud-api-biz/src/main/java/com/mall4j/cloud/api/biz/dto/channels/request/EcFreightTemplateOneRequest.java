package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EcFreightTemplateOneRequest {
	
	//运费模板id
	@JsonProperty("template_id")
	private String templateId;
}
