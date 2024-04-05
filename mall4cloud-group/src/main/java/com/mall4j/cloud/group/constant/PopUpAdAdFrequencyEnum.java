package com.mall4j.cloud.group.constant;

import lombok.Getter;

@Getter
public enum PopUpAdAdFrequencyEnum {

    EVERY_DAY(0),
    ALWAYS(1),
    ONCE(2);


    private Integer adFrequency;

    PopUpAdAdFrequencyEnum(Integer adFrequency){
        this.adFrequency = adFrequency;
    }
}
