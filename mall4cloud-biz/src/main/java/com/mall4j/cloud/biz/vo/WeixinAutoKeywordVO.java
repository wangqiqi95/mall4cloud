package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.biz.dto.WeixinImgTemplateDTO;
import com.mall4j.cloud.biz.dto.WeixinMaTemplateDTO;
import com.mall4j.cloud.biz.dto.WeixinNewsTemplateDTO;
import com.mall4j.cloud.biz.dto.WeixinTextTemplateDTO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 微信关键字表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-19 16:06:52
 */
@Data
public class WeixinAutoKeywordVO {
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

	@ApiModelProperty("回复内容类型")
	private String msgType;
	@ApiModelProperty("回复内容类型名称")
	private String msgTypeName;

	@ApiModelProperty("是否启用： 0否 1是")
	private Integer isWork;

//	@NotEmpty
//	@ApiModelProperty("消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)")
//	private String msgType;

	@ApiModelProperty("文字内容")
	private List<WeixinTextTemplateVO> textTemplates;

	@ApiModelProperty("图片内容")
	private List<WeixinImgTemplateVO> imgTemplates;

	@ApiModelProperty("小程序内容")
	private List<WeixinMaTemplateVO> maTemplates;

	@ApiModelProperty("图文内容")
	private List<WeixinNewsTemplateContentVO> newsTemplates;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	protected Date createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty("更新时间")
	protected Date updateTime;
}
