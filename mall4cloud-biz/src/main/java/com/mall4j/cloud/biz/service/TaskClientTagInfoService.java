package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskClientTagInfo;
import com.mall4j.cloud.biz.model.TaskInfo;

/**
 * 任务客户标签表
 */
public interface TaskClientTagInfoService extends IService<TaskClientTagInfo> {
    void saveClientTagInfo(TaskInfoDTO taskInfoDTO);

    void copyClientTagInfo(Long taskId);
}

