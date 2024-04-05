package com.mall4j.cloud.user.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class AddPushSonTaskMediaBO {

    @ApiModelProperty(value = "推送子任务ID")
    private Long groupPushSonTaskId;

    private Long groupPushTaskId;

    @ApiModelProperty(value = "素材内容")
    private String media;

    @ApiModelProperty(value = "内容类型，image：图片，video：视频，link：H5，miniprogram：小程序")
    private String type;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

}
