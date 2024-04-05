package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * VO
 *
 * @author lhd
 * @date 2021-03-31 10:37:54
 */
public class NotifyLogVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息记录表")
    private Long logId;

    @ApiModelProperty("用户昵称or手机号")
    private String nickName;

    @ApiModelProperty("发送手机号")
    private String userMobile;

    @ApiModelProperty("通知的用户id")
    private String remindId;

    @ApiModelProperty("选择发送的店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

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
    @ApiModelProperty("关联的订单id(等级)")
	private Long bizId;

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
		return "NotifyLogVO{" +
				"logId=" + logId +
				", nickName='" + nickName + '\'' +
				", userMobile='" + userMobile + '\'' +
				", remindId='" + remindId + '\'' +
				", shopId=" + shopId +
				", shopName=" + shopName +
				", remindType=" + remindType +
				", templateId=" + templateId +
				", sendType=" + sendType +
				", message='" + message + '\'' +
				", status=" + status +
				", bizId=" + bizId +
				'}';
	}
}
