package com.mall4j.cloud.distribution.constant;

/**
 * 封禁状态
 * @author cl
 * @date 2021-08-14 13:25:50
 */
public enum DistributionBanStateEnum {
    /**
     * 正常
     */
    INVALID(1, "正常"),
    /**
     * 暂时封禁
     */
    PRE_BIND(2,"暂时封禁"),
    /**
     * 永久封禁
     */
    VALID(-1, "永久封禁")
    ;

    private int value;
    private String desc;

    public Integer value() {
        return value;
    }
    public String desc() {
        return desc;
    }

    DistributionBanStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static DistributionBanStateEnum instance(Integer value) {
        DistributionBanStateEnum[] enums = values();
        for (DistributionBanStateEnum stateEnum : enums) {
            if (stateEnum.value().equals(value)) {
                return stateEnum;
            }
        }
        return null;
    }
}
