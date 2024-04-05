package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信扫码回复表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 16:46:42
 */
@Data
public class WeixinAutoScanDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("规则名称")
    private String name;

    @ApiModelProperty("二维码id")
    private String qrcodeId;

    @ApiModelProperty("原始公众号id")
    private String appId;

    @ApiModelProperty("用户交互类型: 0全部 1:已关注用户 2：未关注用户")
    private Integer type;

	@ApiModelProperty("文字内容")
	private WeixinTextTemplateDTO textTemplate;

	@ApiModelProperty("图片内容")
	private WeixinImgTemplateDTO imgTemplate;

	@ApiModelProperty("小程序内容")
	private WeixinMaTemplateDTO maTemplate;

	@ApiModelProperty("图文内容")
	private WeixinNewsTemplateDTO newsTemplate;

    @ApiModelProperty(value = "标签集合",required = false)
    private String tags;

}
