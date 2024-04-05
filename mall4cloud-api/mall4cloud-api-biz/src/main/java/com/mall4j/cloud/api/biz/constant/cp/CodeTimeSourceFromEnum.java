package com.mall4j.cloud.api.biz.constant.cp;

import lombok.Getter;

/**
 * 渠道活码人员时间段
 * @Description 源：0欢迎语/1渠道活码
 **/
public enum CodeTimeSourceFromEnum {
    WELCOME(0, "欢迎语"),
    CHANNEL_CODE(1, "渠道活码");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    CodeTimeSourceFromEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        CodeTimeSourceFromEnum[] enums = values();
        for (CodeTimeSourceFromEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
