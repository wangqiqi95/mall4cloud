package com.mall4j.cloud.api.user.bo;

/**
 * 退款成功通知
 * @author FrozenWatermelon
 */
public class BalanceRefundBO {

    private Long refundId;

    private Long changeBalance;

    private Long payId;

    private Long userId;

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public Long getChangeBalance() {
        return changeBalance;
    }

    public void setChangeBalance(Long changeBalance) {
        this.changeBalance = changeBalance;
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

    @Override
    public String toString() {
        return "BalanceRefundBO{" +
                "refundId=" + refundId +
                ", changeBalance=" + changeBalance +
                ", payId=" + payId +
                ", userId=" + userId +
                '}';
    }
}
