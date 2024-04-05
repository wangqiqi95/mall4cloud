package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributionStoreActivityCountVO {

    @ApiModelProperty("进行中")
    private Long inProgress = 0l;
    @ApiModelProperty("未开始")
    private Long notStart = 0l;
    @ApiModelProperty("已结束")
    private Long finished = 0l;

}
