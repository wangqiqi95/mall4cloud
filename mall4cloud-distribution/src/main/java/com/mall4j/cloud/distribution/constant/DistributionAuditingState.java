package com.mall4j.cloud.distribution.constant;

/**
 * 分销员审核状态：0 未审核 1已通过 -1未通过
 * @author cl
 * @date 2021-08-14 10:40:55
 */
public enum DistributionAuditingState {
    /**
     * 未审核
     */
    UN_AUDIT(0, "未审核"),
    /**
     * 已通过
     */
    PASS(1, "已通过"),
    /**
     * 未通过
     */
    UN_PASS(-1,"未通过");

    private int value;
    private String desc;

    public Integer value() {
        return value;
    }
    public String desc() {
        return desc;
    }

    DistributionAuditingState(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static DistributionAuditingState instance(Integer value) {
        DistributionAuditingState[] enums = values();
        for (DistributionAuditingState stateEnum : enums) {
            if (stateEnum.value().equals(value)) {
                return stateEnum;
            }
        }
        return null;
    }
}
