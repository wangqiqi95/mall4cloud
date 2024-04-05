package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 微信关注回复DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:59
 */
@Data
public class WeixinTemplatesManagerDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)
     */
    @NotEmpty
    @ApiModelProperty("消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)")
    private String msgType;

    @ApiModelProperty("文字内容")
	private WeixinTextTemplateDTO textTemplate;

    @ApiModelProperty("图片内容")
	private WeixinImgTemplateDTO imgTemplate;

    @ApiModelProperty("小程序内容")
    private WeixinMaTemplateDTO maTemplate;

    @ApiModelProperty("图文内容")
    private WeixinNewsTemplateDTO newsTemplate;
}
