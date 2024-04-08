package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务提醒类型
 */
@AllArgsConstructor
@Getter
public enum TaskRemindTypeEnum {
    TASK_SHOPPING_GUIDE(1, "任务导购"),
    SPECIFY_SHOPPING_GUIDE(2, "指定导购"),
    ALL(3, "任务导购和指定导购"),
    ;
    private final Integer value;
    private final String desc;
}