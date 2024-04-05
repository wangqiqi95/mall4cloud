package com.mall4j.cloud.api.order.bo;

/**
 * @author FrozenWatermelon
 * @date 2021/1/13
 */
public class RefundAmountWithOrderIdBO {

    private Long orderId;

    private Long refundAmount;

    private Long platformRefundAmount;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Long getPlatformRefundAmount() {
        return platformRefundAmount;
    }

    public void setPlatformRefundAmount(Long platformRefundAmount) {
        this.platformRefundAmount = platformRefundAmount;
    }

    @Override
    public String toString() {
        return "RefundAmountWithOrderIdBO{" +
                "orderId=" + orderId +
                ", refundAmount=" + refundAmount +
                ", platformRefundAmount=" + platformRefundAmount +
                '}';
    }
}
