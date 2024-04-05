package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MediaJsonVO {

    @ApiModelProperty("消息类型 text:文本,image:图片,video:视频,miniprogram:小程序 link ： h5链接")
    @NotBlank(message = "消息类型不能为空")
    private String type;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("消息图片url")
    private String url;

    @ApiModelProperty("消息图片url")
    private String appId;

    @ApiModelProperty("小程序APPid")
    private String appTitle;

    @ApiModelProperty("小程序标题")
    private String appPage;

    @ApiModelProperty("小程序封面url")
    private String appPic;

    private Boolean outSide;

    private Long sonTaskMediaId;

    private Long materialId;
    @ApiModelProperty("素材库内容")
    private String materialJson;
}
