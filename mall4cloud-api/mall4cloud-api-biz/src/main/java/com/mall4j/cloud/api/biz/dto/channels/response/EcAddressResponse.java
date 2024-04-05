package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0地址管理响应实体
 */
@Data
public class EcAddressResponse {

	//地址id
	@JsonProperty("address_id")
	private String addressId;
}
