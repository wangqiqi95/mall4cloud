package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddPushSonTaskMediaDTO {

    @NotBlank(message = "素材内容为必传项")
    @ApiModelProperty(value = "素材内容")
    private String media;

    @NotBlank(message = "内容类型为必传项")
    @ApiModelProperty(value = "内容类型，image：图片，video：视频，link：H5，miniprogram：小程序")
    private String type;
}
