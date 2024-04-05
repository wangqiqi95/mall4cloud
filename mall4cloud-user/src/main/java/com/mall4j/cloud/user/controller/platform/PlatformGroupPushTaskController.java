package com.mall4j.cloud.user.controller.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.common.cache.constant.UserCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.ExcelUtil;
import com.mall4j.cloud.common.util.annotation.ParameterValid;
import com.mall4j.cloud.user.dto.*;
import com.mall4j.cloud.user.dto.openapi.SendTaskDTO;
import com.mall4j.cloud.user.service.GroupPushTaskService;
import com.mall4j.cloud.user.service.StaffBatchSendCpMsgService;
import com.mall4j.cloud.user.task.GroupPushTask;
import com.mall4j.cloud.user.vo.*;
import com.mall4j.cloud.user.vo.openapi.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/p/group/push/task")
@Api(tags = "platform-管理后台客户群发任务API")
@Slf4j
public class PlatformGroupPushTaskController {

    @Autowired
    private GroupPushTaskService groupPushTaskService;

   @Autowired
    private GroupPushTask groupPushTask;

   @Autowired
   private StaffBatchSendCpMsgService staffBatchSendCpMsgService;

    @ParameterValid
    @PostMapping("/create")
    @ApiOperation("新建群发任务")
    public ServerResponseEntity<Void> addTask(@Valid @RequestBody AddPushTaskDTO addPushTaskDTO){
        return groupPushTaskService.addTask(addPushTaskDTO);
    }

    @GetMapping("/get/by/task/id")
    @ApiOperation("根据ID获取群发任务")
    public ServerResponseEntity<GroupPushTaskDetailVO> getTheGroupPushTask(@RequestParam("pushTaskId") Long pushTaskId){
        GroupPushTaskDetailVO theGroupPushTask = groupPushTaskService.getTheGroupPushTask(pushTaskId);
        return ServerResponseEntity.success(theGroupPushTask);
    }

    @ParameterValid
    @GetMapping("/get/son/task/statistic")
    @ApiOperation("获取群发任务相关子任务执行统计数据")
    public ServerResponseEntity<List<GroupPushTaskStatisticVO>> getStatistic(@Valid GetGroupPushTaskStatisticDTO statisticDTO){
        return groupPushTaskService.getStatistic(statisticDTO);
    }

    @GetMapping("/get/son/task/detail")
    @ApiOperation("子任务对应的销售群发记录统计")
    public ServerResponseEntity<PageVO<GroupPushSonTaskDetailStatisticVO>> getSonTaskDetailStatistic(@Valid QueryGroupPushSonTaskPageDetailDTO params){
        return groupPushTaskService.getSonTaskDetailStatistic(params);
    }

    @GetMapping("/get/son/task/terminateSonTask")
    @ApiOperation("子任务中止")
    public ServerResponseEntity<PageVO<GroupPushSonTaskDetailStatisticVO>> terminateSonTask(@Valid TerminateGroupPushSonTaskDTO params){
        staffBatchSendCpMsgService.terminateSonTask(params);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get/son/task/detail/soldExcel")
    @ApiOperation(value = "子任务对应的销售群发记录统计-导出", notes = "子任务对应的销售群发记录统计-导出")
    public void soldExcel(QueryGroupPushSonTaskPageDetailDTO dto, HttpServletResponse response) {
        List<SoldGroupPushSonTaskDetailStatisticVO> dataList=groupPushTaskService.soldSonTaskDetailStatistic(dto);
//        if(CollUtil.isEmpty(dataList)){
//            throw new LuckException("暂无数据可导出");
//        }
        String fileName="taskDetailLog-"+ DateUtil.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
        try {
            ExcelUtil.exportExcel(SoldGroupPushSonTaskDetailStatisticVO.class, dataList, fileName, response);
        } catch (IOException e) {
            log.info("数据导出失败 {}：{}", e, e.getMessage());
            throw new LuckException("数据导出失败，请重试！");
        }
    }

    @GetMapping("/get/task/detail")
    @ApiOperation("所有群发记录统计")
    public ServerResponseEntity<PageVO<GroupPushTaskDetailStatisticVO>> getTaskDetailStatistic(QueryGroupPushTaskPageDetailDTO params){
        return groupPushTaskService.getTaskDetailStatistic(params);
    }

    @PostMapping("/get/task/record/statistic")
    @ApiOperation("数据分析图表统计")
    public ServerResponseEntity<List<GroupPushTaskRecordStatisticVO>> getTaskRecordStatistic(@Valid @RequestBody QueryGroupPushRecordDTO params){
        List<GroupPushTaskRecordStatisticVO> result = groupPushTaskService.getTaskRecordStatistic(params);
        return ServerResponseEntity.success(result);
    }

    @GetMapping("/get/task/test")
    @ApiOperation("所有群发记录统计")
    public ServerResponseEntity<Void> test(@RequestParam Integer type){
        if (type == 0) {
            groupPushTask.pushTask();
        } else if(type == 1){
            groupPushTask.syncGroupMessageSendResult();
        }else if(type == 3){
            RedisUtil.deleteBatchKeys(UserCacheNames.CP_EXTERN_CONTACT_ATTACHMENT_LIST+"*");
            RedisUtil.del(UserCacheNames.CP_EXTERN_CONTACT_ATTACHMENT_LIST+"*");
        }
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @ApiOperation("编辑群发任务")
    public ServerResponseEntity editTask(@Valid @RequestBody AddPushTaskDTO editPushTaskDTO){
        return groupPushTaskService.editTask(editPushTaskDTO);
    }

    @PostMapping("/enable")
    @ApiOperation("启用群发任务")
    public ServerResponseEntity enableTask(@RequestParam Long pushTaskId){
        // 是否要校验该次启用为首次启用
        return groupPushTaskService.enableOrDisableTask(pushTaskId);
    }

    @PostMapping("/disable")
    @ApiOperation("禁用群发任务")
    public ServerResponseEntity disableTask(@RequestParam Long pushTaskId){
        return groupPushTaskService.disableTaskTask(pushTaskId);
    }

    @PostMapping("/remove/task/by/id")
    @ApiOperation("删除群发任务")
    public ServerResponseEntity<GroupPushTaskStatisticVO> removeTask(@RequestParam Long pushTaskId){
        return groupPushTaskService.removeTask(pushTaskId);
    }

    @PostMapping("/crmSendTask")
    @ApiOperation("测试-CDP执行发送任务")
    public ServerResponseEntity<GroupPushTaskStatisticVO> crmSendTask(@Valid @RequestBody SendTaskDTO request){
        ApiResponse apiResponse=groupPushTaskService.crmSendTaskNew(request);
//        ApiResponse apiResponse=groupPushTaskService.crmSendTask(request);
        return ServerResponseEntity.showFailMsg(JSON.toJSONString(apiResponse));
    }


  /*  @PostMapping("/statistic/export/to/download/center")
    @ApiOperation("下载群发任务统计数据")
    public ServerResponseEntity exportMarkingUser(@RequestBody DownloadGroupStatisticsExcelDataDTO dataDTO, HttpServletResponse response){
        return groupPushTaskService.downloadData(dataDTO.getPushTaskId(), dataDTO.getSonTaskId(), response);
    }*/

    @ParameterValid
    @PostMapping("/get/page")
    @ApiOperation("获取群发任务分页数据")
    public ServerResponseEntity<PageVO<GroupPushTaskPageVO>> getPage(@Valid @RequestBody QueryGroupPushTaskPageDTO pageDTO){
        return groupPushTaskService.getPage(pageDTO);
    }
}
