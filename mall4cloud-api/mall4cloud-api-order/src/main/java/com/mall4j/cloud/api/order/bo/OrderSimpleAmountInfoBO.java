package com.mall4j.cloud.api.order.bo;

/**
 * @author FrozenWatermelon
 * @date 2020/12/30
 */
public class OrderSimpleAmountInfoBO {

    private Long orderId;

    private Long shopId;

    /**
     * 实际总值
     */
    private Long actualTotal;

    /**
     * 平台补贴金额
     */
    private Long platformAmount;


    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单关闭原因
     */
    private Integer closeType;

    /**
     * 分销金额
     */
    private Long distributionAmount;

    /**
     * 分销佣金状态
     */
    private Integer distributionStatus;

    /**
     * 平台佣金
     */
    private Long platformCommission;
    /**
     * 店铺优惠金额
     */
    private Long shopAmount;
    /**
     * 总计优惠金额
     */
    private Long reduceAmount;

    public Long getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Long reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Long getShopAmount() {
        return shopAmount;
    }

    public void setShopAmount(Long shopAmount) {
        this.shopAmount = shopAmount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Long getPlatformAmount() {
        return platformAmount;
    }

    public void setPlatformAmount(Long platformAmount) {
        this.platformAmount = platformAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCloseType() {
        return closeType;
    }

    public void setCloseType(Integer closeType) {
        this.closeType = closeType;
    }

    public Long getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(Long distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public Integer getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(Integer distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public Long getPlatformCommission() {
        return platformCommission;
    }

    public void setPlatformCommission(Long platformCommission) {
        this.platformCommission = platformCommission;
    }

    @Override
    public String toString() {
        return "OrderSimpleAmountInfoBO{" +
                "orderId=" + orderId +
                ", shopId=" + shopId +
                ", actualTotal=" + actualTotal +
                ", platformAmount=" + platformAmount +
                ", status=" + status +
                ", closeType=" + closeType +
                ", distributionAmount=" + distributionAmount +
                ", platformCommission=" + platformCommission +
                ", shopAmount=" + shopAmount +
                ", reduceAmount=" + reduceAmount +
                '}';
    }
}
