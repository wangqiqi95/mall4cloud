package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  任务执行表状态
 */
@AllArgsConstructor
@Getter
public enum TaskExecuteStatusEnum {
    NOT_START(0, "未完成"),
    END(1, "完成");
    private final Integer value;
    private final String desc;
}