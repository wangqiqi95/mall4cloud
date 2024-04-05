package com.mall4j.cloud.user.constant;

/**
 * 标签类型 0手动 1条件 2系统
 * @author cl
 * @date 2021-05-18 14:02:42
 */
public enum TagTypeEnum {

    /**
     * 手动标签
     */
    MANUAL(0),

    /**
     * 条件标签
     */
    CONDITION(1),
    /**
     * 系统标签
     */
    SYSTEM(2)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    TagTypeEnum(Integer value) {
        this.value = value;
    }

    public static TagTypeEnum instance(Integer value) {
        TagTypeEnum[] enums = values();
        for (TagTypeEnum typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
