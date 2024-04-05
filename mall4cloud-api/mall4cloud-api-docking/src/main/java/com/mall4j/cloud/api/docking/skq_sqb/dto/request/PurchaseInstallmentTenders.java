package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 收钱吧分期支付下的二级支付实体
 */
@Data
public class PurchaseInstallmentTenders {
	
	@ApiModelProperty("必传,分期支付下的二级支付方式")
	private String sub_tender_type;
	
	@ApiModelProperty("必传,分期产品具体参数")
	private List<PurchaseInstallmentOptions> installment_options;
	
}
