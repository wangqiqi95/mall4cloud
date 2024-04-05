package com.mall4j.cloud.user.constant;

/**
 * 期数类型(0:月,1:季,2:年)
 * @author Pineapple
 * @date 2021/5/10 14:51
 */
public enum TermTypeEnum {
    /**
     * 月
     */
    MONTH(0, 31),

    /**
     * 季
     */
    SEASON(1, 92),

    /**
     * 年
     */
    YEAR(2, 366)
    ;

    private final Integer value;

    private final Integer day;

    public Integer value() {
        return value;
    }

    public Integer getDay() {
        return day;
    }

    TermTypeEnum(Integer value, Integer day) {
        this.value = value;
        this.day = day;
    }

    public static TermTypeEnum instance(Integer value) {
        TermTypeEnum[] enums = values();
        for (TermTypeEnum termTypeEnum : enums) {
            if (termTypeEnum.value().equals(value)) {
                return termTypeEnum;
            }
        }
        return null;
    }
}
