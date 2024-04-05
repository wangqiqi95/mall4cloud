package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 微信小程序订阅模版消息 值VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 13:14:50
 */
public class WeixinMaSubscriptTmessageOptionalValueVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("模版类型id")
    private String templateTypeId;

    @ApiModelProperty("可选值value")
    private String value;

    @ApiModelProperty("模板值名称")
    private String text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(String templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "WeixinMaSubscriptTmessageOptionalValueVO{" +
				"id=" + id +
				",templateTypeId=" + templateTypeId +
				",value=" + value +
				",text=" + text +
				'}';
	}
}
