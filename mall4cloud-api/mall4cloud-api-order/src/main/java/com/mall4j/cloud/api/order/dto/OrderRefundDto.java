package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class OrderRefundDto {

	/**
	 * 退款ID
	 */
	@NotNull(message = "退款ID不能为空(对应本系统refundNumber)")
	@ApiModelProperty(value = "小程序商城退款订单号")
	private String refundId;

	/**
	 * 处理状态（2:同意，3:不同意）
	 */
	@NotNull(message = "处理状态不能为空")
	@ApiModelProperty(value = "处理状态（2:同意，3:不同意）")
	private Integer refundSts;

	/**
	 * 退款原因
	 */
	@ApiModelProperty(value = "原因备注（退货失败原因，例如:商品破损）")
	private String rejectMessage;

	/**
	 * 是否收到货
	 */
	@NotNull(message = "是否收到货不能为空")
	@ApiModelProperty(value = "是否收到货")
	private Boolean isReceived;

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public Integer getRefundSts() {
		return refundSts;
	}

	public void setRefundSts(Integer refundSts) {
		this.refundSts = refundSts;
	}

	public String getRejectMessage() {
		return rejectMessage;
	}

	public void setRejectMessage(String rejectMessage) {
		this.rejectMessage = rejectMessage;
	}

	public Boolean getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(Boolean received) {
		isReceived = received;
	}

	@Override
	public String toString() {
		return "OrderRefundDto{" +
				"refundId=" + refundId +
				", refundSts=" + refundSts +
				", rejectMessage='" + rejectMessage + '\'' +
				", isReceived=" + isReceived +
				'}';
	}
}
