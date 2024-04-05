package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CrmUserTags {
    @ApiModelProperty(value = "标签ID")
    private String tagId;

    @ApiModelProperty(value = "标签名")
    private String tagName;

    @ApiModelProperty("标签Value")
    private List<String> tagValue;
}
