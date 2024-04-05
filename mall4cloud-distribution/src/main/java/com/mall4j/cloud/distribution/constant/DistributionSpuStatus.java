package com.mall4j.cloud.distribution.constant;

/**
 * @Author lth
 * @Date 2021/5/25 9:13
 */
public enum DistributionSpuStatus {

    /**
     * 商家下架
     */
    OFF_SHELF(0),
    /**
     * 商家上架
     */
    PUT_SHELF(1),
    /**
     * 违规下架
     */
    PLATFORM_OFF_SHELF(2),
    /**
     * 平台审核
     */
    WAITAUDIT(3)

    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    DistributionSpuStatus(Integer num) {
        this.num = num;
    }

    public static DistributionSpuStatus instance(Integer value) {
        DistributionSpuStatus[] enums = values();
        for (DistributionSpuStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
