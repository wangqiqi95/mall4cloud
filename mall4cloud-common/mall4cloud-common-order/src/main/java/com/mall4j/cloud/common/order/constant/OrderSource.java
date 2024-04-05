package com.mall4j.cloud.common.order.constant;

/**
 * 订单类型
 * @author FrozenWatermelon
 * @date 2020/12/18
 */
public enum OrderSource {

    /**
     * 普通订单
     */
    NORMAL(0),
    /**
     * 直播订单
     */
    LIVE(1),
    /**
     * 视频号3.0订单
     */
    LIVE_SHOP(2),
    /**
     * 视频号4.0订单
     */
    CHANNELS(3),

    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    OrderSource(Integer num) {
        this.num = num;
    }

    public static OrderSource instance(Integer value) {
        OrderSource[] enums = values();
        for (OrderSource statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
