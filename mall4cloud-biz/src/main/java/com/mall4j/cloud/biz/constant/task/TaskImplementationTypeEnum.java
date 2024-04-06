package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务执行方式
 */
@AllArgsConstructor
@Getter
public enum TaskImplementationTypeEnum {
    SHOPPING_GUIDE(1, "导购执行"),
    SYSTEM(2, "系统执行"),
    ;
    private final Integer value;
    private final String desc;
}