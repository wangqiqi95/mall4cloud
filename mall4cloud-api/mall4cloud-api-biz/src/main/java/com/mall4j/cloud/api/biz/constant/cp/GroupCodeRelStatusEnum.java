package com.mall4j.cloud.api.biz.constant.cp;

import lombok.Getter;

/**
 * 群活码-关联群聊状态信息
 **/
public enum GroupCodeRelStatusEnum {
    NO_START(0, "未开始"),
    PULL_USER(1, "拉人中"),
    FULL_USER(2, "已满员"),
    UPPER_LIMIT(3, "已达到上限"),
    EXPIRED(4, "已过期"),
    STOP(5, "已停用")
    ;

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    GroupCodeRelStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        GroupCodeRelStatusEnum[] enums = values();
        for (GroupCodeRelStatusEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
