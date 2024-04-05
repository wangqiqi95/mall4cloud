package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收钱吧分期支付操作实体
 */
@Data
public class PurchaseInstallmentOptions {
	
	@ApiModelProperty("分期数。必填,3-分3期,6-分6期,12-分12期")
	private String installment_number;
	
	@ApiModelProperty("商家是否贴息。100=商家贴息，0=商家不贴息,不传则执行后台配置品牌默认的贴息方式")
	private String installment_fee_merchant_percent;
}
