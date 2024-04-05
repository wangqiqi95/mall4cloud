package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 微信模板消息推送日志DTO
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 17:13:37
 */
public class WeixinTmessageSendlogDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("接收用户openId")
    private String openId;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("消息ID")
    private String msgId;

    @ApiModelProperty("消息模板id")
    private String templateId;

    @ApiModelProperty("消息内容")
    private String dataJson;

    @ApiModelProperty("状态，0：成功，1，失败")
    private Integer status;

    @ApiModelProperty("")
    private String appId;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("删除标识0-正常,1-已删除")
    private Integer delFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "WeixinTmessageSendlogDTO{" +
				"id=" + id +
				",openId=" + openId +
				",taskId=" + taskId +
				",msgId=" + msgId +
				",templateId=" + templateId +
				",dataJson=" + dataJson +
				",status=" + status +
				",appId=" + appId +
				",createBy=" + createBy +
				",updateBy=" + updateBy +
				",delFlag=" + delFlag +
				'}';
	}
}
