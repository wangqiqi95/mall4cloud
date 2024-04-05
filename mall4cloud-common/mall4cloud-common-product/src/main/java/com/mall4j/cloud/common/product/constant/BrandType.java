package com.mall4j.cloud.common.product.constant;/**

@Author lth
@Date 2021/4/26 9:58
*/public enum BrandType {

    /**
     * 平台品牌
     */
    PLATFORM(0),

    /**
     * 店铺自定义品牌
     */
    CUSTOMIZE(1)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    BrandType(Integer value) {
        this.value = value;
    }
}
