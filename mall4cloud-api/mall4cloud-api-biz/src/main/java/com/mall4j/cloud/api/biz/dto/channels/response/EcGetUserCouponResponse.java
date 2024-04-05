package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcUserCouponDetail;
import lombok.Data;

import java.util.List;

/**
 * 视频号4.0获取用户优惠券详情请求实体
 */
@Data
public class EcGetUserCouponResponse extends EcBaseResponse{
	
	//用户openid
	private String openid;
	
	//用户unionid，小店接入 open 平台后生成的用户券会返回
	private String unionid;
	
	//用户优惠券实体
	@JsonProperty("user_coupon")
	private EcUserCouponDetail userCoupon;
}
