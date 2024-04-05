package com.mall4j.cloud.product.constant;

/**
 * @Author lth
 * @Date 2021/5/25 9:13
 */
public enum SpuStatus {


    /**
     * 删除
     */
    DELETE(-1),
    /**
     * 下架
     */
    OFF_SHELF(0),
    /**
     * 上架
     */
    PUT_SHELF(1),
    /**
     * 平台下架
     */
    PLATFORM_OFF_SHELF(2),
    /**
     * 未审核
     */
    WAITAUDIT(3)

    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    SpuStatus(Integer num) {
        this.num = num;
    }

    public static SpuStatus instance(Integer value) {
        SpuStatus[] enums = values();
        for (SpuStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
