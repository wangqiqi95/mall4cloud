package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributionPromotionGroupVO {

    @ApiModelProperty("活动类型 1海报 2专题 3朋友圈 4商品")
    private Integer activityType;

    @ApiModelProperty("统计数量")
    private Integer count;

}
