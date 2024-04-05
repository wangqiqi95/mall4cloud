package com.mall4j.cloud.payment.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 唤起收钱吧收银台参数VO
 */
@Data
public class SQBPayInfoVO {
	@ApiModelProperty("必填,轻POS购买接口成功返回的order_token值")
	private String token;
	
	@ApiModelProperty("非必填,轻POS查询完订单后，order.assigned_tender.tender_sn值")
	private String tender_sn;
	
	@ApiModelProperty("必填,宿主小程序appid")
	private String appid;
	
	@ApiModelProperty("必填,宿主小程序用户openid")
	private String openid;
	
	@ApiModelProperty("非必填,返回宿主小程序标示：00-返回（默认）；01-不返回")
	private String back_flag;
	
	@ApiModelProperty("非必填,收银台：splitpayment-分笔支付")
	private String selected;
	
	@ApiModelProperty("非必填,宿主小程序用户union_id")
	private String union_id;
	
	@ApiModelProperty("非必填,商户会员编号")
	private String client_member_id;
}
