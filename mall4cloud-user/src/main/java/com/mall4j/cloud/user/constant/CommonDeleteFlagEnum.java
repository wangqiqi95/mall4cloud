package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum CommonDeleteFlagEnum {

    IS_REMOVE(1),
    DONT_REMOVE(0);

    private Integer deleteFlag;

    CommonDeleteFlagEnum(Integer deleteFlag){
        this.deleteFlag = deleteFlag;
    }

}
