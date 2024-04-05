package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0查询品牌资质审核请求实体
 */
@Data
public class EcBrandOneRequest {
	
	//品牌库中的品牌编号
	@JsonProperty("brand_id")
	private String brandId;
}
