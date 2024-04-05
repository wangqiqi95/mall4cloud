package com.mall4j.cloud.api.coupon.dto;

/**
 * @author FrozenWatermelon
 * @date 2020/12/23
 */
public class LockCouponDTO {

    /**
     * 订单id
     */
    private String orderIds;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 用户优惠券id
     */
    private Long couponUserId;

    /**
     * 优惠金额
     */
    private Long reduceAmount;

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Long reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Long getCouponUserId() {
        return couponUserId;
    }

    public void setCouponUserId(Long couponUserId) {
        this.couponUserId = couponUserId;
    }

    @Override
    public String toString() {
        return "LockCouponDTO{" +
                "orderIds='" + orderIds + '\'' +
                ", couponId=" + couponId +
                ", couponUserId=" + couponUserId +
                ", reduceAmount=" + reduceAmount +
                '}';
    }
}
