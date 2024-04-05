package com.mall4j.cloud.api.coupon.constant;

/**
 * @author FrozenWatermelon
 * @date 2020/12/28
 */
public enum UserCouponStatus {
    /**
     * 0:失效
     */
    INVALID(0),

    /**
     * 1:有效
     */
    EFFECTIVE(1),

    /**
     * 使用过
     */
    USED(2)
    ;

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    UserCouponStatus(Integer value) {
        this.value = value;
    }
}
