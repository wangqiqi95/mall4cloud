package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageAdTreeVO {

    @ApiModelProperty(value = "页面URL")
    private String pageUrl;

    @ApiModelProperty(value = "页面相关广告集合")
    private List<Long> adList;
}
