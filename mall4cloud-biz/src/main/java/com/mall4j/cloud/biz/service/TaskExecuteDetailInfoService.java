package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.bo.TaskAllocateBO;
import com.mall4j.cloud.biz.model.TaskClientInfo;
import com.mall4j.cloud.biz.model.TaskExecuteDetailInfo;

import java.util.List;

/**
 * 任务调度详情信息
 */
public interface TaskExecuteDetailInfoService extends IService<TaskExecuteDetailInfo> {
    List<TaskExecuteDetailInfo> buildTaskExecuteDetailInfoList(TaskAllocateBO taskAllocateBO);

    /**
     * 完成任务调度详情
     * @param id 任务调度详情id
     */
    void endTaskExecuteDetailInfo(Long id);

    /**
     * 更新加企微好友状态
     * @param id 任务调度详情id
     * @param addStatus 添加状态
     */
    void updateTaskExecuteDetailAddStatus(Long id, Integer addStatus);
}

