package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskClientGroupInfo;

import java.util.List;

/**
 * 任务客户群表
 */
public interface TaskClientGroupInfoService extends IService<TaskClientGroupInfo> {
    void initSaveModel(TaskClientGroupInfo taskClientGroupInfo);

    /**
     * 初始化客户群信息
     * @param taskInfoDTO 任务传输对象
     */
    List<TaskClientGroupInfo> initTaskClientGroupInfo(TaskInfoDTO taskInfoDTO);
}

