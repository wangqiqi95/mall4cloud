package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdatePopUpAdPageDTO {

    @ApiModelProperty(value = "主键")
    private Long popUpAdPageId;

    @ApiModelProperty(value = "页面路径")
    private String pageUrl;

}
