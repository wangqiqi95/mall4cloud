package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class AttachMentBaseDTO implements Serializable {

    @ApiModelProperty("类型:图片为-image/小程序-miniprogram/链接-link/video-视频/素材-material/文件-file")
    private String  msgType;

    @ApiModelProperty("企微素材url|小程序封面路径")
    private String  picUrl;

    @ApiModelProperty("企微素材本地图片回显url|小程序封面路径")
    private String  localUrl;

    @ApiModelProperty("文件原始名称")
    private String fileName;

    @ApiModelProperty("小程序消息标题")
    private String  miniProgramTitle;

    @ApiModelProperty("小程序appid")
    private String  appId;

    @ApiModelProperty("小程序page")
    private String  page;

    @ApiModelProperty("企微素材id")
    private String  mediaId;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty("素材库id")
    private Long materialId;

    @ApiModelProperty("图文描述")
    private String desc;

    private Long attachId;
    private String data;
}
