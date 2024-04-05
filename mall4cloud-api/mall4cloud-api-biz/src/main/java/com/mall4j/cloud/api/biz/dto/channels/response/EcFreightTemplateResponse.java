package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * 视频号4.0运费模版响应实体
 */
@Data
public class EcFreightTemplateResponse extends EcBaseResponse {

	//运费模板id
	@JsonProperty("template_id")
	private String templateId;
}
