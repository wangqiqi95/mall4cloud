package com.mall4j.cloud.api.multishop.bo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商家钱包信息BO
 * @author cl
 * @date 2021-08-19 11:04:45
 */
public class ShopWalletBO {

    @ApiModelProperty("店铺钱包id")
    private Long shopWalletId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("订单号")
    private Long orderId;

    @ApiModelProperty("未结算金额（用户支付）")
    private Long unsettledAmount;

    @ApiModelProperty("已结算金额（用户确认收货后，可以提现）")
    private Long settledAmount;

    @ApiModelProperty("冻结金额（用户确认收货）")
    private Long freezeAmount;

    @ApiModelProperty("累积结算金额")
    private Long totalSettledAmount;

    public Long getShopWalletId() {
        return shopWalletId;
    }

    public void setShopWalletId(Long shopWalletId) {
        this.shopWalletId = shopWalletId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUnsettledAmount() {
        return unsettledAmount;
    }

    public void setUnsettledAmount(Long unsettledAmount) {
        this.unsettledAmount = unsettledAmount;
    }

    public Long getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Long settledAmount) {
        this.settledAmount = settledAmount;
    }

    public Long getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public Long getTotalSettledAmount() {
        return totalSettledAmount;
    }

    public void setTotalSettledAmount(Long totalSettledAmount) {
        this.totalSettledAmount = totalSettledAmount;
    }

    @Override
    public String toString() {
        return "ShopWalletBO{" +
                "shopWalletId=" + shopWalletId +
                ", shopId=" + shopId +
                ", orderId=" + orderId +
                ", unsettledAmount=" + unsettledAmount +
                ", settledAmount=" + settledAmount +
                ", freezeAmount=" + freezeAmount +
                ", totalSettledAmount=" + totalSettledAmount +
                '}';
    }
}
