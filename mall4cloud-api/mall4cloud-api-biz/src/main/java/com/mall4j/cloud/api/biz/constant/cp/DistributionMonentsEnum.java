package com.mall4j.cloud.api.biz.constant.cp;

import lombok.Getter;

/**
 * 朋友圈任务状态：0草稿/1进行中/2已终止/3超时终止
 **/
public enum DistributionMonentsEnum {
    DRAFT(0, "草稿"),
    UNDER_WAY(1, "进行中"),
    TERMINATE(2, "已终止"),
    TIME_OUT_TERMINATE(3, "超时终止");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    DistributionMonentsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        DistributionMonentsEnum[] enums = values();
        for (DistributionMonentsEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
