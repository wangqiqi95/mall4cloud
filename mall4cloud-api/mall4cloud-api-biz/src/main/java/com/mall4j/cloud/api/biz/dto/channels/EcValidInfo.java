package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0优惠券有效期信息实体
 */
@Data
public class EcValidInfo {

	//优惠券有效期开始时间，valid_type为1时必填
	@JsonProperty("start_time")
	private Long startTime;
	//优惠券有效期结束时间，valid_type为1时必填
	@JsonProperty("end_time")
	private Long endTime;
	
	//优惠券有效期天数，valid_type为2时必填
	@JsonProperty("valid_day_num")
	private Integer validDayNum;
	
	//优惠券有效期类型 (1指定时间范围生效  2生效天数)
	@JsonProperty("valid_type")
	private Integer validType;
}
