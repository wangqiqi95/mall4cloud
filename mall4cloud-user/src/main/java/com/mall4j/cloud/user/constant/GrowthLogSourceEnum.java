package com.mall4j.cloud.user.constant;


/**
 * 成长值来源
 * @author cl
 * @date 2021-04-25 10:16:53
 */
public enum GrowthLogSourceEnum {
    /**
     * 系统修改用户成长值
     */
    SYSTEM(0),
    /**
     * 订单
     */
    ORDER(1),
    /**
     * 余额
     */
    BALANCE(2)
    ;
    private final Integer num;

    public Integer value() {
        return num;
    }

    GrowthLogSourceEnum(Integer num){
        this.num = num;
    }

    public static GrowthLogSourceEnum instance(Integer value) {
        GrowthLogSourceEnum[] enums = values();
        for (GrowthLogSourceEnum sourceEnum : enums) {
            if (sourceEnum.value().equals(value)) {
                return sourceEnum;
            }
        }
        return null;
    }
}
