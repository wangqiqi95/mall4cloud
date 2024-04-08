package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务导购表导购类型
 */
@AllArgsConstructor
@Getter
public enum TaskShoppingGuideInfoTypeEnum {
    TASK_SHOPPING_GUIDE(1, "任务导购"),
    SPECIFY_SHOPPING_GUIDE(2, "指定导购"),
    ;
    private final Integer value;
    private final String desc;
}