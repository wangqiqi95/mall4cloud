package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务频率
 */
@AllArgsConstructor
@Getter
public enum TaskFrequencyEnum {
    ONE_TASK(1, "单次任务"),
    CYCLE_TASK(2, "周期任务");
    private final Integer value;
    private final String desc;
}