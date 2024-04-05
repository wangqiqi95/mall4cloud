package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcAddressDetail;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

/**
 * 视频号4.0获取地址详情响应实体
 */
@Data
public class EcAddressDetailResponse extends EcBaseResponse {
	//地址详情
	@JsonProperty("address_detail")
	private EcAddressDetail ecAddressDetail;
}
