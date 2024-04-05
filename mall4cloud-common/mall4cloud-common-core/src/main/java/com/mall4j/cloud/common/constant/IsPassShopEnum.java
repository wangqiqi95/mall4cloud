package com.mall4j.cloud.common.constant;

/**
 * @Author lth
 * @Date 2021/5/12 11:19
 */
public enum IsPassShopEnum {
    /**
     * 管理员
     */
    YES(1),
    /**
     * 用户
     */
    NO(0),
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    public Integer getValue() {
        return value;
    }


    IsPassShopEnum(Integer value) {
        this.value = value;
    }

    public static IsPassShopEnum instance(Integer value) {
        IsPassShopEnum[] enums = values();
        for (IsPassShopEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
