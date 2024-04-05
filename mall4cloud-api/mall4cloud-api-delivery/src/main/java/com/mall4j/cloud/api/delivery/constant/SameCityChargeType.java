package com.mall4j.cloud.api.delivery.constant;


/**
 * 同城配送收费方式 1按区域收取固定配送费 2按距离收取配送费
 * @author FrozenWatermelon
 */
public enum SameCityChargeType {

    /**
     * 1按区域收取固定配送费
     */
    FIXED(1),

    /**
     * 2按距离收取配送费
     */
    DISTANCE(2)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    SameCityChargeType(Integer num){
        this.num = num;
    }

    public static SameCityChargeType instance(Integer value) {
        SameCityChargeType[] enums = values();
        for (SameCityChargeType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
