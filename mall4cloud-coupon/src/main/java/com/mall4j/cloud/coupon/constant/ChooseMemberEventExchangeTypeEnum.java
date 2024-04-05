package com.mall4j.cloud.coupon.constant;


import lombok.Getter;

@Getter
public enum ChooseMemberEventExchangeTypeEnum {

    //兑礼到店
    TO_SHOP(0),
    //兑礼到家
    TO_HOME(1);

    private Integer exchangeType;

    ChooseMemberEventExchangeTypeEnum(Integer exchangeType){

        this.exchangeType = exchangeType;
    }
}
