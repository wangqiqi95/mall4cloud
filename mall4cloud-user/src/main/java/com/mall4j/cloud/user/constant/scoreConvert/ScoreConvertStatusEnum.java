package com.mall4j.cloud.user.constant.scoreConvert;

/**
 * 积分兑换 [状态]
 *
 * @author shijing
 * @date 2021-12-10 18:07:21
 */

public enum ScoreConvertStatusEnum {
    /**
     * 0：启用
     */
    ENABLE(0, "启用"),

    /**
     * 1：停用
     */
    DISABLE(1,"停用")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    ScoreConvertStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ScoreConvertStatusEnum instance(Integer value) {
        ScoreConvertStatusEnum[] enums = values();
        for (ScoreConvertStatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
