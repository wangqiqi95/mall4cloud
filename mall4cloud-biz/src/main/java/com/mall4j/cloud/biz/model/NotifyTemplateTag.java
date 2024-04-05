package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 
 *
 * @author cl
 * @date 2021-05-20 11:09:53
 */
public class NotifyTemplateTag extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 标签消息通知关联表
     */
    private Long notifyTagId;

    /**
     * 标签id
     */
    private Long userTagId;

    /**
     * 模板id
     */
    private Long templateId;

	public Long getNotifyTagId() {
		return notifyTagId;
	}

	public void setNotifyTagId(Long notifyTagId) {
		this.notifyTagId = notifyTagId;
	}

	public Long getUserTagId() {
		return userTagId;
	}

	public void setUserTagId(Long userTagId) {
		this.userTagId = userTagId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	@Override
	public String toString() {
		return "NotifyTemplateTag{" +
				"notifyTagId=" + notifyTagId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",userTagId=" + userTagId +
				",templateId=" + templateId +
				'}';
	}
}
