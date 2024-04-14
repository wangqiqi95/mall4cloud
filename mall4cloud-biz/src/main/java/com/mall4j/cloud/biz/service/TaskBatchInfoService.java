package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskExecuteInfoSearchParamDTO;
import com.mall4j.cloud.biz.model.TaskBatchInfo;
import com.mall4j.cloud.biz.model.TaskInfo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 任务批次信息
 */
public interface TaskBatchInfoService extends IService<TaskBatchInfo> {
    TaskBatchInfo saveTaskBatchInfo(TaskInfo taskInfo);

    PageVO<TaskBatchInfo> taskBatchPage(PageDTO pageDTO, TaskExecuteInfoSearchParamDTO taskExecuteInfoSearchParamDTO);
}

