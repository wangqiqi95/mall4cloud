package com.mall4j.cloud.biz.controller;


import com.mall4j.cloud.biz.service.TaskExecuteDetailInfoService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

/**
 * 任务调度详情信息
 */
@RestController
@RequestMapping("taskExecuteDetailInfo")
@AllArgsConstructor
@Api(tags = "任务调度详情信息")
public class TaskExecuteDetailInfoController {
    private TaskExecuteDetailInfoService taskExecuteDetailInfoService;

}

