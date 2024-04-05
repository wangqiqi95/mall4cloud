package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信扫码回复表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 16:46:42
 */
@Data
public class WeixinAutoScanVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("规则名称")
    private String name;

    @ApiModelProperty("二维码id")
    private String qrcodeId;
	@ApiModelProperty("二维码名称")
	private String qrcodeName;

    @ApiModelProperty("原始公众号id")
    private String appId;

    @ApiModelProperty("用户交互类型: 0全部 1:已关注用户 2：未关注用户")
    private Integer type;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty(value = "标签集合")
    private String tags;

    @ApiModelProperty("文字内容")
    private WeixinTextTemplateVO textTemplate;

    @ApiModelProperty("图片内容")
    private WeixinImgTemplateVO imgTemplate;

    @ApiModelProperty("小程序内容")
    private WeixinMaTemplateVO maTemplate;

    @ApiModelProperty("图文内容")
    private WeixinNewsTemplateContentVO newsTemplate;

}
