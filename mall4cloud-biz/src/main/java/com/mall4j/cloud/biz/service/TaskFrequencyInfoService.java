package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskFrequencyInfo;

/**
 * 任务频率信息
 */
public interface TaskFrequencyInfoService extends IService<TaskFrequencyInfo> {
    void saveTaskFrequencyInfo(TaskInfoDTO taskInfoDTO);

    void deleteByTaskId(Long taskId);
}

