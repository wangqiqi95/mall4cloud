package com.mall4j.cloud.api.biz.constant.cp;

import lombok.Getter;

/**
 源：0欢迎语/1渠道活码/2渠道活码时间段
 **/
public enum WelcomeSourceFromEnum {
    WELCOME(0, "欢迎语"),
    CHANNEL_CODE(1, "渠道活码"),
    CHANNEL_CODE_TIME   (2, "渠道活码时间段");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    WelcomeSourceFromEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        WelcomeSourceFromEnum[] enums = values();
        for (WelcomeSourceFromEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
