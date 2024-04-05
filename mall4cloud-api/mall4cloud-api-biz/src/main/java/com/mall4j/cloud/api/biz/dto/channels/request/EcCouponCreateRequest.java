package com.mall4j.cloud.api.biz.dto.channels.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.channels.EcDiscountInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcPromoteInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcReceiveInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcValidInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcCouponExtInfo;
import lombok.Data;

/**
 * 视频号4.0优惠券创建请求实体
 */
@Data
public class EcCouponCreateRequest {
	
	/*
	1	商品条件折券, discount_condition.product_ids, discount_condition.product_cnt, discount_info.discount_num 必填
	2	商品满减券, discount_condition.product_ids, discount_condition.product_price, discount_info.discount_fee 必填
	3	商品统一折扣券, discount_condition.product_ids, discount_info.discount_num必填
	4	商品直减券, 如果小于可用的商品中的最小价格会提醒(没有商品时超过50w提醒）, discount_condition.product_ids, discount_fee 必填
	101	店铺条件折扣券, discount_condition.product_cnt, discount_info.discount_num必填
	102	店铺满减券, discount_condition.product_price, discount_info.discount_fee 必填
	103	店铺统一折扣券, discount_info.discount_num 必填
	104	店铺直减券, 如果小于可用的商品中的最小价格会提醒(没有商品时超过50w提醒）, discount_fee 必填
	 */
	
	//优惠券类型
	private Integer type;
	
	//优惠券名称，最长10个中文字符
	private String name;
	
	//折扣信息实体
	@JsonProperty("discount_info")
	private EcDiscountInfo discountInfo;
	
	//优惠券扩展信息实体
	@JsonProperty("ext_info")
	private EcCouponExtInfo extInfo;
	
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
