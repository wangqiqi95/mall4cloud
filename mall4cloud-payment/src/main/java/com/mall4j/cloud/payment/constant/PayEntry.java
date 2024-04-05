package com.mall4j.cloud.payment.constant;

/**
 * 支付入口 0订单 1充值 2开通会员
 * @author FrozenWatermelon
 */
public enum PayEntry {

    /**
     * 订单
     */
    ORDER(0),

    /**
     * 充值
     */
    RECHARGE(1),

    /**
     * 开通会员
     */
    VIP(2),

    COUPON_PACK(3)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    PayEntry(Integer num) {
        this.num = num;
    }

    public static PayEntry instance(Integer value) {
        PayEntry[] enums = values();
        for (PayEntry statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
