package com.mall4j.cloud.api.coupon.constant;


/**
 * 优惠券类型 1:代金券 2:折扣券 3:兑换券
 * @author FrozenWatermelon
 */
public enum CouponType {

    /**
     * 代金券
     */
    C2M(1),

    /**
     * 折扣券
     */
    C2D(2),

    /**
     * 兑换券
     */
    C2P(3),
    //抵用券
    CNM(0),
//    折扣券
    CND(1),
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    CouponType(Integer num){
        this.num = num;
    }

    public static CouponType instance(Integer value) {
        CouponType[] enums = values();
        for (CouponType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
