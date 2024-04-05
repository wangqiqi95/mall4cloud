package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum TagGroupSingleStateEnum {

    YES(1),
    NO(0);

    private Integer singleState;

    TagGroupSingleStateEnum(Integer singleState){
        this.singleState = singleState;
    }
}
