package com.mall4j.cloud.api.group.enums;

public enum ActivityChannelEnums {

    PAY_ACTIVITY(1 , "支付有礼"),
    MEAL_ACTIVITY(2 , "一口价"),
    ORDER_GIFT_ACTIVITY(3 , "下单送赠品"),
    TIME_DISCOUNT_ACTIVITY(4 , "限时调价"),
    GROUP_ACTIVITY(5 , "拼团"),
    SECKILL_ACTIVITY(6 , "秒杀"),
    MEMBER_ACTIVITY(7 , "会员日"),
    STORE_INVENT(8 , "虚拟门店价")
    ;

    private Integer code;

    private String name;

    ActivityChannelEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getName( Integer code){
        ActivityChannelEnums[] enums = values();
        for( ActivityChannelEnums e : enums){
            if(e.getCode().equals( code)){
                return e.getName();
            }
        }
        return null;
    }
}
