package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信小程序订阅模版消息 值
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 13:14:50
 */
public class WeixinMaSubscriptTmessageOptionalValue extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 模版类型id
     */
    private String templateTypeId;

    /**
     * 可选值value
     */
    private String value;

    /**
     * 模板值名称
     */
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
		return "WeixinMaSubscriptTmessageOptionalValue{" +
				"id=" + id +
				",templateTypeId=" + templateTypeId +
				",value=" + value +
				",text=" + text +
				'}';
	}
}
