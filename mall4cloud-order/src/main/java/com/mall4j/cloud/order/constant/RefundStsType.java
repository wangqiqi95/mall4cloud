package com.mall4j.cloud.order.constant;

/**
 * @author FrozenWatermelon
 */
public enum RefundStsType {
    /**
     * 待审核
     */
    PROCESS(1),
    /**
     * 同意
     */
    AGREE(2),
    /**
     * 不同意
     */
    DISAGREE(3);

    private final Integer num;

    RefundStsType(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static RefundStsType instance(Integer value) {
        RefundStsType[] enums = values();
        for (RefundStsType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
