package com.mall4j.cloud.coupon.constant;

/**
 * 活动是否删除 [是否删除]
 *
 * @author shijing
 * @date 2021-1-5
 */

public enum ActivityDelEnum {
    /**
     * 0：未删除
     */
    NOT_DEL(0, "未删除"),

    /**
     * 1：已删除
     */
    DEL(1,"已删除")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    ActivityDelEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ActivityDelEnum instance(Integer value) {
        ActivityDelEnum[] enums = values();
        for (ActivityDelEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
