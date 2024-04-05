package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 微信关键字表DTO
 *
 * @author gmq
 * @date 2022-01-19 16:06:52
 */
@Data
public class WeixinAutoKeywordPutDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

	@NotEmpty
    @ApiModelProperty("规则名称")
    private String name;

	@NotEmpty
    @ApiModelProperty("关键字(英文逗号,分隔开)")
    private String keyword;

	@NotEmpty
    @ApiModelProperty("原始公众号id")
    private String appId;

    @ApiModelProperty("关键字类型1:全匹配  2：模糊匹配")
    private String keywordType;

    @ApiModelProperty("是否启用： 0否 1是")
    private Integer isWork;

//	@ApiModelProperty("回复内容")
//	private List<WeixinTemplatesManagerDTO> templates;

//	@NotEmpty
//	@ApiModelProperty("消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)")
//	private String msgType;
//
	@ApiModelProperty("文字内容")
	private List<WeixinTextTemplateDTO> textTemplates;

	@ApiModelProperty("图片内容")
	private List<WeixinImgTemplateDTO> imgTemplates;

	@ApiModelProperty("小程序内容")
	private List<WeixinMaTemplateDTO> maTemplates;

	@ApiModelProperty("图文内容")
	private List<WeixinNewsTemplateDTO> newsTemplates;
}
