package com.mall4j.cloud.user.constant;


import lombok.Getter;

@Getter
public enum GroupAuthFlagEnum {


    STAFF_OPERATION(1),
    STAFF_CHECK(2),
    SYSTEM_OPERATION(3);

    private Integer authFlag;

    GroupAuthFlagEnum(Integer authFlag){
        this.authFlag = authFlag;
    }

}
