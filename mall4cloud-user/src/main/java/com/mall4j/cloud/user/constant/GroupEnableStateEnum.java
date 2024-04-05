package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum GroupEnableStateEnum {

    OFF(0),
    ON(1);

    private Integer enableState;

    GroupEnableStateEnum(Integer enableState){
        this.enableState = enableState;
    }

}
