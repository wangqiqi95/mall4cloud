package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcFreightTemplate;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * 视频号4.0查询运费模版
 */
@Data
public class EcFreightTemplateOneResponse extends EcBaseResponse {
	//运费模板
	@JsonProperty("freight_template")
	private EcFreightTemplate ecFreightTemplate;
}
