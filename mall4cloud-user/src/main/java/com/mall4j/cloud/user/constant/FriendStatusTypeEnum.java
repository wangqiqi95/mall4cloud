package com.mall4j.cloud.user.constant;

/**
 * 是否是好友类型[0.未加好友 1.已加好友]
 * @author Peter_Tan
 * @date 2023/03/22 14:50
 */
public enum FriendStatusTypeEnum {
    /**
     * 未加好友
     */
    NOT_FRIEND(0),

    /**
     * 已加好友
     */
    IS_FRIEND(1)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    FriendStatusTypeEnum(Integer value) {
        this.value = value;
    }
}
