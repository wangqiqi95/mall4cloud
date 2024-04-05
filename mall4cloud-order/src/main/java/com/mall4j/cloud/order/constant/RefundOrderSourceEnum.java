package com.mall4j.cloud.order.constant;

/**
 * 订单来源
 *
 * @author Peter_Tan
 */
public enum RefundOrderSourceEnum {
    /**
     * 普通订单
     */
    ORDINARY(0, "普通订单"),
    /**
     * 直播订单
     */
    BROADCAST(1, "直播订单"),
    /**
     * 视频号3.0订单
     */
    VIDEO_THREE(2, "视频号3.0订单"),
    /**
     * 视频号4.0订单
     */
    VIDEO_FOUR(3, "视频号4.0订单");

    private final Integer orderSource;

    private final String orderSourceName;

    RefundOrderSourceEnum(Integer orderSource, String orderSourceName) {
        this.orderSource = orderSource;
        this.orderSourceName = orderSourceName;
    }

    public Integer value() {
        return orderSource;
    }

    public String orderSourceName() {
        return orderSourceName;
    }

    public static String getOrderSourceName(Integer value) {
        RefundOrderSourceEnum[] enums = values();
        for (RefundOrderSourceEnum refundStatus : enums) {
            if (refundStatus.value().equals(value)) {
                return refundStatus.orderSourceName;
            }
        }
        return null;
    }
}
