package com.mall4j.cloud.user.constant;

/**
 * 折扣范围[0.全平台 1.自营店]
 * @author Pineapple
 * @date 2021/5/10 14:47
 */
public enum DiscountRangeEnum {
    /**
     * 全平台
     */
    ALL_PLATFORMS(0),

    /**
     * 自营店
     */
    SELF_OPERATED_STORE(1)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    DiscountRangeEnum(Integer value) {
        this.value = value;
    }
}
