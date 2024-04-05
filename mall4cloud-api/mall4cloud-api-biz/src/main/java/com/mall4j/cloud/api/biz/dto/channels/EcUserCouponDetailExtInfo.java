package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0获取用户优惠券详情扩展信息实体
 */
@Data
public class EcUserCouponDetailExtInfo {
	
	//优惠券核销时间
	@JsonProperty("use_time")
	private Long useTime;
}
