package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 视频号4.0获取用户优惠券详情实体
 */
@Data
public class EcUserCouponDetail {
	//用户优惠券ID
	@JsonProperty("user_coupon_id")
	private String userCouponId;
	//优惠券ID
	@JsonProperty("coupon_id")
	private String couponId;
	/*
	COUPON_USER_STATUS_VALID	100	生效中
	COUPON_USER_STATUS_EXPIRED	101	已过期
	COUPON_USER_STATUS_USED	102	已使用
	 */
	//优惠券状态
	private Integer status;
	//优惠券派发时间
	@JsonProperty("create_time")
	private Long createTime;
	//优惠券更新时间
	@JsonProperty("update_time")
	private Long updateTime;
	//优惠券生效时间
	@JsonProperty("start_time")
	private Long startTime;
	//优惠券失效时间
	@JsonProperty("end_time")
	private Long endTime;
	//优惠券核销时间
	@JsonProperty("ext_info")
	private EcUserCouponDetailExtInfo extInfo;
	//优惠券使用的订单id
	@JsonProperty("order_id")
	private String orderId;
	//优惠券金额
	@JsonProperty("discount_fee")
	private Integer discountFee;
}
