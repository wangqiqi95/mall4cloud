package com.mall4j.cloud.user.constant;

/**
 * 用户等级变更原因1.成长值不足 2.成长值足够 3.购买会员 4.续费会员 5.会员到期
 * @author FrozenWatermelon
 */
public enum LevelChangeReasonEnum {
    /**
     * 成长值不足
     */
    GROWTH_NOT_ENOUGH(1),

    /**
     * 成长值足够
     */
    GROWTH_ENOUGH(2),

    /**
     * 购买会员
     */
    BUY_VIP(3),

    /**
     * 续费会员
     */
    RENEW_VIP(4),

    /**
     * 会员到期
     */
    VIP_EXPIRE(5),

    /**
     * 会员等级配置更新
     * user_level 表增加/删除/修改 引起用户等级发生变化
     */
    USER_LEVEL_CHANGE(6)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    LevelChangeReasonEnum(Integer value) {
        this.value = value;
    }
}
