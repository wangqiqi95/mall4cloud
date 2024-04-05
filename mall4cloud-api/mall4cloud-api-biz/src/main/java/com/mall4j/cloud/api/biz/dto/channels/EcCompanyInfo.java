package com.mall4j.cloud.api.biz.dto.channels;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0 快递公司实体
 */
@Data
public class EcCompanyInfo {

	//快递公司编码
	@JsonProperty("delivery_id")
	private String deliveryId;
	
	//快递公司名称
	@JsonProperty("delivery_name")
	private String deliveryName;
}
