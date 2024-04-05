package com.mall4j.cloud.order.constant;

/**
 * 退款类型
 *
 * @author FrozenWatermelon
 */
public enum RefundType {
    /**
     * 整单退款
     */
    ALL(1),
    /**
     * 单项退款
     */
    SINGLE(2);

    private final Integer num;

    RefundType(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static RefundType instance(Integer value) {
        RefundType[] enums = values();
        for (RefundType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
