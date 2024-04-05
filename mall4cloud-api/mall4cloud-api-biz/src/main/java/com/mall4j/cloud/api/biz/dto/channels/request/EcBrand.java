package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcApplicationDetails;
import com.mall4j.cloud.api.biz.dto.channels.EcGrantDetails;
import com.mall4j.cloud.api.biz.dto.channels.EcRegisterDetails;
import lombok.Data;

/**
 * 视频号4.0品牌实体
 */
@Data
public class EcBrand {
	
	//品牌库中的品牌编号
	@JsonProperty("brand_id")
	private String brandId;
	
	//品牌商标中文名, 从品牌库获取
	@JsonProperty("ch_name")
	private String chName;
	
	//品牌商标英文名, 从品牌库获取
	@JsonProperty("en_name")
	private String enName;
	
	//商标分类号, 取值范围1-45
	@JsonProperty("classification_no")
	private String classificationNo;
	
	//商标类型, 取值1:R标; 2: TM标
	@JsonProperty("trade_mark_symbol")
	private Integer tradeMarkSymbol;
	
	@JsonProperty("register_details")
	private EcRegisterDetails ecRegisterDetails;
	
	@JsonProperty("application_details")
	private EcApplicationDetails ecApplicationDetails;
	
	//商标授权信息, 取值1:自有品牌; 2: 授权品牌
	@JsonProperty("grant_type")
	private Integer grantType;
	
	@JsonProperty("grant_details")
	private EcGrantDetails ecGrantDetails;
	
}
