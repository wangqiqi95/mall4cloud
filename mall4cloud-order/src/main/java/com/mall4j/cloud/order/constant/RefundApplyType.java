package com.mall4j.cloud.order.constant;

/**
 * 退款申请类型 1,仅退款,2退款退货
 *
 * @author FrozenWatermelon
 */
public enum RefundApplyType {
    /**
     * 仅退款
     */
    REFUND(1),
    /**
     * 退款退货
     */
    REFUND_AND_RETURNS(2);

    private final Integer num;

    RefundApplyType(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static RefundApplyType instance(Integer value) {
        RefundApplyType[] enums = values();
        for (RefundApplyType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
