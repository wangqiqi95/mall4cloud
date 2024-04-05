package com.mall4j.cloud.group.constant;


import lombok.Getter;

@Getter
public enum PopUpAdPushTypeEnum {

    ALL_TIME(1),
    CHOOSE_TIME(2);

    private Integer pushType;

    PopUpAdPushTypeEnum(Integer pushType){

        this.pushType = pushType;

    }
}

