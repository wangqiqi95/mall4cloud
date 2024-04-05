package com.mall4j.cloud.user.constant.scoreConvert;

/**
 * 积分兑换 [兑换类型]
 *
 * @author shijing
 * @date 2021-12-10 18:07:21
 */

public enum ScoreConvertTypeEnum {
    /**
     * 0：启用
     */
    BARTER(0, "积分换物"),

    /**
     * 1：停用
     */
    COUPON(1,"积分换券")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    ScoreConvertTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ScoreConvertTypeEnum instance(Integer value) {
        ScoreConvertTypeEnum[] enums = values();
        for (ScoreConvertTypeEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
