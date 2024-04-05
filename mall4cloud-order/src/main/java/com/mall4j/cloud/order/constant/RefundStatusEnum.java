package com.mall4j.cloud.order.constant;

/**
 * 退款状态
 *
 * @author FrozenWatermelon
 */
public enum RefundStatusEnum {
    /**
     * 申请退款
     */
    APPLY(1, "申请退款"),
    /**
     * 退款成功
     */
    SUCCEED(2, "退款成功"),
    /**
     * 部分退款成功
     */
    PARTIAL_SUCCESS(3, "部分退款成功"),
    /**
     * 退款失败
     */
    DISAGREE(4, "退款失败");

    private final Integer num;

    private final String refundName;

    RefundStatusEnum(Integer num, String refundName) {
        this.num = num;
        this.refundName = refundName;
    }

    public Integer value() {
        return num;
    }

    public String refundName() {
        return refundName;
    }

    public static String getRefundName(Integer value) {
        RefundStatusEnum[] enums = values();
        for (RefundStatusEnum refundStatus : enums) {
            if (refundStatus.value().equals(value)) {
                return refundStatus.refundName;
            }
        }
        return null;
    }
}
