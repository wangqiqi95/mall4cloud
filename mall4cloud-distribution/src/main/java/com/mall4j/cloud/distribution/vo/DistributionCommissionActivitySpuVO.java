package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 佣金配置-活动佣金-商品VO
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@Data
public class DistributionCommissionActivitySpuVO {

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "商品id")
	private Long spuId;

	@ApiModelProperty(value = "商品名称")
	private String spuName;

	@ApiModelProperty("商品介绍主图")
	private String mainImgUrl;

	@ApiModelProperty("导购佣金状态 0-禁用 1-启用")
	private Integer guideRateStatus;

	@ApiModelProperty("导购佣金")
	private BigDecimal guideRate;

	@ApiModelProperty("微客佣金状态 0-禁用 1-启用")
	private Integer shareRateStatus;

	@ApiModelProperty("微客佣金")
	private BigDecimal shareRate;

	@ApiModelProperty("发展佣金状态 0-禁用 1-启用")
	private Integer developRateStatus;

	@ApiModelProperty("发展佣金")
	private BigDecimal developRate;

	@ApiModelProperty(value = "商品售价")
	private Long priceFee;

	@ApiModelProperty(value = "市场价，整数方式保存")
	private Long marketPriceFee;

	@ApiModelProperty(value = "状态")
	private Integer spuStatus;

	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	@ApiModelProperty("创建时间")
	protected Date createTime;

	@ApiModelProperty("更新时间")
	protected Date updateTime;

	private Integer seq;

}
