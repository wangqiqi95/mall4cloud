package com.mall4j.cloud.payment.bo;

import lombok.Data;

/**
 * 退款后，微信/支付宝返回的一些基础数据
 * @author FrozenWatermelon
 * @date 2020/12/25
 */
@Data
public class RefundInfoResultBO {

	/**
	 * 商城退款单号
	 */
	private Long refundId;

	/**
	 * 退款单编号
	 */
	private String refundNumber;

	/**
	 * 第三方订单退款流水号
	 */
	private String bizRefundNo;

	/**
	 * 是否退款成功
	 */
	private Boolean refundSuccess;

	/**
	 * 退款成功的标记
	 */
	private String successString;

	/**
	 * 退款回调信息
	 */
	private String callbackContent;

}
