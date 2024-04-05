package com.mall4j.cloud.biz.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 *
 *
 * @author FrozenWatermelon
 * @date 2021-01-16 15:01:14
 */
public class NotifyLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 消息记录表
     */
    private Long logId;

    /**
     * 用户昵称or手机号
     */
    private String nickName;

    /**
     * 发送手机号
     */
    private String userMobile;

    /**
     * 通知的用户id
     */
    private String remindId;

    /**
     * 通知的订单id
     */
    private Long bizId;

    /**
     * 选择发送的店铺id
     */
    private Long shopId;
    /**
     * 选择发送的店铺id
     */
    private String shopName;

    /**
     * 通知类型 1.短信发送 2.公众号订阅消息 3.站内消息
     */
    private Integer remindType;

    /**
     * 通知模板
     */
    private Long templateId;

    /**
     * 1.订单催付 2.付款成功通知 3.商家同意退款 4.商家拒绝退款 5.核销提醒 6.发货提醒 7.拼团失败提醒 8.拼团成功提醒 9.拼团开团提醒 10.会员升级提醒 101.退款临近超时提醒 102.确认收货提醒 103.买家发起退款提醒 104.买家已退货提醒
     */
    private Integer sendType;

    /**
     * 通知内容
     */
    private String message;

    /**
     * 是否阅读 1已读 0未读
     */
    private Integer status;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getRemindId() {
		return remindId;
	}

	public void setRemindId(String remindId) {
		this.remindId = remindId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getRemindType() {
		return remindType;
	}

	public void setRemindType(Integer remindType) {
		this.remindType = remindType;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "NotifyLog{" +
				"logId=" + logId +
				", nickName='" + nickName + '\'' +
				", userMobile='" + userMobile + '\'' +
				", remindId='" + remindId + '\'' +
				", bizId=" + bizId +
				", shopId=" + shopId +
				", shopName=" + shopName +
				", remindType=" + remindType +
				", templateId=" + templateId +
				", sendType=" + sendType +
				", message='" + message + '\'' +
				", status=" + status +
				'}';
	}
}
