package com.mall4j.cloud.distribution.constant;

/**
 * @author cl
 * @date 2021-08-17 10:05:59
 */
public enum DistributionWithdrawCashStateEnum {
    /**
     * 申请中
     */
    APPLY(0, "申请中"),
    /**
     * 提现成功
     */
    CASH_SUCCESS(1, "提现成功"),
    /**
     * 拒绝提现
     */
    CASH_REJECT(2,"拒绝提现"),
    /**
     * 提现失败
     */
    CASH_FAIL(-1, "提现失败");

    private int value;
    private String desc;
    public Integer value() {
        return value;
    }
    public String desc() {
        return desc;
    }

    DistributionWithdrawCashStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static DistributionWithdrawCashStateEnum instance(Integer value) {
        DistributionWithdrawCashStateEnum[] enums = values();
        for (DistributionWithdrawCashStateEnum stateEnum : enums) {
            if (stateEnum.value().equals(value)) {
                return stateEnum;
            }
        }
        return null;
    }
}
