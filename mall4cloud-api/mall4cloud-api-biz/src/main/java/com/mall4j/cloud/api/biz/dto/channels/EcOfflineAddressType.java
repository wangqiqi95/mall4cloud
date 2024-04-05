package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EcOfflineAddressType {
	
	//1表示同城配送
	@JsonProperty("same_city")
	private Integer sameCity;
	
	//1表示用户自提
	private Integer pickup;
}
