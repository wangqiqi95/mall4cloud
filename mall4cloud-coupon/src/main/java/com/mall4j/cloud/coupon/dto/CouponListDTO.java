package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 优惠券列表搜索条件 *
 * @author shijing
 * @date 2022-01-03 14:55:56
 */
@Data
@ApiModel(description = "优惠券列表搜索条件")
public class CouponListDTO {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "页码",required = true)
	private Integer pageNo = 1;

	@ApiModelProperty(value = "每页长度",required = true)
	private Integer pageSize = 10;

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
