package com.mall4j.cloud.product.constant;

/**
 * @Author HWY
 * @Date 2022/3/12 19:13
 */
public enum TagActivityStatus {


    /**
     * 未启动
     */
    CREATE(0),
    /**
     * 未开始
     */
    NOT_START(1),
    /**
     * 进行中
     */
    PROGRESS(2),
    /**
     * 已结束
     */
    END(3),



    ;
    private final Integer num;

    public Integer value() {
        return num;
    }

    TagActivityStatus(Integer num) {
        this.num = num;
    }

    public static TagActivityStatus instance(Integer value) {
        TagActivityStatus[] enums = values();
        for (TagActivityStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
