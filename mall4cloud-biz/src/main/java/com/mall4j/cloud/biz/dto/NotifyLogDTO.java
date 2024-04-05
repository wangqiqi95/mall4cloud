package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * DTO
 *
 * @author lhd
 * @date 2021-05-14 09:35:32
 */
public class NotifyLogDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息记录表")
    private Long logId;

    @ApiModelProperty("用户昵称or手机号")
    private String nickName;

    @ApiModelProperty("发送手机号")
    private String userMobile;

    @ApiModelProperty("通知的用户id")
    private String remindId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("关联的订单id(等级)")
    private Long bizId;

    @ApiModelProperty("通知类型 1.短信发送 2.公众号订阅消息 3.站内消息")
    private Integer remindType;

    @ApiModelProperty("通知模板")
    private Long templateId;

    @ApiModelProperty("1.订单催付 2.付款成功通知 3.商家同意退款 4.商家拒绝退款 5.核销提醒 6.发货提醒 7.拼团失败提醒 8.拼团成功提醒 9.拼团开团提醒 10.会员升级提醒 101.退款临近超时提醒 102.确认收货提醒 103.买家发起退款提醒 104.买家已退货提醒")
    private Integer sendType;

    @ApiModelProperty("通知内容")
    private String message;

    @ApiModelProperty("是否阅读 1已读 0未读")
    private Integer status;

	/**
	 * 开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
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
		return "NotifyLogDTO{" +
				"logId=" + logId +
				", nickName='" + nickName + '\'' +
				", userMobile='" + userMobile + '\'' +
				", remindId='" + remindId + '\'' +
				", shopId=" + shopId +
				", shopName='" + shopName + '\'' +
				", bizId=" + bizId +
				", remindType=" + remindType +
				", templateId=" + templateId +
				", sendType=" + sendType +
				", message='" + message + '\'' +
				", status=" + status +
				", startTime=" + startTime +
				", endTime=" + endTime +
				'}';
	}
}
