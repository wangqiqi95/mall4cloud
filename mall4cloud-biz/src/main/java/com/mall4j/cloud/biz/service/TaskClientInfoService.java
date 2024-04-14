package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskClientInfo;
import com.mall4j.cloud.biz.vo.cp.taskInfo.ShoppingGuideTaskClientVO;

import java.util.List;

/**
 * 任务客户表
 */
public interface TaskClientInfoService extends IService<TaskClientInfo> {
    void saveTaskClientInfo(TaskInfoDTO taskInfoDTO);

    void deleteByTaskId(Long taskId);

    void copyTaskClientInfo(Long taskId);

    List<ShoppingGuideTaskClientVO> buildShoppingGuideTaskClient(Long taskId);
}

