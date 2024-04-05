package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0优惠券更新状态请求实体
 */
@Data
public class EcCouponUpdateStatusRequest {

	//优惠券id
	@JsonProperty("coupon_id")
	private String couponId;
	
	//优惠券状态(2生效 4已作废 5删除)
	private String status;
}
