package com.mall4j.cloud.biz.constant.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调度详情添加企微好友状态
 */
@AllArgsConstructor
@Getter
public enum TaskExecuteDetailAddStatusEnum {
    NOT_ADD(0,"未添加"),
    ADDED(1,"已添加"),
    ADD_FAIL(2,"添加失败"),
    ;
    private final Integer value;
    private final String desc;
}