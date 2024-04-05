package com.mall4j.cloud.api.payment.bo;

import lombok.Data;

/**
 * 收钱吧订单支付成功BO
 */
@Data
public class SQBOrderPaySuccessBO {
	
	/**
	 * 订单编号
	 */
	private String orderNumber;
	
	/**
	 * 第三方订单流水号
	 */
	private String bizPayNo;
	
	/**
	 * 回调内容
	 */
	private String callbackContent;
}
