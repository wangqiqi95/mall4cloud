package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 收钱吧分期支付下的一级支付实体
 */
@Data
public class PurchaseSpecifiedPayment {
	
	@ApiModelProperty("分期信息，分期支付时有效。数组元素为installmentTenders对象")
	private List<PurchaseInstallmentTenders> installment_tenders;
	
	@ApiModelProperty("礼品卡支付方式下，按默认顺序选中其下足额的礼品卡,0-否，1-是")
	private String selected_giftcard;
	
	@ApiModelProperty("是否强制使用分笔支付，是则直接进入分笔收银台,0-否，1-是,目前仅H5和PC场景支持。")
	private String partial_payment;

}
