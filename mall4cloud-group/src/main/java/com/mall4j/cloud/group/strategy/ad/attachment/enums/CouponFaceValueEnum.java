package com.mall4j.cloud.group.strategy.ad.attachment.enums;

import lombok.Getter;

@Getter
public enum CouponFaceValueEnum {

    FULL_DISCOUNT(Short.valueOf("0"),"满减券"),
    REDUCED(Short.valueOf("1"),"折扣券");


    private Short type;
    private String flag;


    CouponFaceValueEnum(Short type , String flag){
        this.type = type;
        this.flag = flag;
    }
}
