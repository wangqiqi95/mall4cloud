package com.mall4j.cloud.coupon.constant;

import lombok.Getter;

@Getter
public enum ToShopInventoryEnum {

    ENOUGH(1,"ENOUGH"),
    FEW(1,"FEW"),
    SHORT(2,"SHORT");

    private Integer sort;
    private String key;

    ToShopInventoryEnum(Integer sort, String key){
        this.sort = sort;
        this.key = key;
    }
}
