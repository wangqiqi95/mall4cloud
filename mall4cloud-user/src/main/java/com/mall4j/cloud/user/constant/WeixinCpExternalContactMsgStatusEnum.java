package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum WeixinCpExternalContactMsgStatusEnum {

    NOT_SEND(0),
    FINISH_SEND(1),
    CANCEL_SEND(2);


    private Integer sendStatus;


    WeixinCpExternalContactMsgStatusEnum(Integer sendStatus){
        this.sendStatus = sendStatus;
    }

}
