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
     */
    void generateNotStartTaskExecuteInfo();

    /**
     * 生成进行中任务的导购信息
     * @param taskInfo
     */
    void generateProcessTaskExecuteInfo(TaskInfo taskInfo);
}

