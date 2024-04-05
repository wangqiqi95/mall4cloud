package com.mall4j.cloud.biz.constant;

import java.util.Objects;

/**
 * @Description 优选联盟商品状态
 * @Author axin
 * @Date 2023-04-27 11:36
 **/
public enum LeagueItemStatus {
    LISTING(0 , "上架"),
    DE_LISTING(1 , "下架"),
    EXPIRED(2 , "待生效"),
    EFFECTIVE(3 , "推广中");


    private final Integer value;

    private final String desc;

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    LeagueItemStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        if(Objects.isNull(value)) return "";

        LeagueItemStatus[] enums = values();
        for (LeagueItemStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum.desc;
            }
        }
        return "";
    }
}
