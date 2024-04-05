package com.mall4j.cloud.biz.service;


import com.mall4j.cloud.biz.dto.AddTaskManagementDTO;
import com.mall4j.cloud.biz.dto.TaskManagementPageDTO;
import com.mall4j.cloud.biz.vo.TaskManagementVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;

/**
 * 任务service
 * @author hlc
 * @date 2024/4/1 11:36
 */
public interface TaskManagementService {

	ServerResponseEntity<PageVO<TaskManagementVO>> queryPageTask(TaskManagementPageDTO pageDTO);

	ServerResponseEntity update(Long id);

	ServerResponseEntity save(AddTaskManagementDTO addTaskManagementDTO);
}
