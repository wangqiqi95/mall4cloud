package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0优惠券推广信息实体
 */
@Data
public class EcPromoteInfo {
	
	//推广类型(1小店内推广  3定向优惠)
	@JsonProperty("promote_type")
	private Integer promoteType;
}
