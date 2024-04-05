package com.mall4j.cloud.user.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupPushSonTaskMediaVO {

    @ApiModelProperty(value = "主键")
    private Long sonTaskMediaId;

    @ApiModelProperty(value = "推送子任务ID")
    private Long groupPushSonTaskId;

    @ApiModelProperty(value = "素材内容")
    private String media;

    @ApiModelProperty(value = "内容类型，image：图片，video：视频，link：H5，miniprogram：小程序")
    private String type;


}
