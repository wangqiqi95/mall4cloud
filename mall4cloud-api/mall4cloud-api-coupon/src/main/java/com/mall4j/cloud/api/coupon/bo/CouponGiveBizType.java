package com.mall4j.cloud.api.coupon.bo;


/**
 * 赠送优惠券来源业务类型
 * @author FrozenWatermelon
 */
public enum CouponGiveBizType {

    /**
     * 充值送优惠券
     */
    RECHARGE(0),


    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    CouponGiveBizType(Integer num){
        this.num = num;
    }

    public static CouponGiveBizType instance(Integer value) {
        CouponGiveBizType[] enums = values();
        for (CouponGiveBizType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
