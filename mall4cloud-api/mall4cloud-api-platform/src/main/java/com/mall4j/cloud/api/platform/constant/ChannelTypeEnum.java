package com.mall4j.cloud.api.platform.constant;

import lombok.Getter;

@Getter
public enum ChannelTypeEnum {

    APP_SHOP("小程序",1),
    POS_SHOP("线下门店",2),
    TAO_BAO_SHOP("淘宝",3),
    WEB_SHOP("官网",4),
    JIN_DONG_SHOP("京东",5),
    DOU_YIN_SHOP("抖音",6);

    private String shopType;

    private Integer shopFlag;

    ChannelTypeEnum(String shopType, Integer shopFlag){
        this.shopType = shopType;
        this.shopFlag = shopFlag;
    }
}
