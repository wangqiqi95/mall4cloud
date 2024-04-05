package com.mall4j.cloud.common.order.constant;

/**
 * 配送类型
 * @author FrozenWatermelon
 */
public enum DeliveryType {

    /**
     * 快递
     */
    DELIVERY(1, "快递发货"),
    /**
     * 自提
     */
    STATION(2, "到店自提"),

    /**
     * 无需快递
     */
    NOT_DELIVERY(3, "无需快递"),
    /**
     * 同城配送
     */
    SAME_CITY(4, "同城配送"),
    /**
     * SR物流
     */
    LOGISTICS(5, "物流")
    ;

    private final Integer num;
    private final String description;

    public Integer value() {
        return num;
    }

    public String description() {
        return description;
    }

    DeliveryType(Integer num, String name) {
        this.num = num;
        this.description = name;
    }

    public static DeliveryType instance(Integer value) {
        DeliveryType[] enums = values();
        for (DeliveryType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static String getDescription(Integer value) {
        DeliveryType[] enums = values();
        for (DeliveryType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum.description;
            }
        }
        return null;
    }
}
