package com.mall4j.cloud.biz.vo.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskAttachmentVO implements Serializable {
    @ApiModelProperty("任务id")
    private Long id;

    private String userId;

    @ApiModelProperty("群发话术")
    private String content;

    @ApiModelProperty("截止时间")
    private Date endTime;

    @ApiModelProperty("素材类型")
    private String type;

    @ApiModelProperty("小程序appid")
    private String appId;

    @ApiModelProperty("小程序标题|H5标题")
    private String title;

    @ApiModelProperty("小程序页面路径|H5页面路径")
    private String pagePath;

    @ApiModelProperty("小程序封面图片路径|H5封面路径|图片路径|视频路径")
    private String url;

    @ApiModelProperty("小程序封面图片|图片|视频的素材id")
    private String mediaId;

}
