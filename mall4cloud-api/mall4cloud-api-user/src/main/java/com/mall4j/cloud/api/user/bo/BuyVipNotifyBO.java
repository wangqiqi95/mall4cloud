package com.mall4j.cloud.api.user.bo;

/**
 * 购买vip
 * @author FrozenWatermelon
 */
public class BuyVipNotifyBO {

    private Long userLevelLogId;

    private Integer payType;

    private Long payId;

    public BuyVipNotifyBO() {
    }

    public BuyVipNotifyBO(Long userLevelLogId, Integer payType, Long payId) {
        this.userLevelLogId = userLevelLogId;
        this.payType = payType;
        this.payId = payId;
    }

    public Long getUserLevelLogId() {
        return userLevelLogId;
    }

    public void setUserLevelLogId(Long userLevelLogId) {
        this.userLevelLogId = userLevelLogId;
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
        return "BuyVipNotifyBO{" +
                "userLevelLogId=" + userLevelLogId +
                ", payType=" + payType +
                ", payId=" + payId +
                '}';
    }
}
