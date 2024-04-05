package com.mall4j.cloud.coupon.constant;

/**
 * 优惠券活动 [类型]
 *
 * @author shijing
 * @date 2021-1-6
 */

public enum ActivityTypeEnum {
    /**
     * 0：启用
     */
    PUSH(0, "推券"),

    /**
     * 1：停用
     */
    RECEIVE(1,"领券"),
    /**
     * 1：停用
     */
    BUY(2,"买券")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    ActivityTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ActivityTypeEnum instance(Integer value) {
        ActivityTypeEnum[] enums = values();
        for (ActivityTypeEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
