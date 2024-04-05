package com.mall4j.cloud.api.delivery.constant;


/**
 * 包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）
 * @author FrozenWatermelon
 */
public enum TransportFreeType {

    /**
     * 0满x件/重量/体积包邮
     */
    COUNT(0),

    /**
     * 1满金额包邮
     */
    AMOUNT(1),

    /**
     * 2满x件/重量/体积且满金额包邮
     */
    COUNT_AND_AMOUNT(2)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    TransportFreeType(Integer num){
        this.num = num;
    }

    public static TransportFreeType instance(Integer value) {
        TransportFreeType[] enums = values();
        for (TransportFreeType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
