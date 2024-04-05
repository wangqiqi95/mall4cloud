package com.mall4j.cloud.distribution.constant;

/**
 * 收入类型(1一代奖励、2二代奖励 3邀请奖励)
 * @author cl
 * @date 2021-08-18 17:03:46
 */
public enum DistributionUserIncomeTypeEnum {


    /**
     * 直推奖励 : 一代奖励
     */
    AWARD_ONE(1, "直推奖励"),
    /**
     * 间推奖励: 二代奖励
     */
    AWARD_TWO(2, "间推奖励"),

    /**
     * 邀请奖励
     */
    INVITATION(3, "邀请奖励");
    ;

    private int value;
    private String desc;

    public Integer value() {
        return value;
    }
    public String desc() {
        return desc;
    }

    DistributionUserIncomeTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static DistributionUserIncomeTypeEnum instance(Integer value) {
        DistributionUserIncomeTypeEnum[] enums = values();
        for (DistributionUserIncomeTypeEnum typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
