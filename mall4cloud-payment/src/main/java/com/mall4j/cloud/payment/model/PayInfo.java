package com.mall4j.cloud.payment.model;

import java.io.Serializable;
import java.util.Date;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 订单支付记录
 *
 * @author FrozenWatermelon
 * @date 2020-12-25 09:50:59
 */
public class PayInfo extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 支付单号
     */
    private Long payId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 外部订单流水号
     */
    private String bizPayNo;

	/**
	 * 本次支付关联的多个订单号
	 */
	private String orderIds;

    /**
     * 系统类型 见SysTypeEnum
     */
    private Integer sysType;

	/**
	 * 支付入口
	 */
	private Integer payEntry;

    /**
     * 支付方式 1 微信支付 2 支付宝 10 收钱吧轻POS支付
     */
    private Integer payType;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 支付积分
     */
    private Long payScore;

    /**
     * 支付金额
     */
    private Long payAmount;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 回调内容
     */
    private String callbackContent;

    /**
     * 回调时间
     */
    private Date callbackTime;

    /**
     * 确认时间
     */
    private Date confirmTime;
	
	/**
	 * 收钱吧轻POS的订单凭证，H5、微信小程序、微信小程序插件,刷脸终端的场景
	 */
	private String orderToken;
	
	/**
	 * 商城订单号
	 */
	private String orderNumber;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getOrderToken() {
		return orderToken;
	}
	
	public void setOrderToken(String orderToken) {
		this.orderToken = orderToken;
	}
	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBizPayNo() {
		return bizPayNo;
	}

	public void setBizPayNo(String bizPayNo) {
		this.bizPayNo = bizPayNo;
	}

	public Integer getSysType() {
		return sysType;
	}

	public void setSysType(Integer sysType) {
		this.sysType = sysType;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Long getPayScore() {
		return payScore;
	}

	public void setPayScore(Long payScore) {
		this.payScore = payScore;
	}

	public Long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCallbackContent() {
		return callbackContent;
	}

	public void setCallbackContent(String callbackContent) {
		this.callbackContent = callbackContent;
	}

	public Date getCallbackTime() {
		return callbackTime;
	}

	public void setCallbackTime(Date callbackTime) {
		this.callbackTime = callbackTime;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

	public Integer getPayEntry() {
		return payEntry;
	}

	public void setPayEntry(Integer payEntry) {
		this.payEntry = payEntry;
	}
	
	@Override
	public String toString() {
		return "PayInfo{" +
				"payId=" + payId +
				", userId=" + userId +
				", bizPayNo='" + bizPayNo + '\'' +
				", orderIds='" + orderIds + '\'' +
				", sysType=" + sysType +
				", payEntry=" + payEntry +
				", payType=" + payType +
				", payStatus=" + payStatus +
				", payScore=" + payScore +
				", payAmount=" + payAmount +
				", version=" + version +
				", callbackContent='" + callbackContent + '\'' +
				", callbackTime=" + callbackTime +
				", confirmTime=" + confirmTime +
				", orderToken='" + orderToken + '\'' +
				", orderNumber='" + orderNumber + '\'' +
				'}';
	}
}
