package com.mall4j.cloud.user.constant;

/**
 * 用户状态
 * @author cl
 * @date 2021-05-11 13:13:00
 */
public enum UserStatusEnum {
    /**
     * 普通会员
     */
    DISABLE(0, "禁用"),

    /**
     * 付费会员
     */
    ENABLE(1,"正常")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    UserStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UserStatusEnum instance(Integer value) {
        UserStatusEnum[] enums = values();
        for (UserStatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
