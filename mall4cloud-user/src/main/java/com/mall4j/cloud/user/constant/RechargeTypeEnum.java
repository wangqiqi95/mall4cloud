package com.mall4j.cloud.user.constant;
/**
 * 充值类型 1:充值 2:赠送 3:支付 4:退款 5:平台手动修改
 * @author cl
 * @date 2021-4-28 10:04:28
 */
public enum RechargeTypeEnum {

    /**
     * 1.充值
     */
    RECHARGE(1),

    /**
     * 赠送
     */
    PRESENT(2),

    /**
     * 支付
     */
    PAY(3),

    /**
     * 退款
     */
    REFUND(4),

    /**
     * 平台手动修改
     */
    SYSTEM(5),

    /**
     * 购买会员
     */
    VIP(6)
    ;


    private final Integer num;

    public Integer value() {
        return num;
    }

    RechargeTypeEnum(Integer num){
        this.num = num;
    }

    public static RechargeTypeEnum instance(Integer value) {
        RechargeTypeEnum[] enums = values();
        for (RechargeTypeEnum typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
