package com.mall4j.cloud.group.constant;

/**
 * 拼团团队状态类型
 * @author yxf
 * @date 2020/11/20
 */
public enum TeamStatusEnum {

    /** 待成团(未支付) */
    WAITING_GROUP(0, "待成团"),

    /** 拼团中(已支付) */
    IN_GROUP(1, "拼团中"),

    /** 拼团成功 */
    SUCCESS(2, "拼团成功"),

    /** 拼团失败 */
    FAIL(3, "拼团失败");

    private final Integer code;

    private final String title;

    public Integer value() {
        return code;
    }

    TeamStatusEnum(Integer code, String title){
        this.code = code;
        this.title = title;
    }
}
