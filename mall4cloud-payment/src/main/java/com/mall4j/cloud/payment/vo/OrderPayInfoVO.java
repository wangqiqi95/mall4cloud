package com.mall4j.cloud.payment.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单支付记录VO
 */
@Data
public class OrderPayInfoVO {
	
	@ApiModelProperty("订单支付方式 1 微信支付 2支付宝 10收钱吧轻POS支付")
	private Integer orderPayType;
}
