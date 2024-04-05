package com.mall4j.cloud.api.biz.constant;

import lombok.Getter;

/**
 * 企微消息时间同步数云枚举类型
 **/
public enum PushCrmCpTypeEnum {
    DEPART("1", "部门数据"),
    STAFF("2", "部门成员"),
    EXTERNAL("3", "外部联系人及联系人与部门成员映射关系"),
    CUST_GROUP("4", "企微群及群客户信息");

    @Getter
    private final String value;
    @Getter
    private final String desc;

    PushCrmCpTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(String value) {
        PushCrmCpTypeEnum[] enums = values();
        for (PushCrmCpTypeEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
