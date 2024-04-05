package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信消息模板VO
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 16:17:14
 */
@Data
public class WeixinTmessageVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("模板ID")
    private String templateId;

    @ApiModelProperty("消息内容标题")
    private String title;

	@ApiModelProperty("模板标题")
	private String templateTitle;

    @ApiModelProperty("模板所属行业的一级行业")
    private String primaryIndustry;

    @ApiModelProperty("模板所属行业的二级行业")
    private String deputyIndustry;

    @ApiModelProperty("模板内容")
    private String content;

    @ApiModelProperty("模板示例")
    private String example;

    @ApiModelProperty("公众号id")
    private String appId;

    @ApiModelProperty("模板用途：1公众号 2小程序")
    private Integer dataSrc;

    @ApiModelProperty("0禁用 1启用")
    private Integer status;

    @ApiModelProperty("备注")
    private String remarks;

}
