package com.mall4j.cloud.biz.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.biz.constant.task.TaskFrequencyEnum;
import com.mall4j.cloud.biz.constant.task.TaskStatusEnum;
import com.mall4j.cloud.biz.model.TaskInfo;
import com.mall4j.cloud.biz.service.TaskExecuteInfoService;
import com.mall4j.cloud.biz.service.TaskInfoService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("cpTaskInfoTask")
@Slf4j
public class CpTaskInfoTask {
    @Resource
    private TaskInfoService taskInfoService;
    @Resource
    private TaskExecuteInfoService taskExecuteInfoService;

    /**
     * 生成导购任务
     */
    @XxlJob("generateShoppingGuideTask")
    public void generateShoppingGuideTask() {
        log.info("开始生成导购任务");
        // 获取单次任务且未开始的
        List<TaskInfo> notStartTaskInfos = taskInfoService.list(Wrappers.<TaskInfo>lambdaQuery()
                .eq(TaskInfo::getTaskStatus, TaskStatusEnum.NOT_START.getValue())
                .eq(TaskInfo::getTaskFrequency, TaskFrequencyEnum.ONE_TASK.getDesc())
        );

        // 获取单次任务且为进行中的
        List<TaskInfo> progressTaskInfos = taskInfoService.list(Wrappers.<TaskInfo>lambdaQuery()
                .eq(TaskInfo::getTaskStatus, TaskStatusEnum.PROGRESS.getValue())
                .eq(TaskInfo::getTaskFrequency, TaskFrequencyEnum.ONE_TASK.getDesc())
        );

        log.info("未被生成到任务数量为:{}", notStartTaskInfos.size());
        for (TaskInfo notStartTaskInfo : notStartTaskInfos) {

        }


        // todo 二期需要获取周期任务
    }
}
