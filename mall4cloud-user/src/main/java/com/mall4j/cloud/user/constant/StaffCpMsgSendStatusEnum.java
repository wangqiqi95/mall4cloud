package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum StaffCpMsgSendStatusEnum {

    // 0-未发送
    NOT_SENT(0),
    // 1-已发送
    SUCCESS(1),
    // 2已停止
    STOP(2);

    private Integer sendStatus;

    StaffCpMsgSendStatusEnum(Integer sendStatus){
        this.sendStatus = sendStatus;
    }

}
