package com.mall4j.cloud.user.constant.scoreConvert;

/**
 * 积分兑换 [是否删除]
 *
 * @author shijing
 * @date 2021-12-10 18:07:21
 */

public enum ScoreConvertDelEnum {
    /**
     * 0：未删除
     */
    NOT_DEL(0, "未删除"),

    /**
     * 1：已删除
     */
    DEL(1,"已删除")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    ScoreConvertDelEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ScoreConvertDelEnum instance(Integer value) {
        ScoreConvertDelEnum[] enums = values();
        for (ScoreConvertDelEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
