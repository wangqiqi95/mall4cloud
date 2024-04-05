package com.mall4j.cloud.api.multishop.bo;

/**
 * @author FrozenWatermelon
 * @date 2021/3/9
 */
public class OrderChangeShopWalletAmountBO {

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单项id
     */
    private Long orderItemId;

    /**
     * 退款单id
     */
    private Long refundId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 订单 or 订单项 实际支付金额
     */
    private Long actualTotal;

    /**
     * 申请退款金额
     */
    private Long refundAmount;

    /**
     * 平台补贴金额
     */
    private Long platformAllowanceAmount;

    /**
     * 平台抽成
     */
    private Long platformCommission;

    /**
     * 平台抽成变化金额
     */
    private Long changePlatformCommission;

    /**
     * 平台抽成比例
     */
    private Double rate;

    /**
     * 分销金额
     */
    private Long distributionAmount;

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Long getPlatformAllowanceAmount() {
        return platformAllowanceAmount;
    }

    public void setPlatformAllowanceAmount(Long platformAllowanceAmount) {
        this.platformAllowanceAmount = platformAllowanceAmount;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getPlatformCommission() {
        return platformCommission;
    }

    public void setPlatformCommission(Long platformCommission) {
        this.platformCommission = platformCommission;
    }

    public Long getChangePlatformCommission() {
        return changePlatformCommission;
    }

    public void setChangePlatformCommission(Long changePlatformCommission) {
        this.changePlatformCommission = changePlatformCommission;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(Long distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    @Override
    public String toString() {
        return "OrderChangeShopWalletAmountBO{" +
                "orderStatus=" + orderStatus +
                ", orderId=" + orderId +
                ", orderItemId=" + orderItemId +
                ", refundId=" + refundId +
                ", shopId=" + shopId +
                ", actualTotal=" + actualTotal +
                ", refundAmount=" + refundAmount +
                ", platformAllowanceAmount=" + platformAllowanceAmount +
                ", platformCommission=" + platformCommission +
                ", changePlatformCommission=" + changePlatformCommission +
                ", rate=" + rate +
                '}';
    }
}
