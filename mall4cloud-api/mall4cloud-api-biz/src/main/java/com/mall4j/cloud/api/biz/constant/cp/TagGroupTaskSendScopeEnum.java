package com.mall4j.cloud.api.biz.constant.cp;

import lombok.Getter;

/**
 * 发送范围: 0按部门员工/1按客户标签/2按客户分组
 **/
public enum TagGroupTaskSendScopeEnum {
    DEPART_STAFF(0, "按部门员工"),
    USER_TAG(1, "按客户标签"),
    USER_GROUP_PHASE(2, "按客户分组阶段");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    TagGroupTaskSendScopeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        TagGroupTaskSendScopeEnum[] enums = values();
        for (TagGroupTaskSendScopeEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
