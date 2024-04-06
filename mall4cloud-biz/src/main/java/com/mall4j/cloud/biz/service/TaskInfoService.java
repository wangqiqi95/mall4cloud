package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskInfo;

/**
 * 任务信息表
 */
public interface TaskInfoService extends IService<TaskInfo> {

    /**
     * 保存任务信息
     * @param taskInfo 任务对象
     */
    void saveTaskInfo(TaskInfoDTO taskInfo);
}

