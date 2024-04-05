package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import lombok.Data;

/**
 * 销售类结果通知支付渠道,银行卡交易信息
 */
@Data
public class ResultNoticeBankCardInfo {
	
	/**
	 * 	凭证号
	 */
	private String trace_no;
	
	/**
	 * 批次号
	 */
	private String batch_no;
	
	/**
	 * 系统参考号
	 */
	private String ref_no;
	
	/**
	 * 授权码
	 */
	private String auth_no;
	
	/**
	 * 发卡行号
	 */
	private String issuer_no;
	
	/**
	 * 发卡行名称
	 */
	private String issuer_name;
	
	/**
	 * 卡号（卡号中段为“*”号，已加密）
	 */
	private String card_no;
	
	/**
	 * 借贷记卡标识 0：借记；1：贷记
	 */
	private String card_type_identity;
	
	/**
	 * 内外卡标识 0：内卡；1：外卡
	 */
	private String abroad_card_type;

}
