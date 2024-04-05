package com.mall4j.cloud.api.biz.constant.channels;

import lombok.Getter;

/**
 * @Description 视频号分享员绑定状态
 * @Author axin
 * @Date 2023-02-21 11:37
 **/
public enum LiveStoreSharerBindStatus {
    INIT(0,"初始化"),
    BIND_WAIT (1, "待绑定"),
    BIND_EXPIRE(2,"绑定失效"),
    BIND_SUCC(3,"绑定成功"),
    UNBIND(4,"解除绑定");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    LiveStoreSharerBindStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        LiveStoreSharerBindStatus[] enums = values();
        for (LiveStoreSharerBindStatus statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }
}
