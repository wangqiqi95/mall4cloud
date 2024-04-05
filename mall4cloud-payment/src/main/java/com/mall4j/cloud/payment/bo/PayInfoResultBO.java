package com.mall4j.cloud.payment.bo;

/**
 * 支付后，微信/支付宝返回的一些基础数据
 * @author FrozenWatermelon
 * @date 2020/12/25
 */
public class PayInfoResultBO {

	/**
	 * 商城支付单号
	 */
	private Long payId;

	/**
	 * 第三方订单流水号
	 */
	private String bizPayNo;

	/**
	 * 是否支付成功
	 */
	private Boolean paySuccess;

	/**
	 * 支付成功的标记
	 */
	private String successString;
	/**
	 * 本次支付关联的多个订单号
	 */
	private String orderIds;

	/**
	 * 支付金额
	 */
	private Long payAmount;

	private String orderNumber;

	/**
	 * 回调内容
	 */
	private String callbackContent;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public String getBizPayNo() {
		return bizPayNo;
	}

	public void setBizPayNo(String bizPayNo) {
		this.bizPayNo = bizPayNo;
	}

	public Boolean getPaySuccess() {
		return paySuccess;
	}

	public void setPaySuccess(Boolean paySuccess) {
		this.paySuccess = paySuccess;
	}

	public String getSuccessString() {
		return successString;
	}

	public void setSuccessString(String successString) {
		this.successString = successString;
	}

	public Long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	public String getCallbackContent() {
		return callbackContent;
	}

	public void setCallbackContent(String callbackContent) {
		this.callbackContent = callbackContent;
	}

	@Override
	public String toString() {
		return "PayInfoResultBO{" +
				"payId=" + payId +
				", bizPayNo='" + bizPayNo + '\'' +
				", paySuccess=" + paySuccess +
				", successString='" + successString + '\'' +
				", payAmount=" + payAmount +
				", callbackContent='" + callbackContent + '\'' +
				'}';
	}
}
