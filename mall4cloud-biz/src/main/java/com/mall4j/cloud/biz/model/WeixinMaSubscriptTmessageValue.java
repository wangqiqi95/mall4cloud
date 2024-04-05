package com.mall4j.cloud.biz.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 微信小程序订阅模版消息类型 可选值列表
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 13:14:50
 */
public class WeixinMaSubscriptTmessageValue extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 模板值名称 微信返回的名称
     */
    private String templateValueName;

    /**
     * 模板值value
     */
    private String templateValue;

	/**
	 * 模板值中文名称
	 */
    private String templateKeyName;

	public String getTemplateKeyName() {
		return templateKeyName;
	}

	public void setTemplateKeyName(String templateKeyName) {
		this.templateKeyName = templateKeyName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateValueName() {
		return templateValueName;
	}

	public void setTemplateValueName(String templateValueName) {
		this.templateValueName = templateValueName;
	}

	public String getTemplateValue() {
		return templateValue;
	}

	public void setTemplateValue(String templateValue) {
		this.templateValue = templateValue;
	}

	@Override
	public String toString() {
		return "WeixinMaSubscriptTmessageValue{" +
				"id=" + id +
				",templateId=" + templateId +
				",templateValueName=" + templateValueName +
				",templateValue=" + templateValue +
				'}';
	}
}
