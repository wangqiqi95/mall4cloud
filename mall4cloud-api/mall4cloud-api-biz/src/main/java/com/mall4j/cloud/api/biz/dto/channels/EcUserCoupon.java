package com.mall4j.cloud.api.biz.dto.channels;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0用户优惠券实体
 */
@Data
public class EcUserCoupon {

	//用户优惠券ID
	@JsonProperty("user_coupon_id")
	private String userCouponId;
	//优惠券ID
	private String couponId;
	
}
