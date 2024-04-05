package com.mall4j.cloud.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 退款支付结算单据VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public class OrderRefundSettlementVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("退款结算单据id")
    private Long settlementId;

    @ApiModelProperty("订单支付单号")
    private Long payId;

    @ApiModelProperty("订单编号")
    private Long orderId;

    @ApiModelProperty("退款单编号（本系统退款单号）")
    private Long refundId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("微信/支付宝退款单号（支付平台退款单号）")
    private String bizRefundNo;

    @ApiModelProperty("支付方式(1:微信支付 2支付宝支付)")
    private Integer payType;

    @ApiModelProperty("退款状态(1:申请中 2：已完成 -1失败)")
    private Integer refundStatus;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("订单总额")
    private Long orderTotalAmount;

    @ApiModelProperty("版本号")
    private Integer version;

	public Long getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Long settlementId) {
		this.settlementId = settlementId;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBizRefundNo() {
		return bizRefundNo;
	}

	public void setBizRefundNo(String bizRefundNo) {
		this.bizRefundNo = bizRefundNo;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
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

	public Long getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(Long orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "OrderRefundSettlementVO{" +
				"settlementId=" + settlementId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",payId=" + payId +
				",orderId=" + orderId +
				",refundId=" + refundId +
				",userId=" + userId +
				",bizRefundNo=" + bizRefundNo +
				",payType=" + payType +
				",refundStatus=" + refundStatus +
				",refundAmount=" + refundAmount +
				",orderTotalAmount=" + orderTotalAmount +
				",version=" + version +
				'}';
	}
}
