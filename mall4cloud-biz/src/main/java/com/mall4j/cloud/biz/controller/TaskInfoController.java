package com.mall4j.cloud.biz.controller;


import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.dto.TaskInfoSearchParamDTO;
import com.mall4j.cloud.biz.dto.TaskVisitResultInfoDTO;
import com.mall4j.cloud.biz.service.TaskInfoService;
import com.mall4j.cloud.biz.service.TaskVisitResultInfoService;
import com.mall4j.cloud.biz.vo.cp.taskInfo.TaskInfoPageVO;
import com.mall4j.cloud.biz.vo.cp.taskInfo.TaskInfoVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 任务信息表
 */
@RestController
@RequestMapping("/p/taskInfo")
@AllArgsConstructor
@Api(tags = "任务信息表")
public class TaskInfoController {
    private TaskInfoService taskInfoService;

    @GetMapping("/page")
    @ApiOperation(value = "任务列表接口", notes = "任务接口")
    public ServerResponseEntity<PageVO<TaskInfoPageVO>> page(@Valid PageDTO pageDTO, TaskInfoSearchParamDTO request) {
        return ServerResponseEntity.success(taskInfoService.page(pageDTO, request));
    }


    @PostMapping("saveTaskInfo")
    @ApiOperation(value = "新增任务接口", notes = "新增任务接口")
    public ServerResponseEntity<Void> saveTaskInfo(@RequestBody TaskInfoDTO taskInfo) {
        taskInfoService.saveTaskInfo(taskInfo);
        return ServerResponseEntity.success();
    }

    @GetMapping("getTaskInfo")
    @ApiOperation(value = "任务详情接口", notes = "任务详情接口")
    @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "Long")
    public ServerResponseEntity<TaskInfoVO> getTaskInfo(@RequestParam("id") Long id) {
        return ServerResponseEntity.success(taskInfoService.getTaskInfo(id));
    }

    @GetMapping("endTask")
    @ApiOperation(value = "结束任务接口", notes = "结束任务接口")
    @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "Long")
    public ServerResponseEntity<Void> endTask(@RequestParam("id") Long id) {
        taskInfoService.endTask(id);
        return ServerResponseEntity.success();
    }

    @GetMapping("copyTask")
    @ApiOperation(value = "复制任务", notes = "复制任务")
    @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "Long")
    public ServerResponseEntity<Void> copyTask(@RequestParam("id") Long id) {
        taskInfoService.copyTask(id);
        return ServerResponseEntity.success();
    }

    @GetMapping("/downloadClientImportTemplate")
    @ApiOperation(value = "下载导入客户模板", notes = "下载导入客户模板，加企微好友时使用")
    public void downloadClientImportTemplate(HttpServletResponse response) throws IOException {
        taskInfoService.downloadClientImportTemplate(response);
    }

    @PostMapping("/importClients")
    @ApiOperation(value = "导入客户信息", notes = "请先下载模板后填写，再去导入客户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "客户信息excel", required = true),
            @ApiImplicitParam(name = "uuid", value = "临时id，新增任务时初始化。一个任务上传多次excel时需保证uuid相同", required = true)}
    )
    public void importClients(@RequestParam MultipartFile file, @RequestParam("uuid") String uuid, HttpServletResponse response) {
        taskInfoService.importClients(file, uuid, response);
    }
}

