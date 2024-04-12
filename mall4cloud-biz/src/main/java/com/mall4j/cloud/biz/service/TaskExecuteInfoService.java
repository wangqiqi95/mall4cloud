package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.TaskExecuteInfo;
import com.mall4j.cloud.biz.model.TaskInfo;

/**
 * 任务调度信息
 */
public interface TaskExecuteInfoService extends IService<TaskExecuteInfo> {
    /**
     * 生成未启动任务的导购任务
     * 注意：请确保定时任务的频率为半小时执行一次
     */
    void generateNotStartTaskExecuteInfo();

    /**
     * 生成进行中的任务信息
     * 注意：请确保每天执行一次
     */
    void generateProcessTaskExecuteInfo();
}

