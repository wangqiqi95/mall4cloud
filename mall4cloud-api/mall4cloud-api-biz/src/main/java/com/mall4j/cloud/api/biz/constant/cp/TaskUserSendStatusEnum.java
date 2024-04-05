package com.mall4j.cloud.api.biz.constant.cp;

import lombok.Getter;

/**
 * 企业微信接口获取群发任务结果-客户状态
 **/
public enum TaskUserSendStatusEnum {
    // 0-未发送
    NOT_SENT(0,"未发送"),
    // 1-已发送
    SUCCESS(1,"已发送"),
    // 2-因客户不是好友导致发送失败
    NOT_ADD_FRIEND_FAIL(2,"因客户不是好友导致发送失败"),
    // 3-因客户已经收到其他群发消息导致发送失败
    RECEIVE_OTHER_MESSAGE_FAIL(3,"因客户已经收到其他群发消息导致发送失败");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    TaskUserSendStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        TaskUserSendStatusEnum[] enums = values();
        for (TaskUserSendStatusEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
