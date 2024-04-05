package com.mall4j.cloud.api.platform.constant;

/**
 * 下架处理事件处理状态
 *
 * @author YXF
 */
public enum OfflineHandleEventStatus {

    /**
     * 平台进行下线
     */
    OFFLINE_BY_PLATFORM(1, "平台进行下线"),

    /**
     * 重新申请
     */
    APPLY_BY_SHOP(2, "商家申请恢复"),

    /**
     * 平台审核通过
     */
    AGREE_BY_PLATFORM(3, "平台审核通过"),
    /**
     * 平台审核不通过
     */
    DISAGREE_BY_PLATFORM(4, "平台审核不通过"),
    ;

    private final Integer value;
    private final String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    OfflineHandleEventStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}



