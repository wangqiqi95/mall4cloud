package com.mall4j.cloud.group.constant;

import lombok.Getter;

@Getter
public enum RemoveStatusEnum {

    IS_REMOVE(1),
    NOT_REMOVE(0);

    private Integer removeStatus;

    RemoveStatusEnum(Integer removeStatus){
        this.removeStatus = removeStatus;
    }

}
