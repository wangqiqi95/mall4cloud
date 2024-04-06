package com.mall4j.cloud.biz.controller;


import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.service.TaskInfoService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

/**
 * 任务信息表
 */
@RestController
@RequestMapping("taskInfo")
@AllArgsConstructor
@Api(tags = "任务信息表")
public class TaskInfoController {
    private TaskInfoService taskInfoService;


    @PostMapping("saveTaskInfo")
    public ServerResponseEntity<Void> saveTaskInfo(@RequestBody TaskInfoDTO taskInfo) {
        taskInfoService.saveTaskInfo(taskInfo);
        return ServerResponseEntity.success();
    }
}

