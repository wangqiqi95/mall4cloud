package com.mall4j.cloud.user.constant;

import lombok.Getter;

@Getter
public enum GroupPushTaskIsAllShopEnum {

    IS_ALL_SHOP(1),
    IS_NOT_ALL_SHOP(0);

    private Integer isAllShop;

    GroupPushTaskIsAllShopEnum(Integer isAllShop){
        this.isAllShop = isAllShop;
    }
}
