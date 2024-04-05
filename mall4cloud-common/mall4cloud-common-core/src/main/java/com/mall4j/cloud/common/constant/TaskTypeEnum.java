package com.mall4j.cloud.common.constant;

/**
 * 任务状态枚举
 * @author 知章
 * @date 2024/4/2 14:45

 */
public enum TaskTypeEnum {

    HAVE_NOT_STARTED(0, "未开始"),
    COUPON_EXPIRE(1, "进行中"),
    COUPON_ARRIVAL(2,"已结束"),

    ;

    private final Integer value;
    private final String desc;
    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    TaskTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static TaskTypeEnum instance(Integer value) {
        TaskTypeEnum[] enums = values();
        for (TaskTypeEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
