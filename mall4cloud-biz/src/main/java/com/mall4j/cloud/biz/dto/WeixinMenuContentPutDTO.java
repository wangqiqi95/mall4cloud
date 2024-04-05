package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 微信菜单表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
@Data
public class WeixinMenuContentPutDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单id")
    private String id;

    @ApiModelProperty("文字内容")
    private WeixinTextTemplateDTO textTemplate;

    @ApiModelProperty("图片内容")
    private WeixinImgTemplateDTO imgTemplate;

    @ApiModelProperty("小程序内容")
    private WeixinMaTemplateDTO maTemplate;

    @ApiModelProperty("图文内容")
    private WeixinNewsTemplateDTO newsTemplate;

    @ApiModelProperty("外部链接")
    private WeixinLinksucaiDTO linksucai;

    @ApiModelProperty("商城功能")
    private WeixinShopMallTemplateDTO shopMallTemplate;

}
