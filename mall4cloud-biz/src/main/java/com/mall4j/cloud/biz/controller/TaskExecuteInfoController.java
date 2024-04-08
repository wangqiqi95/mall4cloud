package com.mall4j.cloud.biz.controller;


import com.mall4j.cloud.biz.service.TaskExecuteInfoService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

/**
 * 任务调度信息
 */
@RestController
@RequestMapping("taskExecuteInfo")
@AllArgsConstructor
@Api(tags = "任务调度信息")
public class TaskExecuteInfoController {
    private TaskExecuteInfoService taskExecuteInfoService;

}

