package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务客户群类型
 */
@AllArgsConstructor
@Getter
public enum TaskClientGroupTypeEnum {
    ALL(1, "全部客户群"),
    SPECIFY(2, "指定客户群"),

    ;
    private final Integer value;
    private final String desc;
}
