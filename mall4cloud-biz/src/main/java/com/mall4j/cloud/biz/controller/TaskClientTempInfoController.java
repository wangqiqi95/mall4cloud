package com.mall4j.cloud.biz.controller;


import com.mall4j.cloud.biz.service.TaskClientTempInfoService;
import com.mall4j.cloud.biz.vo.TaskClientTempInfoVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 任务客户临时表
 */
@RestController
@RequestMapping("/p/taskClientTempInfo")
@AllArgsConstructor
@Api(tags = "任务客户临时表")
public class TaskClientTempInfoController {
    private TaskClientTempInfoService taskClientTempInfoService;

    @GetMapping("listTaskClientTempInfoByUuid")
    @ApiOperation(value = "获取当前任务导入的临时客户", notes = "获取当前任务导入的临时客户")
    @ApiImplicitParam(name = "uuid", value = "当前任务临时id", required = true, dataType = "String")
    public ServerResponseEntity<List<TaskClientTempInfoVO>> listTaskClientTempInfoByUuid(@RequestParam("uuid") String uuid) {
        return ServerResponseEntity.success(taskClientTempInfoService.listTaskClientTempInfoByUuid(uuid));
    }


}

