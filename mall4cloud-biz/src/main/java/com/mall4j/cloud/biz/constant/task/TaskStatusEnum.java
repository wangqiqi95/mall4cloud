package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 */
@AllArgsConstructor
@Getter
public enum TaskStatusEnum {
    NOT_START(1, "未开始"),
    PROGRESS(2, "进行中"),
    END(3, "已结束"),
    ;
    private final Integer value;
    private final String desc;
}