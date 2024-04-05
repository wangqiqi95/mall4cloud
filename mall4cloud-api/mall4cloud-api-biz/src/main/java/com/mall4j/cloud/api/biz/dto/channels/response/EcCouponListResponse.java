package com.mall4j.cloud.api.biz.dto.channels.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcCouponData;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;


/**
 * 视频号4.0获取优惠券ID列表响应实体
 */
@Data
public class EcCouponListResponse extends BaseResponse {
	/*
	errcode	number	错误码
	errmsg	string	错误信息
	coupons[].coupon_id	string	优惠券ID
	total_num	number	优惠券总数
	 */
	
	//优惠券ID集合
	List<EcCouponData> coupons;
	
	//优惠券总数
	@JsonProperty("total_num")
	private Integer totalNum;
	
	//翻页上下文，第一次请求填空，后续请求值为上次请求的返回
	@JsonProperty("page_ctx")
	private String pageCtx;
}
