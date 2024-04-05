package com.mall4j.cloud.api.order.bo;

/**
 * @author FrozenWatermelon
 * @date 2021/1/13
 */
public class DistributionAmountWithOrderIdBO {

    private Long orderId;

    private Long distributionAmount;

    private Long distributionParentAmount;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(Long distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public Long getDistributionParentAmount() {
        return distributionParentAmount;
    }

    public void setDistributionParentAmount(Long distributionParentAmount) {
        this.distributionParentAmount = distributionParentAmount;
    }

    @Override
    public String toString() {
        return "DistributionAmountWithOrderIdBO{" +
                "orderId=" + orderId +
                ", distributionAmount=" + distributionAmount +
                ", distributionParentAmount=" + distributionParentAmount +
                '}';
    }
}
