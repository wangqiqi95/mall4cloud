package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * DTO
 *
 * @author lhd
 * @date 2021-05-14 09:35:32
 */
public class NotifyTemplateDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息模板表")
    private Long templateId;

    @ApiModelProperty("通知类型")
    private Integer sendType;

    @ApiModelProperty("通知内容")
    private String message;

    @ApiModelProperty("通知方式集合用逗号分隔 1.短信 2.公众号 3.站内消息")
    private String notifyTypes;

    @ApiModelProperty("短信模板code")
    private String templateCode;

    @ApiModelProperty("公众号消息模板code")
    private String mpCode;

    @ApiModelProperty("消息发送类型 1.平台自行发送类型 2.商家")
    private Integer msgType;

    @ApiModelProperty("1启用 0禁用")
    private Integer status;

	/**
	 * 可用通知方式集合用逗号分隔 1.短信 2.公众号 3.站内消息
	 */
	private List<Integer> templateTypeList;

	/**
	 * tagId 会员标签
	 */
	private List<Long> tagIds;

	public List<Integer> getTemplateTypeList() {
		return templateTypeList;
	}

	public void setTemplateTypeList(List<Integer> templateTypeList) {
		this.templateTypeList = templateTypeList;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNotifyTypes() {
		return notifyTypes;
	}

	public void setNotifyTypes(String notifyTypes) {
		this.notifyTypes = notifyTypes;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getMpCode() {
		return mpCode;
	}

	public void setMpCode(String mpCode) {
		this.mpCode = mpCode;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Long> getTagIds() {
		return tagIds;
	}

	public void setTagIds(List<Long> tagIds) {
		this.tagIds = tagIds;
	}

	@Override
	public String toString() {
		return "NotifyTemplateDTO{" +
				"templateId=" + templateId +
				", sendType=" + sendType +
				", message='" + message + '\'' +
				", notifyTypes='" + notifyTypes + '\'' +
				", templateCode='" + templateCode + '\'' +
				", mpCode='" + mpCode + '\'' +
				", msgType=" + msgType +
				", status=" + status +
				", templateTypeList=" + templateTypeList +
				", tagIds=" + tagIds +
				'}';
	}
}
