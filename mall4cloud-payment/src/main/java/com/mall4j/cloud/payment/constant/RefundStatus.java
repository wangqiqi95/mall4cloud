package com.mall4j.cloud.payment.constant;

/**
 * 退款状态
 * @author FrozenWatermelon
 */
public enum RefundStatus {

    /**
     * 未退款
     */
    UNREFUND(0),

    /**
     * 已退款
     */
    REFUNDED(1)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    RefundStatus(Integer num) {
        this.num = num;
    }

    public static RefundStatus instance(Integer value) {
        RefundStatus[] enums = values();
        for (RefundStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
