package com.mall4j.cloud.biz.controller.wx.cp;


import com.mall4j.cloud.biz.dto.cp.TaskSendDetailDTO;
import com.mall4j.cloud.biz.model.cp.TaskSendDetail;
import com.mall4j.cloud.biz.service.cp.SendTaskService;
import com.mall4j.cloud.biz.service.cp.TaskPushService;
import com.mall4j.cloud.biz.service.cp.TaskSendDetailService;
import com.mall4j.cloud.biz.vo.cp.TaskSendDetailCountVO;
import com.mall4j.cloud.biz.vo.cp.TaskSendDetailVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 群发任务明细表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
@RequiredArgsConstructor
@RestController("platformTaskSendDetailController")
@RequestMapping("/p/cp/task_send_detail")
@Api(tags = "群发任务明细表")
public class TaskSendDetailController {

    private final TaskSendDetailService taskSendDetailService;
    private final SendTaskService sendTaskService;

	@GetMapping("/page")
	@ApiOperation(value = "获取群发任务明细表列表", notes = "分页获取群发任务明细表列表")
	public ServerResponseEntity<PageVO<TaskSendDetailVO>> page(@Valid PageDTO pageDTO,TaskSendDetailDTO request) {
		return ServerResponseEntity.success(taskSendDetailService.page(pageDTO,request));
	}

    @GetMapping("/count")
    @ApiOperation(value = "发送率的统计", notes = "发送率的统计")
    public ServerResponseEntity<TaskSendDetailCountVO> count(@RequestParam Long pushId) {
        return ServerResponseEntity.success(taskSendDetailService.count(pushId));
    }

    @GetMapping
    @ApiOperation(value = "重新发送", notes = "重新发送")
    public ServerResponseEntity<Void> resend(@RequestParam Long id) {
        TaskSendDetail detail = taskSendDetailService.getById(id);
        sendTaskService.send(detail);
        return ServerResponseEntity.success();
    }

}
