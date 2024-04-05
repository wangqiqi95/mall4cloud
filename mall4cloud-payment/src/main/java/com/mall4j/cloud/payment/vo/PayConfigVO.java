package com.mall4j.cloud.payment.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 支付配置VO
 */
@Data
public class PayConfigVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("主键ID")
	private Long id;
	
	@ApiModelProperty("选中金额限制(0：未选中/1：选中)")
	private Integer selectedAmountLimit;
	
	@ApiModelProperty("金额限制类型（0：不限/1：满额）")
	private Integer amountLimitType;

	@ApiModelProperty("限制金额")
	private BigDecimal amountLimitNum;
	
	@ApiModelProperty("选中会员限制(0：未选中/1：选中)")
	private Integer selectedMemberLimit;

	@ApiModelProperty("会员限制类型（0：不限/1：指定会员）")
	private Integer memberLimitType;
	
	@ApiModelProperty(value = "配置会员列表")
	private List<String> memberList;
	
	@ApiModelProperty(value = "配置会员数量")
	private Integer limitMemberCount;
}
