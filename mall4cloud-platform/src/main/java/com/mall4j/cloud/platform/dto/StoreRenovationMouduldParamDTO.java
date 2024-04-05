package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("微页面查询")
public class StoreRenovationMouduldParamDTO {
    @ApiModelProperty("关键字")
    private String keyword;
    @ApiModelProperty("页面类型 0-微页面，1 -主页，2-底部导航，3-分类页，4-会员主页")
    private Integer homeStatus;
}
