package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0优惠券领用信息实体
 */
@Data
public class EcReceiveInfo {

	//单人限领张数
	@JsonProperty("limit_num_one_person")
	private Integer limitNumOnePerson;
	
	//优惠券领用总数
	@JsonProperty("total_num")
	private Integer totalNum;
	
	//优惠券领用开始时间戳
	@JsonProperty("start_time")
	private Long startTime;
	
	//优惠券领用结束时间戳
	@JsonProperty("end_time")
	private Long endTime;


	
}
