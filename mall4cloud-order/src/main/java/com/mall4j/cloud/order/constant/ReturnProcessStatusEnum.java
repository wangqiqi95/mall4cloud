package com.mall4j.cloud.order.constant;

/**
 * @author FrozenWatermelon
 */
public enum ReturnProcessStatusEnum {

    /**
     * 退款申请中
     */
    APPLY(1),

    /**
     * 卖家处理退款
     */
    PROCESSING(2),

    /**
     * 买家已发货
     */
    CONSIGNMENT(3),

    /**
     * 卖家已收货
     */
    RECEIVE(4),

    /**
     * 退款成功
     */
    SUCCESS(5),

    /**
     * 退款关闭
     */
    FAIL(-1);

    private final Integer num;

    ReturnProcessStatusEnum(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static ReturnProcessStatusEnum instance(Integer value) {
        ReturnProcessStatusEnum[] enums = values();
        for (ReturnProcessStatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
