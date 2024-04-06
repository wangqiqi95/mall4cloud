package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务导购类型
 */
@AllArgsConstructor
@Getter
public enum TaskShoppingGuideTypeEnum {
    ALL(1, "全部导购"),
    SPECIFY(2, "指定导购");
    private final Integer value;
    private final String desc;
}