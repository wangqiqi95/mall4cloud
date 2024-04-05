package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * DTO
 *
 * @author cl
 * @date 2021-05-20 11:09:53
 */
public class NotifyTemplateTagDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签消息通知关联表")
    private Long notifyTagId;

    @ApiModelProperty("标签id")
    private Long userTagId;

    @ApiModelProperty("模板id")
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
		return "NotifyTemplateTagDTO{" +
				"notifyTagId=" + notifyTagId +
				",userTagId=" + userTagId +
				",templateId=" + templateId +
				'}';
	}
}
