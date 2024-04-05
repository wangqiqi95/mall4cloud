package com.mall4j.cloud.api.order.constant;

/**
 * 订单状态
 * @author FrozenWatermelon
 * @date 2020/12/18
 */
public enum OrderDeleteStatus {

    /**
     * 没有删除
     */
    NOT_DELETE(1),

    /**
     * 回收站
     */
    RECYCLE_BIN(1),

    /**
     * 永久删除
     */
    DELETE(2);


    private final Integer num;

    public Integer value() {
        return num;
    }


    OrderDeleteStatus(Integer num) {
        this.num = num;
    }

    public static OrderDeleteStatus instance(Integer value) {
        OrderDeleteStatus[] enums = values();
        for (OrderDeleteStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
