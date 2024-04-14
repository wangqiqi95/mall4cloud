package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskVisitResultInfoDTO;
import com.mall4j.cloud.biz.model.TaskVisitResultInfo;

/**
 * 任务回访信息信息
 */
public interface TaskVisitResultInfoService extends IService<TaskVisitResultInfo> {
    void saveTaskVisitResultInfo(TaskVisitResultInfoDTO taskVisitResultInfoDTO);

    TaskVisitResultInfo getTaskVisitResultInfo(Long executeDetailId);
}

