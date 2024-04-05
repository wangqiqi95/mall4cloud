package com.mall4j.cloud.distribution.constant;

/**
 * 审核状态：0 未审核 1已通过 -1未通过
 * @author cl
 * @date 2021-08-16 14:06:36
 */
public enum DistributionAuditStateEnum {
    /**
     * 未通过
     */
    NO_PASSED(-1, "未通过"),
    /**
     * 未审核
     */
    UNAUDITED(0,"未审核"),
    /**
     * 已通过
     */
    PASSED(1, "已通过")
    ;

    private int value;
    private String desc;

    public Integer value() {
        return value;
    }
    public String desc() {
        return desc;
    }

    DistributionAuditStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static DistributionAuditStateEnum instance(Integer value) {
        DistributionAuditStateEnum[] enums = values();
        for (DistributionAuditStateEnum stateEnum : enums) {
            if (stateEnum.value().equals(value)) {
                return stateEnum;
            }
        }
        return null;
    }
}
