package com.mall4j.cloud.user.constant;


public enum UserJourneyEnum {
    /**
     * 电话
     */
    PHONE(1),

    /**
     * 邮箱
     */
    MAILBOX(2),
    /**
     * 企微会话
     */
    CP_CHATS(3),
    /**
     * 短信
     */
    SMS(4),
    /**
     * 跟进记录
     */
    FOLLOW_UP(5),
    /**
     * 美恰
     */
    MEIQIA(6),
    /**
     * 修改跟进
     */
    UPDATE_FOLLOW_UP(7),
    /**
     * 行为轨迹
     */
    BEHAVIORAL(8)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    UserJourneyEnum(Integer value) {
        this.value = value;
    }

    public static UserJourneyEnum instance(Integer value) {
        UserJourneyEnum[] enums = values();
        for (UserJourneyEnum typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
