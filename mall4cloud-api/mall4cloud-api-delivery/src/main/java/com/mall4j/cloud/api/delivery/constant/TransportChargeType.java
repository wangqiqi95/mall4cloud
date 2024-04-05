package com.mall4j.cloud.api.delivery.constant;


/**
 * 运费收费方式 （0 按件数,1 按重量 2 按体积）
 * @author FrozenWatermelon
 */
public enum TransportChargeType {

    /**
     * 0按件数
     */
    COUNT(0),

    /**
     * 1按重量
     */
    WEIGHT(1),

    /**
     * 2按体积
     */
    VOLUME(2)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    TransportChargeType(Integer num){
        this.num = num;
    }

    public static TransportChargeType instance(Integer value) {
        TransportChargeType[] enums = values();
        for (TransportChargeType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
