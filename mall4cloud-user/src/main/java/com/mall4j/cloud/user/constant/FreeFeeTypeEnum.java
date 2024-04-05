package com.mall4j.cloud.user.constant;

/**
 * 包邮类型[1.全平台包邮 2.自营店包邮]
 * @author Pineapple
 * @date 2021/5/10 14:50
 */
public enum FreeFeeTypeEnum {
    /**
     * 全平台包邮
     */
    ALL_PLATFORMS_FREE(1),

    /**
     * 自营店包邮
     */
    SELF_OPERATED_STORE_FREE(2)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    FreeFeeTypeEnum(Integer value) {
        this.value = value;
    }
}
