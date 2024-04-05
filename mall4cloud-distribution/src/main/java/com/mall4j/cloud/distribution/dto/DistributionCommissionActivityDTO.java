package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 佣金配置-活动佣金DTO
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@Data
public class DistributionCommissionActivityDTO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("活动名称")
	@NotBlank(message = "活动名称不能为空")
    private String activityName;

    @ApiModelProperty("导购佣金状态 0-禁用 1-启用")
	@NotNull(message = "导购佣金状态不能为空")
    private Integer guideRateStatus;

    @ApiModelProperty("导购佣金")
	@NotNull(message = "导购佣金不能为空")
    private BigDecimal guideRate;

    @ApiModelProperty("微客佣金状态 0-禁用 1-启用")
	@NotNull(message = "微客佣金状态不能为空")
    private Integer shareRateStatus;

    @ApiModelProperty("微客佣金")
	@NotNull(message = "微客佣金不能为空")
    private BigDecimal shareRate;

	@ApiModelProperty("发展佣金状态 -1-全部 0-禁用 1-启用")
	@NotNull(message = "发展佣金状态不能为空")
	private Integer developRateStatus;

	@ApiModelProperty("发展佣金")
	@NotNull(message = "发展佣金不能为空")
	private BigDecimal developRate;

    @ApiModelProperty("活动状态 0-失效 1-生效")
	@NotNull(message = "活动状态不能为空")
    private Integer status;

    @ApiModelProperty("活动时间类型 0-长期生效 1-指定时间段生效")
	@NotNull(message = "活动时间类型不能为空")
    private Integer limitTimeType;

    @ApiModelProperty("适用门店类型 0-所有门店 1-指定门店 ")
	@NotNull(message = "适用门店类型不能为空")
    private Integer limitStoreType;

	@ApiModelProperty("适用门店集合")
    private List<Long> limitStoreIdList;

    @ApiModelProperty("商品范围类型 0-所有商品 1-指定商品 ")
	@NotNull(message = "商品范围类型不能为空")
    private Integer limitSpuType;

	@ApiModelProperty("订单类型 0-普通订单 1-代购订单")
	@NotNull(message = "订单类型不能为空")
	private Integer limitOrderType;

	@ApiModelProperty("适用商品集合")
	private List<Long> limitSpuIdList;

    @ApiModelProperty("活动开始时间")
    private Date startTime;

    @ApiModelProperty("活动结束时间")
    private Date endTime;

    @ApiModelProperty("备注")
    private String remark;
}
