package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0优惠券库存信息实体
 */
@Data
public class EcStockInfo {

	//优惠券剩余量
	@JsonProperty("issued_num")
	private Integer issuedNum;
	
	//优惠券领用量
	@JsonProperty("receive_num")
	private Integer receiveNum;
	
	//优惠券已用量
	@JsonProperty("used_num")
	private Integer usedNum;
}
