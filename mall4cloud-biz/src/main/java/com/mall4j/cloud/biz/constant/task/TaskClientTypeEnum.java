package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务客户类型
 */
@AllArgsConstructor
@Getter
public enum TaskClientTypeEnum {
    ALL(1, "全部客户"),
    SPECIFY_LABEL(2, "指定标签"),
    IMPORT_CUSTOMER(3, "导入客户"),

    ;
    private final Integer value;
    private final String desc;
}
