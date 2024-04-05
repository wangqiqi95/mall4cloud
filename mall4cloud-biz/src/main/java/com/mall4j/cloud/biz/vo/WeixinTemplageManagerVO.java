package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.biz.dto.WeixinImgTemplateDTO;
import com.mall4j.cloud.biz.dto.WeixinMaTemplateDTO;
import com.mall4j.cloud.biz.dto.WeixinNewsTemplateDTO;
import com.mall4j.cloud.biz.dto.WeixinTextTemplateDTO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信消息自动回复VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:52:24
 */
@Data
public class WeixinTemplageManagerVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    private String templateId;

    private String templateName;

    private String msgType;

    @ApiModelProperty("文字内容")
    private WeixinTextTemplateVO textTemplate;

    @ApiModelProperty("图片内容")
    private WeixinImgTemplateVO imgTemplate;

    @ApiModelProperty("小程序内容")
    private WeixinMaTemplateVO maTemplate;

    @ApiModelProperty("图文内容")
    private WeixinNewsTemplateContentVO newsTemplate;

    @ApiModelProperty("外部去链接")
    private WeixinLinksucaiVO linksucai;

    @ApiModelProperty("商城功能")
    private WeixinShopMallTemplateVo shopMallTemplate;

    private String dataId;
    private Integer dataSrc;
}
