package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskStoreInfo;

/**
 * 任务门店表
 */
public interface TaskStoreInfoService extends IService<TaskStoreInfo> {
    void saveTaskStoreInfo(TaskInfoDTO taskInfoDTO);

    void deleteByTaskId(Long taskId);

    void copyTaskStoreInfo(Long taskId);
}

