package com.mall4j.cloud.api.coupon.constant;

/**
 * 活动来源
 *
 * @author shijing
 * @date 2022-2-6
 */

public enum ActivitySourceEnum {
    PUSH(0, "推券中心"),

    RECEIVE(1,"领券中心"),

    BUY(2,"现金买券"),

    SCORE_COUPON(3,"积分换券"),

    COUPON_PACK(4,"券包活动"),

    PERFECT_DATA(5,"完善资料"),

    REGISTER_ACTIVITY(6,"注册有礼"),

    SIGN_ACTIVITY(7,"签到活动"),

    PAY_ACTIVITY(8,"支付有礼"),

    DRAW_ACTIVITY(9,"抽奖活动"),

    GOODS_COUPON(10,"商详领券"),

    SYSTEM_COUPON(11,"管理员发券"),
    POP_UP_AD_COUPON(13,"广告领券")
    ;

    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    ActivitySourceEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ActivitySourceEnum instance(Integer value) {
        ActivitySourceEnum[] enums = values();
        for (ActivitySourceEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
