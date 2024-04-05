package com.mall4j.cloud.distribution.constant;

/**
 * 佣金状态(0:待支付、1:待结算、2:已结算、-1:订单失效)
 * @author cl
 * @date 2021-08-18 17:01:56
 */
public enum DistributionUserIncomeStateEnum {

    /**
     * 待支付
     */
    UN_PAY(0, "待支付"),
    /**
     * 待结算
     */
    UN_COMMISSION(1, "待结算"),

    /**
     * 已结算
     */
    COMMISSION(2, "已结算"),

    /**
     * 已失效
     */
    INVALID(-1, "已失效"),
    ;

    private int value;
    private String desc;

    public Integer value() {
        return value;
    }
    public String desc() {
        return desc;
    }

    DistributionUserIncomeStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static DistributionUserIncomeStateEnum instance(Integer value) {
        DistributionUserIncomeStateEnum[] enums = values();
        for (DistributionUserIncomeStateEnum stateEnum : enums) {
            if (stateEnum.value().equals(value)) {
                return stateEnum;
            }
        }
        return null;
    }
}
