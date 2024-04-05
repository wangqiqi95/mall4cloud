package com.mall4j.cloud.distribution.constant;

/**
 * @Author lth
 * @Date 2021/8/10 10:34
 * 奖励方式(0 按比例 1 按固定数值)
 */
public enum AwardMode {
    /**
     * 按比例
     */
    PROPORTION(0),
    /**
     * 按固定数值
     */
    FIXED_VALUE(1)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    AwardMode(Integer num) {
        this.num = num;
    }

    public static AwardMode instance(Integer value) {
        AwardMode[] enums = values();
        for (AwardMode statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
