package com.mall4j.cloud.api.biz.constant.cp;

import lombok.Getter;

/**
 **/
public enum CodeChannelEnum {
    CHANNEL_CODE(0, "渠道活码"),
    GROUP_CODE(1, "群活码"),
    AUTO_GROUP_CODE(2, "自动拉群"),
    TAG_GROUP_CODE(3, "标签建群");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    CodeChannelEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        CodeChannelEnum[] enums = values();
        for (CodeChannelEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
