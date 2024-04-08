package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务导购表的导购类型枚举
 */
@AllArgsConstructor
@Getter
public enum TaskClientInfoTypeEnum {
    IMPORT_CLIENT(1, "任务导购"),
    ADD_REMIND_CLIENT(2, "任务提醒额外选择的");
    private final Integer value;
    private final String desc;
}