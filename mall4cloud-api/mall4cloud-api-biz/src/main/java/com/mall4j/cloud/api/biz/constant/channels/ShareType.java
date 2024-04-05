package com.mall4j.cloud.api.biz.constant.channels;

import lombok.Getter;

/**
 * @Description 视频号分享员类型
 * @Author axin
 * @Date 2023-03-06 11:03
 **/
public enum ShareType {
    EVERYMAN (0, "普通分享员"),
    ENTERPRISE(1,"企业分享员");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    ShareType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        ShareType[] enums = values();
        for (ShareType statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }
}
