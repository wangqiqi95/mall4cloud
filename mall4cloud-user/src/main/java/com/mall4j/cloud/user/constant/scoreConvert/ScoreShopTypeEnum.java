package com.mall4j.cloud.user.constant.scoreConvert;

/**
 * 积分活动关联门店 [类型]
 *
 * @author shijing
 */

public enum ScoreShopTypeEnum {
    /**
     * 0：积分活动相关门店
     */
    APPLY(0, "积分活动相关门店"),

    /**
     * 1：积分活动线下兑换门店
     */
    EXCHANGE(1,"积分活动线下兑换门店"),

    /**
     * 1：积分活动线下兑换门店
     */
    COUPON(2, "优惠券相关门店")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    ScoreShopTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ScoreShopTypeEnum instance(Integer value) {
        ScoreShopTypeEnum[] enums = values();
        for (ScoreShopTypeEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
