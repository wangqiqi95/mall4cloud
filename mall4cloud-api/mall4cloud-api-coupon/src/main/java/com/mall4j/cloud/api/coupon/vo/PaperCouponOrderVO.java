package com.mall4j.cloud.api.coupon.vo;

import lombok.Data;

@Data
public class PaperCouponOrderVO {
	
	/**
	 * 优惠券id
	 */
	private Long couponId;
	
	/**
	 * 优惠券名称
	 */
	private String name;
	
	/**
	 * 优惠券类型（0：抵用券/1：折扣券）
	 */
	private Integer type;
	
	/**
	 * 抵用金额
	 */
	private Long reduceAmount;
	
	/**
	 * 限制金额
	 */
	private Long amountLimitNum;
	
	/**
	 * 订单编号
	 */
	private Long orderNo;
	
	/**
	 * 券码
	 */
	private String couponCode;

}
