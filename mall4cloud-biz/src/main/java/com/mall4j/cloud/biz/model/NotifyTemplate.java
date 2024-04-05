package com.mall4j.cloud.biz.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 *
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
public class NotifyTemplate extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 消息模板表
     */
    private Long templateId;

    /**
     * 1.订单催付 2.付款成功通知 3.商家同意退款 4.商家拒绝退款 5.核销提醒  6.发货提醒  7.拼团失败提醒 8.拼团成功提醒 9.拼团开团提醒 10.会员升级提醒
	 * 11.退款临近超时提醒 102.确认收货提醒 103.买家发起退款提醒 104.买家已退货提醒
     */
    private Integer sendType;

    /**
     * 通知内容
     */
    private String message;

    /**
     * 通知方式集合用逗号分隔 1.短信 2.公众号 3.站内消息
     */
    private String notifyTypes;

    /**
     * 短信模板code
     */
    private String templateCode;

    /**
     * 公众号消息模板code
     */
    private String mpCode;

    /**
     * 0.自定义消息 1.为全部发送给用户消息，2.为发送给商家的
     */
    private Integer msgType;

    /**
     * 1启用 0禁用
     */
    private Integer status;

	/**
	 * 可用通知方式集合用逗号分隔 1.短信 2.公众号 3.站内消息
	 */
	private Boolean sms = false;

	private Boolean app= false;

	private Boolean sub= false;

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

	@Override
	public String toString() {
		return "NotifyTemplate{" +
				"templateId=" + templateId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",sendType=" + sendType +
				",message=" + message +
				",notifyTypes=" + notifyTypes +
				",templateCode=" + templateCode +
				",mpCode=" + mpCode +
				",msgType=" + msgType +
				",status=" + status +
				'}';
	}
}
