package com.mall4j.cloud.distribution.constant;

/**
 * @author cl
 * @date 2021-08-14 10:40:55
 */
public enum DistributionUserStateEnum {
    /**
     * 永久封禁
     */
    PER_BAN(-1, "永久封禁"),

    /**
     * 待审核状态
     */
    WAIT_AUDIT(0,"待审核状态"),
    /**
     * 正常
     */
    NORMAL(1, "正常"),
    /**
     * 暂时封禁
     */
    BAN(2, "暂时封禁"),
    /**
     * 审核未通过
     */
    FAIL_AUDIT(3,"审核未通过"),
    ;

    private int value;
    private String desc;

    public Integer value() {
        return value;
    }
    public String desc() {
        return desc;
    }

    DistributionUserStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static DistributionUserStateEnum instance(Integer value) {
        DistributionUserStateEnum[] enums = values();
        for (DistributionUserStateEnum stateEnum : enums) {
            if (stateEnum.value().equals(value)) {
                return stateEnum;
            }
        }
        return null;
    }
}
