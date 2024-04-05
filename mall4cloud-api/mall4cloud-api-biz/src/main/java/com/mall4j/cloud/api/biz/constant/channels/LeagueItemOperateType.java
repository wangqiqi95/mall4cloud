package com.mall4j.cloud.api.biz.constant.channels;

import lombok.Getter;

/**
 * @Description 优选联盟商品操作状态
 * @Author axin
 * @Date 2023-02-22 16:19
 **/
public enum LeagueItemOperateType {
    UPD_LISTING (1, "编辑并上架"),
    LISTING(2,"下架"),
    DELISTING(4,"上架");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    LeagueItemOperateType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        LeagueItemOperateType[] enums = values();
        for (LeagueItemOperateType statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }
}
