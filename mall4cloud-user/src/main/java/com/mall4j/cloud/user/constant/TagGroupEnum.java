package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum TagGroupEnum {

    MANUAL(1),
    GUIDE(2);

    private Integer value;

    TagGroupEnum(Integer value){
        this.value = value;
    }

}
