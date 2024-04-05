package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import lombok.Data;

/**
 * 销售类结果通知支付渠道
 */
@Data
public class ResultNoticeChannelInfo {
	
	/**
	 * 	银行卡交易信息二级支付方式为“201-银行卡”
	 */
	private ResultNoticeBankCardInfo bank_card_info;
	
	
	/**
	 * 	移动支付交易信息二级支付方式为 “301-微信”，“302-支付宝”，“402-花呗分期”
	 */
	private ResultNoticeMobilePaymentInfo mobile_payment_info;
	
	
	/**
	 * 卡券支付交易信息二级支付方式为“801-会员储值卡”、“804-单品券”
	 */
	private ResultNoticeCardPaymentInfo card_payment_info;
	
	


}
