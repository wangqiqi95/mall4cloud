package com.mall4j.cloud.api.biz.constant.channels;

import lombok.Getter;

/**
 * @Description 推广类型
 * @Author axin
 * @Date 2023-02-21 11:37
 **/
public enum PromotionType {
    ORDINARY (1, "普通推广商品"),
    DIRECTIONAL(2,"定向推广商品"),
    EXCLUSIVE(3,"专属推广商品");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    PromotionType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        PromotionType[] enums = values();
        for (PromotionType statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return "";
    }
}
