package com.mall4j.cloud.group.constant;


import lombok.Getter;


@Getter
public enum PopUpAdVisibleTypeEnum {

    ALL(1),
    CHOOSE(2);

    private Integer visibleType;


    PopUpAdVisibleTypeEnum(Integer visibleType){
        this.visibleType = visibleType;
    }

}
