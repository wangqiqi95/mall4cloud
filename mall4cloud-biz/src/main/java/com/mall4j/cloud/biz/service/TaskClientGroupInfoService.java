package com.mall4j.cloud.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.model.TaskClientGroupInfo;

import java.util.List;

/**
 * 任务客户群表
 */
public interface TaskClientGroupInfoService extends IService<TaskClientGroupInfo> {
   void saveTaskClientGroupInfo(TaskInfoDTO taskInfoDTO);
}

