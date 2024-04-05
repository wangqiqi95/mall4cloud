package com.mall4j.cloud.biz.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 *
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
public class NotifyTemplateShop extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 店铺消息通知关联表
     */
    private Long notifyShopId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 通知方式集合用逗号分隔 1.短信 2.公众号 3.站内消息
     */
    private String notifyTypes;

	public Long getNotifyShopId() {
		return notifyShopId;
	}

	public void setNotifyShopId(Long notifyShopId) {
		this.notifyShopId = notifyShopId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getNotifyTypes() {
		return notifyTypes;
	}

	public void setNotifyTypes(String notifyTypes) {
		this.notifyTypes = notifyTypes;
	}

	@Override
	public String toString() {
		return "NotifyTemplateShop{" +
				"notifyShopId=" + notifyShopId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",templateId=" + templateId +
				",notifyTypes=" + notifyTypes +
				'}';
	}
}
