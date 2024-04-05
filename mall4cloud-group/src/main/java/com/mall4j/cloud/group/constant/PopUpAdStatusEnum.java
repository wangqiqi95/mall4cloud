package com.mall4j.cloud.group.constant;

import lombok.Getter;

@Getter
public enum PopUpAdStatusEnum {

    ENABLE(true, 1),
    DISABLE(false, 0);

    private Boolean adStatus;
    private Integer adStatusIntValue;


    PopUpAdStatusEnum(Boolean adStatus, Integer adStatusIntValue){
        this.adStatus = adStatus;
        this.adStatusIntValue = adStatusIntValue;
    }
}
