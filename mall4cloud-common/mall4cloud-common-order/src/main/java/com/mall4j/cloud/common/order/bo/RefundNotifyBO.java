package com.mall4j.cloud.common.order.bo;

/**
 * 订单退款成功通知
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
public class RefundNotifyBO {

    private Long orderId;

    private Long payId;

    private Long refundId;

    private String refundNumber;

    private Long refundAmount;

    /**
     * 是否为未成团而退款的团购订单
     */
    private Integer unSuccessGroupOrder;

    public String getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(String refundNumber) {
        this.refundNumber = refundNumber;
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

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Integer getUnSuccessGroupOrder() {
        return unSuccessGroupOrder;
    }

    public void setUnSuccessGroupOrder(Integer unSuccessGroupOrder) {
        this.unSuccessGroupOrder = unSuccessGroupOrder;
    }

    @Override
    public String toString() {
        return "RefundNotifyBO{" +
                "orderId=" + orderId +
                ", payId=" + payId +
                ", refundId=" + refundId +
                ", refundAmount=" + refundAmount +
                ", groupOrder=" + unSuccessGroupOrder +
                '}';
    }
}
