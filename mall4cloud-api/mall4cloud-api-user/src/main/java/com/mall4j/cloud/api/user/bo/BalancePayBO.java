package com.mall4j.cloud.api.user.bo;

/**
 * 充值成功通知
 * @author FrozenWatermelon
 */
public class BalancePayBO {

    private Long payId;

    private Long changeBalance;

    private Boolean isVip;

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public Long getChangeBalance() {
        return changeBalance;
    }

    public void setChangeBalance(Long changeBalance) {
        this.changeBalance = changeBalance;
    }

    public Boolean getIsVip() {
        return isVip;
    }

    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }

    @Override
    public String toString() {
        return "BalancePayBO{" +
                "payId=" + payId +
                ", changeBalance=" + changeBalance +
                ", isVip=" + isVip +
                '}';
    }
}
