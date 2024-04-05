package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcAddressDetail;
import lombok.Data;

/**
 * 视频号4.0地址管理请求实体
 */
@Data
public class EcAddressRequest {
	//地址详情
	@JsonProperty("address_detail")
	private EcAddressDetail ecAddressDetail;
}
