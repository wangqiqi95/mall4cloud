package com.mall4j.cloud.api.user.bo;

/**
 * 充值成功通知
 * @author FrozenWatermelon
 */
public class RechargeNotifyBO {

    private Long balanceLogId;

    private Integer payType;

    private Long payId;

    public RechargeNotifyBO() {
    }

    public RechargeNotifyBO(Long balanceLogId, Integer payType, Long payId) {
        this.balanceLogId = balanceLogId;
        this.payType = payType;
        this.payId = payId;
    }

    public Long getBalanceLogId() {
        return balanceLogId;
    }

    public void setBalanceLogId(Long balanceLogId) {
        this.balanceLogId = balanceLogId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    @Override
    public String toString() {
        return "RechargeNotifyBO{" +
                "rechargeLogId=" + balanceLogId +
                ", payType=" + payType +
                ", payId=" + payId +
                '}';
    }
}
