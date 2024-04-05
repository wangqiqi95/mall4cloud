package com.mall4j.cloud.api.coupon.constant;

/**
 *
 */

public enum CouponActivityCentreSourceEnum {

    /**
     * 活动类型：1-限时调价 2-会员日活动调价 3-虚拟门店价
     */
    TIME_DISCOUNT_PRICE(1, "限时调价"),
    MEMBER_DAY_PRICE(2, "会员日活动调价"),
    STORE_INVITE_PRICE(3, "虚拟门店价")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    CouponActivityCentreSourceEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static CouponActivityCentreSourceEnum instance(Integer value) {
        CouponActivityCentreSourceEnum[] enums = values();
        for (CouponActivityCentreSourceEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
