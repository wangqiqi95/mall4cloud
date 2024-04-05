package com.mall4j.cloud.api.user.constant;

import lombok.Getter;

/**
 * 兑换方式枚举
 * */
@Getter
public enum ConvertTypeEnum {

    //兑礼到店
    TO_SHOP(0),
    //积分兑券
    COUPON_EXCHANGE(1),
    //兑礼到家
    TO_HOUSE(2);

    private Integer type;

    ConvertTypeEnum(Integer type){
        this.type = type;
    }

}
