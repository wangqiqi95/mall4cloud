package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import lombok.Data;

/**
 * 销售类结果通知支付渠道,卡券支付交易信息
 */
@Data
public class ResultNoticeCardPaymentInfo {
	/**
	 * 	付款人ID，（微信公众平台unionid）
	 */
	private String payer_unionid;
	
	/**
	 * 	卡号或者券号
	 */
	private String card_number;
	
}
