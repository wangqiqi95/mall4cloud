package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionCommissionActivitySearchDTO {


    @ApiModelProperty("活动名称")
    private String  activityName;

    @ApiModelProperty("活动状态 -1-全部 0-失效 1-生效")
    private Integer status;

    @ApiModelProperty("订单类型 0-普通订单 1-代购订单")
    private Integer limitOrderType;

}
