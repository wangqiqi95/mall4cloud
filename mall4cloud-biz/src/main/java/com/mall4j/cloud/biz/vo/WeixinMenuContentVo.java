package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.biz.dto.*;
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
public class WeixinMenuContentVo {
    private static final long serialVersionUID = 1L;

    @NotEmpty
    @ApiModelProperty("菜单id")
    private String id;

    @ApiModelProperty("文字内容")
    private WeixinTextTemplateVO textTemplate;

    @ApiModelProperty("图片内容")
    private WeixinImgTemplateVO imgTemplate;

    @ApiModelProperty("小程序内容")
    private WeixinMaTemplateVO maTemplate;

    @ApiModelProperty("图文内容")
    private WeixinNewsTemplateContentVO newsTemplate;

    @ApiModelProperty("外部链接")
    private WeixinLinksucaiVO linksucai;

    @ApiModelProperty("商城功能")
    private WeixinShopMallTemplateVo shopMallTemplate;

}
