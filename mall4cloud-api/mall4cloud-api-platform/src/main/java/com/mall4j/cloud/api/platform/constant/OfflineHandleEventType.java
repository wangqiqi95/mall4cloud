package com.mall4j.cloud.api.platform.constant;

/**
 * 下架处理事件类型
 *
 * @author YXF
 */
public enum OfflineHandleEventType {

    /**
     * 商品
     */
    PROD(1, "商品"),
    /**
     * 店铺
     */
    SHOP(2,"店铺"),
    /**
     * 满减
     */
    DISCOUNT(3,"满减"),
    /**
     * 优惠券
     */
    COUPON(4,"优惠券"),
    /**
     * 团购活动
     */
    GROUP_BUY(5,"团购活动"),
    /**
     * 分销总开关
     */
    DISTRIBUTION(6,"分销总开关"),
    /**
     * 秒杀
     */
    SECKILL(7,"秒杀"),

    /**
     * 分销商品
     */
    DISTRIBUTION_PROD(8,"分销商品"),

    /**
     * 自提点
     */
    STATION(9,"自提点"),
    ;

    private final Integer value;
    private final String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    OfflineHandleEventType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}



