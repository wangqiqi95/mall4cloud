package com.mall4j.cloud.discount.constant;

/**
 * 满减满折活动状态枚举
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
public enum DiscountStatusEnum {

    /**  关闭    */
    CLOSE(0, "关闭"),

    /**  启动    */
    RUN(1, "启动"),

    /**  违规下线    */
    OFFLINE(2, "违规下线"),

    /**  平台审核    */
    PLATFORM_AUDIT(3, "平台审核");

    private final Integer value;
    private final String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    DiscountStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

