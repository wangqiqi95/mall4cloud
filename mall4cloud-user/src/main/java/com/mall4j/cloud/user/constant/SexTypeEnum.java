package com.mall4j.cloud.user.constant;

/**
 * @author cl
 * @date 2021-05-12 17:26:37
 */
public enum SexTypeEnum {

    /**
     * 女
     */
    FEMALE(0, "0"),

    /**
     * 男
     */
    MALE(1, "1")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    SexTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static SexTypeEnum instance(Integer value) {
        SexTypeEnum[] enums = values();
        for (SexTypeEnum typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
