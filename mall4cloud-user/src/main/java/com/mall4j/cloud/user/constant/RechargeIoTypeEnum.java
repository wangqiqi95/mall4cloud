package com.mall4j.cloud.user.constant;

/**
 * 充值 收入 支出
 * @author cl
 * @date 2021-4-28 10:04:25
 */
public enum RechargeIoTypeEnum {
    /**
     * 0.支出
     */
    EXPENDITURE(0),
    /**
     * 1. 收入
     */
    INCOME(1),
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    RechargeIoTypeEnum(Integer num){
        this.num = num;
    }

    public static RechargeIoTypeEnum instance(Integer value) {
        RechargeIoTypeEnum[] enums = values();
        for (RechargeIoTypeEnum typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
