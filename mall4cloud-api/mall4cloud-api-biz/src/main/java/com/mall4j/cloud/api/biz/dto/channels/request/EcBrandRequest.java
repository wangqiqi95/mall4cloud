package com.mall4j.cloud.api.biz.dto.channels.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0新增品牌请求实体
 */
@Data
public class EcBrandRequest {
	
	@JsonProperty("brand")
	private EcBrand ecBrand;
}
