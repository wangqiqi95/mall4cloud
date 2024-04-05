package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum StaffCpMsgFinishStatusEnum {

    // 0-非最终发送
    NOT_VALUE(0),
    // 1-最终发送（完成）
    SUCCESS(1);

    private Integer finishStatus;

    StaffCpMsgFinishStatusEnum(Integer finishStatus){
        this.finishStatus = finishStatus;
    }

}
