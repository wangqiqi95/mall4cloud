package com.mall4j.cloud.api.product.enums;

/**
 * @Author lth
 * @Date 2021/5/25 9:13
 */
public enum SkuPriceLogType {

    //日志类型：0-同步商品基础数据 1-同步吊牌价 2-同步保护价 3-同步pos价 4-同步库存 5-商品批量改价 6-批量设置渠道价 7-批量取消渠道价 8-同步吊牌价重算门店pos价

    /**
     * 同步商品基础数据
     */
    ERP_PUSH_PRODUCT(0,"同步商品基础数据"),
    /**
     * 同步吊牌价
     */
    ERP_PUSH_MARKET_PRICE(1,"同步吊牌价"),
    /**
     * 同步保护价
     */
    ERP_PUSH_PROTECT_PRICE(2,"同步保护价"),
    /**
     * 同步pos价
     */
    ERP_PUSH_POS_PRICE(3,"同步pos价"),
    /**
     * 同步库存
     */
    ERP_PUSH_STOCK(4,"同步库存"),
    /**
     * 商品批量改价
     */
    APP_UPDATE_BATCH_PRICE(5,"商品批量改价"),
    /**
     * 批量设置渠道价
     */
    APP_BATCH_CHANNEL_PRICE(6,"批量设置渠道价"),
    /**
     * 批量取消渠道价
     */
    APP_CANCEL_CHANNEL_PRICE(7,"批量取消渠道价"),

    ERP_PUSH_MARKET_PRICE_RELSTORE_POS(8,"同步吊牌价重算门店pos价")

    ;

    private final Integer num;

    private final String str;

    public Integer value() {
        return num;
    }

    public String getStr() {
        return str;
    }

    SkuPriceLogType(Integer num, String str) {
        this.num = num;
        this.str = str;
    }

    public static SkuPriceLogType instance(Integer value) {
        SkuPriceLogType[] enums = values();
        for (SkuPriceLogType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
