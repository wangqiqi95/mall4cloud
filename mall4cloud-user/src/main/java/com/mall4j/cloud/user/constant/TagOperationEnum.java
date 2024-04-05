package com.mall4j.cloud.user.constant;


import lombok.Getter;

@Getter
public enum TagOperationEnum {

    ADD(1),
    REMOVE(2);

    private Integer operationState;

    TagOperationEnum(Integer operationStateEnum){
        this.operationState = operationStateEnum;
    }
}
