package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DistributionStoreActivityQueryDTO {

    @ApiModelProperty("活动名称")
    private String name;
    @ApiModelProperty("活动状态 -1:全部 0:未开始 1:进行中 2:已结束 3:已禁用")
    private Integer activityStatus;
    @ApiModelProperty("省份编码")
    private String provinceCode;
    @ApiModelProperty("市编码")
    private String cityCode;
    @ApiModelProperty("区编码")
    private String districtCode;
    @ApiModelProperty("报名状态 -1:全部 0:未报名 1:已报名 2:已取消")
    private Integer applyStatus;

    // 用户id
    private Long userId;

}
