package com.mall4j.cloud.user.constant;


import lombok.Getter;

@Getter
public enum WeChatTypeEnum {

    YES(1),
    NO(0);

    private Integer weChatState;

    WeChatTypeEnum(Integer weChatState){
        this.weChatState = weChatState;
    }
}
