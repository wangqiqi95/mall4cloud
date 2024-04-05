package com.mall4j.cloud.distribution.constant;

/**
 * @author cl
 * @date 2021-08-11 09:44:30
 */
public enum DistributionWalletBillTypeEnum {

    /**
     * 系统修改
     */
    SYSTEM(0),

    /**
     * 人工修改
     */
    USER(1)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    DistributionWalletBillTypeEnum(Integer value) {
        this.value = value;
    }
}
