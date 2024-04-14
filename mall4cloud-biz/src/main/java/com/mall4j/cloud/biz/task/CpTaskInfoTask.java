package com.mall4j.cloud.biz.task;

import com.mall4j.cloud.biz.service.TaskExecuteInfoService;
import com.mall4j.cloud.biz.service.TaskInfoService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("cpTaskInfoTask")
@Slf4j
public class CpTaskInfoTask {
    @Resource
    private TaskInfoService taskInfoService;
    @Resource
    private TaskExecuteInfoService taskExecuteInfoService;

    /**
     * 生成未启动任务。请确保半小时执行一次
     */
    @XxlJob("generateNotStartTask")
    public void generateShoppingGuideTask() {
        log.info("开始生成未启动任务信息");
        taskExecuteInfoService.generateNotStartTaskExecuteInfo();
        log.info("生成未启动任务信息结束");
    }

    /**
     * 生成未结束的任务。请确保每天执行一次
     */
    @XxlJob("generateProcessTaskExecuteInfo")
    public void generateProcessTaskExecuteInfo() {
        log.info("开始生成进行中的任务信息");
        taskExecuteInfoService.generateProcessTaskExecuteInfo();
        log.info("生成进行中的任务信息结束");
    }
}
