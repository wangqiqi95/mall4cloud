package com.mall4j.cloud.coupon.constant;

import lombok.Getter;

@Getter
public enum ChooseMemberEventEnableEnum {

    ENABLE(1),
    DISABLE(0);

    private Integer enableStatus;

    ChooseMemberEventEnableEnum(Integer enableStatus){
        this.enableStatus = enableStatus;
    }
}
