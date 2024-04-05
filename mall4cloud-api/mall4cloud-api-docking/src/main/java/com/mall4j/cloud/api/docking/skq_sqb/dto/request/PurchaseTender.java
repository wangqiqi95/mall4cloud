package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收钱吧购买操作,订单流水信息实体
 */
@Data
public class PurchaseTender {
	
	@ApiModelProperty("必传,支付方式类型：0-其他，1-预授权完成，2-银行卡，3-QRCode，4-分期，99-外部")
	private String tender_type;
	/*
		001-现金，如需在轻POS录入其他支付方式，在对接时与收钱吧沟通配置；
		101-银行卡预授权完成，
		102-微信预授权完成，103-支付宝预授权完成；
		201-银行卡；
		301-微信，302-支付宝；
		401-银行卡分期；402-花呗分期
	 */
	@ApiModelProperty("二级支付方式类型（开发者不需要传入具体值）")
	private String sub_tender_type;
	
	@ApiModelProperty("当tender_type为99时，必填，且传入参数值为收银系统自定义的支付方式")
	private String sub_tender_desc;
	
	@ApiModelProperty("必传,支付金额，精确到分；")
	private String amount;
	
	@ApiModelProperty("必传,商户系统流水号，在商户系统中唯一")
	private String transaction_sn;
	
	@ApiModelProperty("指定分期数，分期支付时必填,3-分3期,6-分6期,12-分12期")
	private String installment_number;
	
	@ApiModelProperty("商家贴息比例0~100的整数，跟期数同时出现，指定期数时必传。花呗分期只能传0或者100，商家承担手续费传入100，用户承担手续费传入0。")
	private String installment_fee_merchant_percent;
	
	@ApiModelProperty("tender_type 为1时必填，内容为预授权订单操作成功后，轻POS返回给商户的预授权流水号")
	private String original_tender_sn;
	
	@ApiModelProperty("标记该tender是否已经支付完成。0：待操作,1：已完成（tender_type为99时必须为1：已完成；其他的tender_type必须为0：待操作）")
	private String pay_status;
	
	@ApiModelProperty("tender创建时间，当pay_status为1时必填, 格式详见 1.5时间数据元素定义")
	private String create_time;
	
}
