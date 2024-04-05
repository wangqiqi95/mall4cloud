package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("活动优惠信息")
public class OrderActivityDTO {

    @ApiModelProperty("活动Id")
    private Long activityId;
    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动总优惠金额")
    private Long activityReduceTotal;
}
