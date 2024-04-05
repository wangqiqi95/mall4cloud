

package com.mall4j.cloud.biz.model.live;

/**
 * 微信直播商品类型
 * 0：未审核，1：审核中，2:审核通过，3 审核失败
 * @author lhd
 */
public enum LiveProdStatusType {
    /**
     * 已删除
     */
    DELETE(-1),
    /**
     * 未审核
     */
    NO_EXAMINE(0),
    /**
     * 审核中
     */
    EXAMINING(1),

    /**
     * 审核通过
     */
    EXAMINE_SUCCESS(2),

    /**
     * 审核失败
     */
    EXAMINE_FAIL(3),

    /**
     * 4:微信直播服务平台，违规下架
     */
    WX_BREAK(4),

    /**
     * 5:平台撤销直播商品
     */
    PLATFORM_BREAK(5)
    ;


    private Integer num;

    public Integer value() {
        return num;
    }

    LiveProdStatusType(Integer num){
        this.num = num;
    }

    public static LiveProdStatusType instance(Integer value) {
        LiveProdStatusType[] enums = values();
        for (LiveProdStatusType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
