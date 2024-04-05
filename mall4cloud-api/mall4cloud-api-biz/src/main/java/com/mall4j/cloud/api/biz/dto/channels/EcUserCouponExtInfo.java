package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0用户优惠券扩展信息
 */
@Data
public class EcUserCouponExtInfo {
	
	//优惠券失效时间戳
	@JsonProperty("invalid_time")
	private Long invalidTime;
	
	//商品折扣券领取后跳转的商品id
	@JsonProperty("jump_product_id")
	private Integer jumpProductId;
	
	//备注信息
	private Integer notes;
	
	//优惠券有效时间戳
	@JsonProperty("valid_time")
	private Long validTime;
}
