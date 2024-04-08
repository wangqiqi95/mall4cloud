package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskInfo;
import com.mall4j.cloud.biz.model.TaskRemindInfo;

/**
 * 任务提醒表
 */
public interface TaskRemindInfoService extends IService<TaskRemindInfo> {
    void  saveTaskRemindInfo(TaskInfoDTO taskInfoDTO);
}

