package com.mall4j.cloud.api.biz.dto.channels;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0优惠券数据实体
 */
@Data
public class EcCouponData {
	
	//优惠券id
	@JsonProperty("coupon_id")
	private String couponId;
}
