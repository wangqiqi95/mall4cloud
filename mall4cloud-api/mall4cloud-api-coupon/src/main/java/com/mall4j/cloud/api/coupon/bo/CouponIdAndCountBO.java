package com.mall4j.cloud.api.coupon.bo;

/**
 * 优惠券id和数量
 * @author FrozenWatermelon
 */
public class CouponIdAndCountBO {

    private Long couponId;

    private Integer count;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CouponIdAndCountBO{" +
                "couponId=" + couponId +
                ", count=" + count +
                '}';
    }
}
