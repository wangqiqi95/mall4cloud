package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import com.mall4j.cloud.api.docking.skq_sqb.dto.response.common.providerResponse.ProviderResponse;
import lombok.Data;

import java.util.List;

/**
 * 销售类结果通知支付渠道,移动支付交易信息
 */
@Data
public class ResultNoticeMobilePaymentInfo {
	
	/**
	 * 付款人ID，支付平台（微信，支付宝）上的付款人ID
	 */
	private String payer_uid;
	
	/**
	 * 活动优惠
	 */
	private List<ResultNoticePayment> payment_list;
	
	/**
	 * 优惠详情
	 */
	private ProviderResponse provider_response;

}
