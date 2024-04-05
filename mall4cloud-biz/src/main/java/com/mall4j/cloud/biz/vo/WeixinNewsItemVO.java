package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信图文模板素材表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:35
 */
@Data
public class WeixinNewsItemVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    private String id;

    @ApiModelProperty("图文id")
    private String newstemplateId;

    @ApiModelProperty("图文缩略图的media_id")
    private String thumbMediaId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("图片路径")
    private String imagePath;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("摘要")
    private String abstracts;

    @ApiModelProperty("")
    private String description;

    @ApiModelProperty("素材顺序")
    private String orderNo;

    @ApiModelProperty("图文：news；外部url：url")
    private String newType;

    @ApiModelProperty("原文链接")
    private String url;

    @ApiModelProperty("外部链接")
    private String externalUrl;

    @ApiModelProperty("是否显示封面：'1':显示,'0':不显示")
    private String showCoverPic;

    @ApiModelProperty("小程序素材id")
    private String maTemplateId;
}
