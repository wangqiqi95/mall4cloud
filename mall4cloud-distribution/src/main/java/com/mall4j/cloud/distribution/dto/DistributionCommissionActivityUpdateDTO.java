package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DistributionCommissionActivityUpdateDTO {

    @NotNull(message = "活动id不能为空")
    @ApiModelProperty("活动id")
    private Long id;

    @ApiModelProperty("活动状态 0-失效 1-生效")
    private Integer status;

    @ApiModelProperty("导购佣金状态 0-禁用 1-启用")
    private Integer guideRateStatus;

    @ApiModelProperty("导购佣金")
    private BigDecimal guideRate;

    @ApiModelProperty("微客佣金状态 0-禁用 1-启用")
    private Integer shareRateStatus;

    @ApiModelProperty("微客佣金")
    private BigDecimal shareRate;

    @ApiModelProperty("发展佣金状态 -1-全部 0-禁用 1-启用")
    private Integer developRateStatus;

    @ApiModelProperty("发展佣金")
    private BigDecimal developRate;

    @ApiModelProperty("活动开始时间")
    private Date startTime;

    @ApiModelProperty("活动结束时间")
    private Date endTime;


}
