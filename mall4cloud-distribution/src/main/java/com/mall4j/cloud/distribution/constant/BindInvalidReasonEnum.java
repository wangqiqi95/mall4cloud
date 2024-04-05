package com.mall4j.cloud.distribution.constant;

/**
 * 失效原因
 * @author cl
 * @date 2021-08-14 13:27:17
 */
public enum BindInvalidReasonEnum {

    /**
     * 管理员更改
     */
    ADMIN(1,"管理员更改"),

    /**
     * 封禁
     */
    CLEAR(2, "封禁")
    ;

    private int value;
    private String desc;

    public Integer value() {
        return value;
    }
    public String desc() {
        return desc;
    }

    BindInvalidReasonEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static BindInvalidReasonEnum instance(Integer value) {
        BindInvalidReasonEnum[] enums = values();
        for (BindInvalidReasonEnum reasonEnum : enums) {
            if (reasonEnum.value().equals(value)) {
                return reasonEnum;
            }
        }
        return null;
    }
}
