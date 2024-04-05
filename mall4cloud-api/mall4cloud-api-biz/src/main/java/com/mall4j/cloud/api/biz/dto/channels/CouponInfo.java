package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0 优惠券信息实体
 */
@Data
public class CouponInfo {
	//优惠券名称
	private String name;
	
	//折扣信息实体
	@JsonProperty("discount_info")
	private EcDiscountInfo discountInfo;
	
	//优惠券推广信息实体
	@JsonProperty("promote_info")
	private EcPromoteInfo promoteInfo;
	
	//优惠券领用信息实体
	@JsonProperty("receive_info")
	private EcReceiveInfo receiveInfo;
	
	//优惠券有效期信息实体
	@JsonProperty("valid_info")
	private EcValidInfo validInfo;
	
}
