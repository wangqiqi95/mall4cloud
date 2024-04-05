package com.mall4j.cloud.distribution.constant;

/**
 * @Author lth
 * @Date 2021/8/10 10:34
 * ("上级奖励设置(0 关闭 1开启)")
 */
public enum ParentAwardSet {
    /**
     * 关闭
     */
    CLOSE(0),
    /**
     * 开启
     */
    OPEN(1)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    ParentAwardSet(Integer num) {
        this.num = num;
    }

    public static ParentAwardSet instance(Integer value) {
        ParentAwardSet[] enums = values();
        for (ParentAwardSet statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
