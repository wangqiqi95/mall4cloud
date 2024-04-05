package com.mall4j.cloud.api.product.enums;

public enum PriceTypeEnums {

    /**
     * 价格类型 1-吊牌价 2-保护价 3-活动价 4-渠道价
     */
    MARKET_PRICE(1 , "吊牌价"),
    PROTECT_PRICE(2 , "保护价"),
    ACTIVITY_PRICE(3 , "活动价"),
    CHANNEL_PRICE(4 , "渠道价"),
    ;

    private Integer code;

    private String name;

    PriceTypeEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getName( Integer code){
        PriceTypeEnums[] enums = values();
        for( PriceTypeEnums e : enums){
            if(e.getCode().equals( code)){
                return e.getName();
            }
        }
        return null;
    }
}
