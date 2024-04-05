package com.mall4j.cloud.biz.wx.wx.constant;

/**
 * @Date 2022年3月21日, 0021 16:10
 * @Created by eury
 */
public enum WxBusinessType {

    /**
     * 业务类型：1:会员业务 2:优惠券业务 3:订单业务 4:退单业务 5:线下活动 6:积分商城 7:每日签到 8:直播通知 9:微客审核
     */
    MEMBER(1, "会员业务"),
    CONPON(2, "优惠券业务"),
    ORDER(3, "订单业务"),
    ORDER_REFUND(4, "退单业务"),
    ACTIVITY(5, "线下活动"),
    SCORE_SHOP(6, "积分商城"),
    SIGIN_IN_DAY(7, "每日签到"),
    LIVE(8, "直播通知"),
    WEKE_CHECK(9, "微客审核");

    private final Integer value;

    private final String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    WxBusinessType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static WxBusinessType instance(Integer value) {
        WxBusinessType[] enums = values();
        for (WxBusinessType statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
