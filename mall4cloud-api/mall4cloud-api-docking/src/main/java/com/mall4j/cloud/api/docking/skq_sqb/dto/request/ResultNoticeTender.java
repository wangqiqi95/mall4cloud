package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import lombok.Data;

/**
 * 销售类结果通知本订单的流水信息
 */
@Data
public class ResultNoticeTender {
	
	/**
	 * 	支付/退款指定中商户系统流水号，在商户系统中唯一
	 */
	private String original_transaction_sn;
	
	/**
	 * 	退款对应的原购货订单完成后本系统返回的支付流水号
	 */
	private String original_tender_sn;
	
	/**
	 * 	支付/退款成功后，轻POS生成的唯一流水号
	 */
	private String tender_sn;
	
	/**
	 * 	支付/退款金额，精确到分
	 */
	private String amount;
	
	/**
	 * 商家实收/实退金额，精确到分
	 */
	private String collected_amount;
	
	/**
	 * 消费者实付/实退金额，精确到分
	 */
	private String paid_amount;
	
	/**
	 * 支付源交易流水完成时间，格式详见 1.5时间数据元素定义
	 */
	private String pay_time;
	
	/**
	 * 	支付状态, 1:待操作; 2:支付中; 3:支付成功; 4:退款中; 5:退款成功;6:退款失败;7:支付失败;8:未知状态;
	 */
	private String pay_status;
	
	/**
	 * 	结果原因描述
	 */
	private String reason;
	
	/**
	 * 支付方式类型：0-其他，1-预授权完成，2-银行卡，3-QRCode，4-分期，99-外部
	 */
	private String tender_type;
	
	/**
	 * 	二级支付方式类型：
	 *  001-现金，如需在轻POS录入其他支付方式，在对接时与收钱吧沟通配置；
	 * 	101-银行卡预授权完成，102-微信预授权完成，103-支付宝预授权完成；
	 * 	201-银行卡；
	 * 	301-微信，302-支付宝；
	 * 	401-银行卡分期，402-花呗分期
	 */
	private String sub_tender_type;
	
	/**
	 * 	二级支付方式描述。如：微信支付
	 */
	private String sub_tender_desc;
	
	/**
	 * 	支付渠道流水号，操作成功时存在。
	 * 微信支付宝：微信支付宝流水号；
	 * 银行卡：银行卡流水号。
	 */
	private String channel_sn;
	
	/**
	 * 移动支付：收钱吧传入支付宝/微信的out_trade_no；
	 * 银行卡支付：交易返回的交易授权码
	 */
	private String internal_transaction_sn;
	
	/**
	 * 	分期数，分期支付时返回
	 */
	private String installment_number;
	
	/**
	 * 	商家贴息比例，分期支付时返回
	 */
	private String installment_fee_merchant_percent;
	
	/**
	 * 	付渠道（银行卡/移动支付/礼品卡等）交易信息。定义见下表
	 */
	private ResultNoticeChannelInfo channel_info;
}
