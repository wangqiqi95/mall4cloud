package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 */
@AllArgsConstructor
@Getter
public enum TaskStoreTypeEnum {
    ALL(1, "全部门店"),
    SPECIFY(2, "指定门店"),
    ;
    private final Integer value;
    private final String desc;
}