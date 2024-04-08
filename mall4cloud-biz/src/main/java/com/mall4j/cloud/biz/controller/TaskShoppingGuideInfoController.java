package com.mall4j.cloud.biz.controller;


import com.mall4j.cloud.biz.service.TaskShoppingGuideInfoService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

/**
 * 任务导购表
 */
@RestController
@RequestMapping("taskShoppingGuideInfo")
@AllArgsConstructor
@Api(tags = "任务导购表")
public class TaskShoppingGuideInfoController {
    private TaskShoppingGuideInfoService taskShoppingGuideInfoService;

}

