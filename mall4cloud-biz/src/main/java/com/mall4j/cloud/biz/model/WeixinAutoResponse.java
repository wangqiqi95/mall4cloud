package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信公共回复内容表
 *
 * @author FrozenWatermelon
 * @date 2022-01-17 17:21:03
 */
public class WeixinAutoResponse extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 内容id
     */
    private String dataId;

    /**
     * 内容来源： 1消息自动回复 2关键词回复 3扫码回复 4关注默认回复 5关注门店回复
     */
    private Integer dataSrc;

    /**
     * 消息类型(text:文本消息,news:图文消息,voice:音频消息,video:视频消息,image:图片消息,wxma:小程序)
     */
    private String msgType;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 关联模板名称
     */
    private String templateName;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除标识0-正常,1-已删除
     */
    private Integer delFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Integer getDataSrc() {
		return dataSrc;
	}

	public void setDataSrc(Integer dataSrc) {
		this.dataSrc = dataSrc;
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

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
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

	@Override
	public String toString() {
		return "WeixinAutoResponse{" +
				"id=" + id +
				",dataId=" + dataId +
				",dataSrc=" + dataSrc +
				",msgType=" + msgType +
				",templateId=" + templateId +
				",templateName=" + templateName +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
