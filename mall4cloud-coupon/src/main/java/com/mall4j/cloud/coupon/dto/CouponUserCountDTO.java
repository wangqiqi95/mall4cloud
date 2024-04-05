package com.mall4j.cloud.coupon.dto;

/**
 * 用户已领优惠券的数量
 * @author cl
 * @date 2021-06-07 14:24:25
 */
public class CouponUserCountDTO {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * 用户已领取 优惠券couponId的数量
     */
    private Integer num;

    /**
     * 领取优惠券的总数
     */
    private Integer receiveNum;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(Integer receiveNum) {
        this.receiveNum = receiveNum;
    }

    @Override
    public String toString() {
        return "CouponUserCountDTO{" +
                "userId=" + userId +
                ", couponId=" + couponId +
                ", num=" + num +
                ", receiveNum=" + receiveNum +
                '}';
    }
}
