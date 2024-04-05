package com.mall4j.cloud.biz.constant;

/**
 * 通知类型 1.短信发送 2.公众号订阅消息 3.站内消息
 * @author lhd
 * @date 2021-04-12 09:36:59
 */
public enum NotifyType {


    /**
     * 短信发送
     */
    SMS(1),

    /**
     * 公众号订阅消息
     */
    MP(2),

    /**
     * 站内消息
     */
    APP(3)

    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    NotifyType(Integer num) {
        this.num = num;
    }

    public static NotifyType instance(Integer value) {
        NotifyType[] enums = values();
        for (NotifyType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
