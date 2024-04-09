package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskShoppingGuideInfo;

/**
 * 任务导购表
 */
public interface TaskShoppingGuideInfoService extends IService<TaskShoppingGuideInfo> {
    void saveShoppingGuideInfo(TaskInfoDTO taskInfoDTO);

    void deleteByTaskId(Long taskId);

    void copyShoppingGuideInfo(Long taskId);
}

