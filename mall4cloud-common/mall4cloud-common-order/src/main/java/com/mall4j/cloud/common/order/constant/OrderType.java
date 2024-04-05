package com.mall4j.cloud.common.order.constant;

/**
 * 订单类型
 * @author FrozenWatermelon
 * @date 2020/12/18
 */
public enum OrderType {

    /**
     * 普通订单
     */
    ORDINARY(0),
    /**
     * 团购订单
     */
    GROUP(1),

    /**
     * 秒杀订单
     */
    SECKILL(2),
    /**
     * 积分订单
     */
    SCORE(3),
    /**
     * 一口价订单
     */
    ONEPRICE(4),
    /**
     * 企业单
     */
    ENTERPRISE(5),

    DAIKE(6),
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    OrderType(Integer num) {
        this.num = num;
    }

    public static OrderType instance(Integer value) {
        OrderType[] enums = values();
        for (OrderType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
