package com.mall4j.cloud.api.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 优惠券关联活动DTO
 */
@Data
public class TCouponActivityCentreParamDTO {
    private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty("活动id")
	private Long activityId;

	@NotNull
	@ApiModelProperty("活动类型：1-限时调价 2-会员日活动调价 3-虚拟门店价")
	private Integer activitySource;

	@ApiModelProperty(value = "优惠券名称")
	private String name;

	@ApiModelProperty(value = "优惠券状态（0：有效/1：失效）")
	private Short status;

	@ApiModelProperty("优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
	private Short kind;

	@ApiModelProperty("优惠券来源信息（1：小程序添加/2：CRM同步优惠券）")
	private Short sourceType;

	@ApiModelProperty("第三方券id")
	private String crmCouponId;
}
