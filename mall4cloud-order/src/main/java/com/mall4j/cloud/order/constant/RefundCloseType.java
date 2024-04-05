package com.mall4j.cloud.order.constant;

/**
 * 退款关闭原因(1.买家撤销退款 2.卖家拒绝退款 3.退款申请超时被系统关闭)
 * @author FrozenWatermelon
 */
public enum RefundCloseType {
    /**
     * 买家撤销退款
     */
    CANCEL(1),
    /**
     * 卖家拒绝退款
     */
    DISAGREE(2),
    /**
     * 退款申请超时被系统关闭
     */
    TIME_OUT(3);

    private final Integer num;

    RefundCloseType(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static RefundCloseType instance(Integer value) {
        RefundCloseType[] enums = values();
        for (RefundCloseType refundHandleType : enums) {
            if (refundHandleType.value().equals(value)) {
                return refundHandleType;
            }
        }
        return null;
    }
}
