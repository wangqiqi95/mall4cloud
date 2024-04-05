package com.mall4j.cloud.user.constant;

/**
 * 付费会员，是否可以招募会员；1可以招募，0停止招募
 * @author cl
 * @date 2021-07-26 17:21:04
 */
public enum RecruitStatusEnum {

    /**
     * 停止招募
     */
    STOP_RECRUIT(0),

    /**
     * 可以招募
     */
    ALLOW_RECRUIT(1)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    RecruitStatusEnum(Integer value) {
        this.value = value;
    }
}
