package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.AddressInfo;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0不发货区域实体
 */
@Data
public class EcNotSendArea {
	
	//不支持的地址列表
	@JsonProperty("address_infos")
	private List<AddressInfo> addressInfos;
}
