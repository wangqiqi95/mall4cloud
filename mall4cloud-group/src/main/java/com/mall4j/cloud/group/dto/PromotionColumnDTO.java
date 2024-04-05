package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PromotionColumnDTO implements Serializable {

    @ApiModelProperty("活动名称")
    private String activityName;
    @ApiModelProperty("活动状态0未启用，2未开始，3已开始，4，已结束")
    private Integer status;
    @ApiModelProperty("适用门店id集合")
    private String shopIds;
}
