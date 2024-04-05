package com.mall4j.cloud.group.constant;

import lombok.Getter;

@Getter
public enum PopUpAdShopEnum {

    IS_ALL_SHOP(1),
    NOT_ALL_SHOP(0);

    private Integer isAllShop;

    PopUpAdShopEnum(Integer isAllShop){
        this.isAllShop = isAllShop;
    }

}
