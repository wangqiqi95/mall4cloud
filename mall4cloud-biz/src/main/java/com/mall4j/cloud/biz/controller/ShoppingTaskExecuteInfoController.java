package com.mall4j.cloud.biz.controller;

import com.mall4j.cloud.biz.dto.TaskExecuteDetailInfoSearchParamDTO;
import com.mall4j.cloud.biz.dto.TaskExecuteInfoSearchParamDTO;
import com.mall4j.cloud.biz.dto.TaskVisitResultInfoDTO;
import com.mall4j.cloud.biz.model.TaskVisitResultInfo;
import com.mall4j.cloud.biz.service.TaskExecuteDetailInfoService;
import com.mall4j.cloud.biz.service.TaskExecuteInfoService;
import com.mall4j.cloud.biz.service.TaskVisitResultInfoService;
import com.mall4j.cloud.biz.vo.cp.taskInfo.*;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/s/taskExecuteInfo")
@AllArgsConstructor
@Api(tags = "任务调度信息")
public class ShoppingTaskExecuteInfoController {
    private TaskExecuteInfoService taskExecuteInfoService;
    private TaskExecuteDetailInfoService taskExecuteDetailInfoService;
    private TaskVisitResultInfoService taskVisitResultInfoService;

    @GetMapping("/page")
    @ApiOperation(value = "获取导购任务", notes = "获取导购任务接口")
    public ServerResponseEntity<PageVO<TaskExecuteInfoVO>> page(@Valid PageDTO pageDTO, TaskExecuteInfoSearchParamDTO request) {
        return ServerResponseEntity.success(taskExecuteInfoService.page(pageDTO, request));
    }

    @GetMapping("/getTaskExecuteDetailInfo")
    @ApiOperation(value = "获取任务详情", notes = "获取任务详情")
    @ApiImplicitParam(name = "executeId", value = "任务调度id，分页查询任务时返回的id")
    public ServerResponseEntity<TaskExecuteDetailInfoVO> getTaskExecuteDetailInfo(@RequestParam("executeId") Long executeId) {
        return ServerResponseEntity.success(taskExecuteInfoService.getTaskExecuteDetailInfo(executeId));
    }

    @PostMapping("/listTaskClientInfo")
    @ApiOperation(value = "获取任务客户信息", notes = "获取任务客户信息")
    @ApiImplicitParam(name = "taskExecuteDetailInfoSearchParamDTO", value = "查询条件")
    public ServerResponseEntity<List<ShoppingGuideTaskClientVO>> listTaskClientInfo(@RequestBody TaskExecuteDetailInfoSearchParamDTO taskExecuteDetailInfoSearchParamDTO) {
        return ServerResponseEntity.success(taskExecuteDetailInfoService.listTaskClientInfo(taskExecuteDetailInfoSearchParamDTO));
    }

    @PostMapping("/listTaskClientGroupInfo")
    @ApiOperation(value = "获取任务客户群信息", notes = "获取任务客户群信息")
    @ApiImplicitParam(name = "taskExecuteDetailInfoSearchParamDTO", value = "查询条件")
    public ServerResponseEntity<List<ShoppingGuideTaskClientGroupVO>> listTaskClientGroupInfo(@RequestBody TaskExecuteDetailInfoSearchParamDTO taskExecuteDetailInfoSearchParamDTO) {
        return ServerResponseEntity.success(taskExecuteDetailInfoService.listTaskClientGroupInfo(taskExecuteDetailInfoSearchParamDTO));
    }

    @PostMapping("/saveTaskVisitResultInfo")
    @ApiOperation(value = "保存任务反馈信息", notes = "保存任务反馈信息")
    @ApiImplicitParam(name = "taskVisitResultInfoDTO", value = "任务返回信息")
    public ServerResponseEntity<Void> saveTaskVisitResultInfo(@RequestBody TaskVisitResultInfoDTO taskVisitResultInfoDTO) {
        taskVisitResultInfoService.saveTaskVisitResultInfo(taskVisitResultInfoDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/getTaskVisitResultInfo")
    @ApiOperation(value = "获取回访任务的反馈信息", notes = "获取回访任务的反馈信息")
    @ApiImplicitParam(name = "executeDetailId", value = "任务调度详情id")
    public ServerResponseEntity<TaskVisitResultInfo> getTaskVisitResultInfo(@RequestParam("executeDetailId") Long executeDetailId) {
        return ServerResponseEntity.success(taskVisitResultInfoService.getTaskVisitResultInfo(executeDetailId));
    }
}
