package com.mall4j.cloud.biz.controller.wx.cp;

import com.mall4j.cloud.biz.model.cp.TaskPush;
import com.mall4j.cloud.biz.service.cp.TaskPushService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 推送任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@Slf4j
@RequiredArgsConstructor
@RestController("platformTaskPushController")
@RequestMapping("/p/cp/task_push")
@Api(tags = "推送任务表")
public class TaskPushController {
    private final TaskPushService pushService;

	@GetMapping("/list")
	@ApiOperation(value = "获取推送tab列表", notes = "获取推送tab列表")
	public ServerResponseEntity<List<TaskPush>> list(@RequestParam Long taskId) {
		return ServerResponseEntity.success(pushService.listPushTaskByTaskId(taskId));
	}

}
