package com.mall4j.cloud.biz.controller;

import com.mall4j.cloud.biz.dto.AddTaskManagementDTO;
import com.mall4j.cloud.biz.dto.TaskManagementPageDTO;
import com.mall4j.cloud.biz.service.TaskManagementService;
import com.mall4j.cloud.biz.vo.TaskManagementVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 *
 * @author hlc
 * @date 2024/4/1 11:41

 */
@RestController("taskManagementController")
@AllArgsConstructor
@RequestMapping("/p/task_management")
@Api(tags = "任务管理")
public class TaskManagementController {


    private final TaskManagementService taskManagementService;


	private final MapperFacade mapperFacade;

	@GetMapping("/page")
	@ApiOperation(value = "获取列表", notes = "分页获取任务列表")
	public ServerResponseEntity<PageVO<TaskManagementVO>> page(@Valid TaskManagementPageDTO pageDTO) {
		return taskManagementService.queryPageTask(pageDTO);
	}


	@GetMapping("/update")
	@ApiOperation(value = "修改任务状态", notes = "修改任务状态")
	public ServerResponseEntity update(@Valid Long id) {
		return taskManagementService.update(id);
	}

	@GetMapping("/save")
	@ApiOperation(value = "新增任务", notes = "新增任务")
	public ServerResponseEntity save(@Valid AddTaskManagementDTO addTaskManagementDTO) {
		return taskManagementService.save(addTaskManagementDTO);
	}

}
