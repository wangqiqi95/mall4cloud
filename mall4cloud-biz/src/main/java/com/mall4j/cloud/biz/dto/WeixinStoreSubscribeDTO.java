package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 微信门店回复内容DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 16:43:04
 */
@Data
public class WeixinStoreSubscribeDTO{
    private static final long serialVersionUID = 1L;

    @NotEmpty
    @ApiModelProperty("")
    private String id;

//	@NotEmpty
//    @ApiModelProperty("关注回复id")
//    private String subscribeId;

//	@NotEmpty
//    @ApiModelProperty("门店id")
//    private String storeId;
//
//	@NotEmpty
//    @ApiModelProperty("公众号原始id")
//    private String appId;

    /**
     * 消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)
     */
//    @NotEmpty
//    @ApiModelProperty("消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)")
//    private String msgType;

    @ApiModelProperty("文字内容")
    private WeixinTextTemplateDTO textTemplate;

    @ApiModelProperty("图片内容")
    private WeixinImgTemplateDTO imgTemplate;

    @ApiModelProperty("小程序内容")
    private WeixinMaTemplateDTO maTemplate;

    @ApiModelProperty("图文内容")
    private WeixinNewsTemplateDTO newsTemplate;

}
