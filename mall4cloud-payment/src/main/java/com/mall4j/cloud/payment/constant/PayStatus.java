package com.mall4j.cloud.payment.constant;

/**
 * 支付状态
 * @author FrozenWatermelon
 */
public enum PayStatus {
    
    /**
     * 收钱吧订单已取消或者操作失败
     */
    CANCELED_OR_CANCELED(-2),
    /**
     * 直接进行退款
     */
    REFUND(-1),

    /**
     * 未支付
     */
    UNPAY(0),

    /**
     * 已支付
     */
    PAYED(1)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    PayStatus(Integer num) {
        this.num = num;
    }

    public static PayStatus instance(Integer value) {
        PayStatus[] enums = values();
        for (PayStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
