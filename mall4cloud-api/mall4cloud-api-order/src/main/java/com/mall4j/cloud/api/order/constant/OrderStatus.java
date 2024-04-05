package com.mall4j.cloud.api.order.constant;

/**
 * 订单状态
 * @author FrozenWatermelon
 * @date 2020/12/18
 */
public enum OrderStatus {

    /**
     * 没有付款.待付款
     */
    UNPAY(1, "待付款", "待付款"),

    /**
     * 已经付款,但卖家没有发货.待发货
     */
    PAYED(2, "待发货", "待自提"),

    /**
     * 发货，导致实际库存减少，没有确认收货.待收货
     */
    CONSIGNMENT(3, "待收货", "已自提"),

    /**
     * 订单确认收货成功，购买数增加1.
     */
    SUCCESS(5, "交易完成", "交易完成"),

    /**
     * 交易失败,还原库存
     */
    CLOSE(6, "交易失败", "交易失败"),

    /**
     * 待成团
     */
    WAIT_GROUP(7, "待成团", "待成团");

    private final Integer num;
    private final String deliveryName;
    private final String stationName;

    public Integer value() {
        return num;
    }

    public String deliveryName() {
        return deliveryName;
    }

    public String stationName() {
        return stationName;
    }

    OrderStatus(Integer num, String deliveryName, String stationName) {
        this.num = num;
        this.deliveryName = deliveryName;
        this.stationName = stationName;
    }

    public static OrderStatus instance(Integer value) {
        OrderStatus[] enums = values();
        for (OrderStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static String getDeliveryName(Integer value) {
        OrderStatus[] enums = values();
        for (OrderStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum.deliveryName;
            }
        }
        return null;
    }

    public static String getStationName(Integer value) {
        OrderStatus[] enums = values();
        for (OrderStatus statusEnum : enums) {
            if (!statusEnum.value().equals(value)) {
                return statusEnum.stationName;
            }
        }
        return null;
    }
}
