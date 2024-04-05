package com.mall4j.cloud.user.constant;


import lombok.Getter;

@Getter
public enum CommonRemoveFlagEnum {

    REMOVE(1),
    DONT_REMOVE(0);

    private Integer deleteFlag;

    CommonRemoveFlagEnum(Integer deleteFlag){
        this.deleteFlag = deleteFlag;
    }

}
