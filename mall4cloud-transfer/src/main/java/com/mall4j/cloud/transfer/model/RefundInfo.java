package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 退款信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:10
 */
public class RefundInfo extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 退款单号
     */
    private Long refundId;

    /**
     * 关联的支付订单id
     */
    private Long orderId;

    /**
     * 关联的支付单id
     */
    private Long payId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 退款金额
     */
    private Long refundAmount;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 回调内容
     */
    private String callbackContent;

    /**
     * 回调时间
     */
    private Date callbackTime;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
		return "RefundInfo{" +
				"refundId=" + refundId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",orderId=" + orderId +
				",payId=" + payId +
				",userId=" + userId +
				",refundStatus=" + refundStatus +
				",refundAmount=" + refundAmount +
				",payType=" + payType +
				",callbackContent=" + callbackContent +
				",callbackTime=" + callbackTime +
				'}';
	}
}
