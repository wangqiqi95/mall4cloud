package com.mall4j.cloud.group.constant;


import lombok.Getter;

@Getter
public enum PopUpFrequencyEnum {
//    "popUpAd::everyDay::","::"

    EVERY_DAY(0),
    ONCE(2);

    private Integer frequency;


    PopUpFrequencyEnum(Integer frequency){
        this.frequency = frequency;
    }
}
