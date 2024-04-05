package com.mall4j.cloud.distribution.constant;

/**
 * 当前绑定关系
 * @author cl
 * @date 2021-08-14 13:25:50
 */
public enum BindStateEnum {
    /**
     * 失效
     */
    INVALID(-1, "失效"),
    /**
     * 预绑定
     */
    PRE_BIND(0,"预绑定"),
    /**
     * 生效
     */
    VALID(1, "生效")
    ;

    private int value;
    private String desc;

    public Integer value() {
        return value;
    }
    public String desc() {
        return desc;
    }

    BindStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static BindStateEnum instance(Integer value) {
        BindStateEnum[] enums = values();
        for (BindStateEnum stateEnum : enums) {
            if (stateEnum.value().equals(value)) {
                return stateEnum;
            }
        }
        return null;
    }
}
