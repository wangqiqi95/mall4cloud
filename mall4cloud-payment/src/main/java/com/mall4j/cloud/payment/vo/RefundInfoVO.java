package com.mall4j.cloud.payment.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 退款信息VO
 *
 * @author FrozenWatermelon
 * @date 2021-03-15 15:26:03
 */
public class RefundInfoVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    private Long index;

    @ApiModelProperty("退款单号")
    private Long refundId;

    @ApiModelProperty("关联的支付订单id")
    private Long orderId;

    @ApiModelProperty("关联的支付单id")
    private Long payId;

    @ApiModelProperty("退款状态")
    private Integer refundStatus;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("回调内容")
    private String callbackContent;

    @ApiModelProperty("回调时间")
    private Date callbackTime;

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Long getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Long refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
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

	@Override
	public String toString() {
		return "RefundInfoVO{" +
				"index=" + index +
				", refundId=" + refundId +
				", orderId=" + orderId +
				", payId=" + payId +
				", refundStatus=" + refundStatus +
				", refundAmount=" + refundAmount +
				", payType=" + payType +
				", callbackContent='" + callbackContent + '\'' +
				", callbackTime=" + callbackTime +
				'}';
	}
}
