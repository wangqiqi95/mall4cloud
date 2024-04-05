package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcUserCoupon;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0用户优惠券列表响应实体
 */
@Data
public class EcUserCouponListResponse extends BaseResponse {
	
	//用户优惠券实体
	@JsonProperty("user_coupon_list")
	List<EcUserCoupon> userCouponList;
}
