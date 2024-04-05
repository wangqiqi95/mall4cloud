package com.mall4j.cloud.api.biz.constant.cp;

import lombok.Getter;

/**
 * 状态：0待分配/1任务中/2添加成功
 */
public enum TaskPhoneLibraryStatusEnum {
    BE_DISTRIBUTED(0,"待分配"),
    WAY_DISTRIBUTED(1,"任务中"),
    SUCCESS(2,"添加成功");

    @Getter
    private final Integer value;
    @Getter
    private final String desc;

    TaskPhoneLibraryStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        TaskPhoneLibraryStatusEnum[] enums = values();
        for (TaskPhoneLibraryStatusEnum statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.desc;
            }
        }
        return null;
    }

}
