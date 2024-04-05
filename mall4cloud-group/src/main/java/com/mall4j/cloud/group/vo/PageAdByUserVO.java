package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageAdByUserVO {

    @ApiModelProperty(value = "广告ID")
    private Long popUpAdId;

    @ApiModelProperty(value = "页面路径")
    private String pageUrl;

    @ApiModelProperty(value = "指定人群标签")
    private Long userTagId;


}
