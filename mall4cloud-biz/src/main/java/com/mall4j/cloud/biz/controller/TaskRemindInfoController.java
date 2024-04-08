package com.mall4j.cloud.biz.controller;


import com.mall4j.cloud.biz.service.TaskRemindInfoService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

/**
 * 任务提醒表
 */
@RestController
@RequestMapping("taskRemindInfo")
@AllArgsConstructor
@Api(tags = "任务提醒表")
public class TaskRemindInfoController {
    private TaskRemindInfoService taskRemindInfoService;

}

