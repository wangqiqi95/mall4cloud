package com.mall4j.cloud.api.multishop.constant;

/**
 * 店铺状态
 *
 * @author YXF
 */
public enum ShopStatus {

    /**
     * 已删除
     */
    DELETE(-1),
    /**
     * 停业中
     */
    STOP(0),

    /**
     * 营业中
     */
    OPEN(1),

    /**
     * 违规下线
     */
    OFFLINE(2),

    /**
     * 开店申请待审核
     */
    OPEN_AWAIT_AUDIT(3),

    /**
     * 店铺开店申请中
     */
    APPLYING(4),

    /**
     * 上线申请待审核
     */
    OFFLINE_AWAIT_AUDIT(5)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    ShopStatus(Integer num) {
        this.num = num;
    }

    public static ShopStatus instance(Integer value) {
        ShopStatus[] enums = values();
        for (ShopStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
