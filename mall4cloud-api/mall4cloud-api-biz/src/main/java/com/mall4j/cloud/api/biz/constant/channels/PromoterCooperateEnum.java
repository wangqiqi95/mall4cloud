package com.mall4j.cloud.api.biz.constant.channels;

import lombok.Getter;

/**
 * @Description 达人合作状态
 * @Author axin
 * @Date 2023-02-15 10:56
 **/
public enum PromoterCooperateEnum {
    INIT(0, "初始值"),
    INVITED(1, "邀请中"),
    INVITATION_ACCEPTED(2, "达人已接受邀请"),
    INVITATION_DECLINED(3, "达人已拒绝邀请"),
    INVITATION_CANCELED(4, "已取消邀请"),
    COOPERATION_CANCELED(5, "已取消合作"),
    DELETEED(10, "已删除");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    PromoterCooperateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        PromoterCooperateEnum[] enums = values();
        for (PromoterCooperateEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
