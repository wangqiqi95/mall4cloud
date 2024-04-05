package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0获取用户优惠券详情请求实体
 */
@Data
public class EcGetUserCouponRequest {
	
	@JsonProperty("discount_info")
	private String openid;
	
	//用户优惠券ID
	@JsonProperty("user_coupon_id")
	private String userCouponId;
}
