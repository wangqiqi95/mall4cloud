package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskMaterialInfo;

/**
 * 任务素材信息表
 */
public interface TaskMaterialInfoService extends IService<TaskMaterialInfo> {
    void saveTaskMaterialInfo(TaskInfoDTO taskInfoDTO);

    void deleteByTaskId(Long taskId);

    void copyTaskMaterialInfo(Long taskId);
}

