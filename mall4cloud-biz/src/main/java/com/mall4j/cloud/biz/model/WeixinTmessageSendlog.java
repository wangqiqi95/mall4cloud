package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信模板消息推送日志
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 17:13:37
 */
public class WeixinTmessageSendlog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 接收用户openId
     */
    private String openId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 消息模板id
     */
    private String templateId;

    /**
     * 消息内容
     */
    private String dataJson;

    /**
     * 状态，0：成功，1，失败
     */
    private Integer status;

    /**
     * 公众号appId
     */
    private String appId;

	/**
	 * 本系统微信消息模板id
	 */
	private String tmessageId;

	/**
	 * 小程序appId
	 */
	private String maAppId;

	/**
	 * 跳小程序所需数据
	 */
	private String miniprogram;

	/**
	 * 跳转到小程序的具体页面路径
	 */
	private String pagepath;

	/**
	 * 微信推送消息接收状态
	 */
	private String wxStatus;

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

	public String getTmessageId() {
		return tmessageId;
	}

	public void setTmessageId(String tmessageId) {
		this.tmessageId = tmessageId;
	}

	public String getMaAppId() {
		return maAppId;
	}

	public void setMaAppId(String maAppId) {
		this.maAppId = maAppId;
	}

	public String getMiniprogram() {
		return miniprogram;
	}

	public void setMiniprogram(String miniprogram) {
		this.miniprogram = miniprogram;
	}

	public String getPagepath() {
		return pagepath;
	}

	public void setPagepath(String pagepath) {
		this.pagepath = pagepath;
	}

	public String getWxStatus() {
		return wxStatus;
	}

	public void setWxStatus(String wxStatus) {
		this.wxStatus = wxStatus;
	}

	@Override
	public String toString() {
		return "WeixinTmessageSendlog{" +
				"id=" + id +
				",openId=" + openId +
				",taskId=" + taskId +
				",msgId=" + msgId +
				",templateId=" + templateId +
				",dataJson=" + dataJson +
				",status=" + status +
				",appId=" + appId +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
