package com.mall4j.cloud.api.coupon.constant;


/**
 * 优惠券适用商品类型
 * @author FrozenWatermelon
 */
public enum CouponProdType {

    /**
     * 0全部商品参与
     */
    ALL(0),

    /**
     * 1指定商品参与 SPU
     */
    PROD_IN(1),

    /**
     * 2指定商品参与 SPU
     */
    PROD_NOT_IN(2),
    /**
     * 3指定商品参与SKU
     */
    PRICECODE_IN(3),
    /**
     * 4指定商品参与SKU
     */
    PRICECODE_NOT_IN(4),

    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    CouponProdType(Integer num){
        this.num = num;
    }

    public static CouponProdType instance(Integer value) {
        CouponProdType[] enums = values();
        for (CouponProdType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
