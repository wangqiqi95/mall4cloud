package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 微信公共回复内容表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:03
 */
public class WeixinAutoResponseDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("内容id")
    private String dateId;

    @ApiModelProperty("内容来源： 1消息自动回复 2关键词回复 3扫码回复 4关注默认回复 5关注门店回复")
    private Integer dateSrc;

    @ApiModelProperty("消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)")
    private String msgType;

    @ApiModelProperty("模板ID")
    private String templateId;

    @ApiModelProperty("关联模板名称")
    private String templateName;

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

	public String getDateId() {
		return dateId;
	}

	public void setDateId(String dateId) {
		this.dateId = dateId;
	}

	public Integer getDateSrc() {
		return dateSrc;
	}

	public void setDateSrc(Integer dateSrc) {
		this.dateSrc = dateSrc;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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
		return "WeixinAutoResponseDTO{" +
				"id=" + id +
				",dateId=" + dateId +
				",dateSrc=" + dateSrc +
				",msgType=" + msgType +
				",templateId=" + templateId +
				",templateName=" + templateName +
				",createBy=" + createBy +
				",updateBy=" + updateBy +
				",delFlag=" + delFlag +
				'}';
	}
}
