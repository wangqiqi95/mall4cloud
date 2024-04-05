package com.mall4j.cloud.api.user.bo;

/**
 * 用户购物抵扣积分
 * @author cl
 * @date 2021-07-17 15:18:02
 */
public class UserOrderScoreBo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 退款id
     */
    private Long refundId;

    /**
     * 订单使用积分
     */
    private Long orderScore;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getOrderScore() {
        return orderScore;
    }

    public void setOrderScore(Long orderScore) {
        this.orderScore = orderScore;
    }

    @Override
    public String toString() {
        return "UserOrderScoreBo{" +
                "userId=" + userId +
                ", orderId=" + orderId +
                ", refundId=" + refundId +
                ", orderScore=" + orderScore +
                '}';
    }
}
