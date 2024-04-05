package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0品牌库基础数据实体
 */
@Data
public class EcBasicBrand {
	
	//品牌库中的品牌编号
	@JsonProperty("brand_id")
	private String brandId;
	
	//品牌商标中文名
	@JsonProperty("ch_name")
	private String chName;
	
	//品牌商标中文名
	@JsonProperty("en_name")
	private String enName;
}
