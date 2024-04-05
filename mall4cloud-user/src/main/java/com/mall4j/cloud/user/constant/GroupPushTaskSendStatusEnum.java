package com.mall4j.cloud.user.constant;

import lombok.Getter;

/**
 * 	发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
 */
@Getter
public enum GroupPushTaskSendStatusEnum {
    // 0-未发送
    NOT_SENT(0),
    // 1-已发送
    SUCCESS(1),
    // 2-因客户不是好友导致发送失败
    NOT_ADD_FRIEND_FAIL(2),
    // 3-因客户已经收到其他群发消息导致发送失败
    RECEIVE_OTHER_MESSAGE_FAIL(3);

    private Integer sendStatus;

    GroupPushTaskSendStatusEnum(Integer sendStatus){
        this.sendStatus = sendStatus;
    }
}
