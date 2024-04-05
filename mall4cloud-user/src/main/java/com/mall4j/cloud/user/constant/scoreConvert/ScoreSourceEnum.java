package com.mall4j.cloud.user.constant.scoreConvert;

/**
 * 积分扣减来源类型
 *
 * @author shijing
 */

public enum ScoreSourceEnum {
    /**
     * 0：积分活动相关门店
     */
    SCORE_COUPON(0, "积分换券"),

    /**
     * 1：后台人工调整
     */
    SYSTEM(1, "后台人工调整"),

    /**
     * 2：抽奖活动扣减积分
     */
    SCORE_DRAW(2, "抽奖活动扣减积分"),

    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    ScoreSourceEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ScoreSourceEnum instance(Integer value) {
        ScoreSourceEnum[] enums = values();
        for (ScoreSourceEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static ScoreSourceEnum instance(String desc) {
        ScoreSourceEnum[] enums = values();
        for (ScoreSourceEnum statusEnum : enums) {
            if (statusEnum.desc.equals(desc)) {
                return statusEnum;
            }
        }
        return null;
    }

}
