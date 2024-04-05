package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 素材消息表DTO
 *
 * @author hwy
 * @date 2022-01-25 20:33:45
 */
@Data
public class MaterialMsgDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("消息类型 text:文本,image:图片,video:视频,miniprogram:小程序 h5：H5页面 文件：file 文章：article")
    @NotBlank(message = "消息类型不能为空")
    private String type;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("消息图片url")
    private String url;

    @ApiModelProperty("appId")
    private String appId;

    @ApiModelProperty("appTitle")
    private String appTitle;

    @ApiModelProperty("appPage")
    private String appPage;

    @ApiModelProperty("小程序封面url")
    private String appPic;

    @ApiModelProperty("小程序封面图片|图片|视频的素材id")
    private String mediaId;

    @ApiModelProperty("原始文件名")
    private String fileName;

    @ApiModelProperty("文章url")
    private String articleUrl;
    @ApiModelProperty("文章描述")
    private String articleDesc;

    @ApiModelProperty("是否编辑换新文件: true是/fale否")
    private boolean changeFile=false;

    private String picUrl;


}
