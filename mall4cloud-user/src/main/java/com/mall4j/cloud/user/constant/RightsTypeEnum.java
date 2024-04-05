package com.mall4j.cloud.user.constant;

/**
 * 权益类型[0.自定义 1.积分回馈倍率 2.优惠券 3.积分赠送(数量) 4.会员折扣 5.包邮类型]
 *
 * @author Pineapple
 * @date 2021/5/10 14:11
 */
public enum RightsTypeEnum {
    /**
     * 自定义
     */
    CUSTOMIZATION(0),

    /**
     * 积分回馈倍率
     */
    SCORE_REWARD_MULTIPLIER(1),

    /**
     * 优惠券
     */
    COUPON(2),

    /**
     * 积分赠送(数量)
     */
    SCORE_PRESENTING(3),

    /**
     * 会员折扣
     */
    MEMBER_DISCOUNT(4),

    /**
     * 包邮类型
     */
    FREE_FEE_TYPE(5)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    RightsTypeEnum(Integer value) {
        this.value = value;
    }
}
