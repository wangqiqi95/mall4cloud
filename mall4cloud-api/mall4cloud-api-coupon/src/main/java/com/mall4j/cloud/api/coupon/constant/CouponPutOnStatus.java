package com.mall4j.cloud.api.coupon.constant;

/**
 * 优惠券投放状态(-1:取消投放 0:等待投放 1:投放 2:违规下架 3:等待审核)
 * @author cl
 */
public enum CouponPutOnStatus {

    /**
     * 取消投放
     */
    CANCEL(-1),

    /**
     * 等待投放
     */
    WAIT_PLACED(0),

    /**
     * 投放
     */
    PLACED(1),

    /**
     * 违规下架
     */
    OFF_SHELVES(2),

    /**
     * 等待审核
     */
    WAIT_AUDIT(3)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    CouponPutOnStatus(Integer num){
        this.num = num;
    }

    public static CouponPutOnStatus instance(Integer value) {
        CouponPutOnStatus[] enums = values();
        for (CouponPutOnStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
