package com.mall4j.cloud.biz.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.api.biz.constant.channels.LiveStoreSharerBindStatus;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSharerMapper;
import com.mall4j.cloud.biz.model.channels.LiveStoreSharer;
import com.mall4j.cloud.biz.service.channels.ChannelsSharerService;
import com.mall4j.cloud.biz.service.cp.CpPhoneLibraryService;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskService;
import com.mall4j.cloud.biz.service.cp.CpPhoneTaskUserService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

/**
 * 手机号引流模块-定时任务
 **/
@Component
@Slf4j
@RequestMapping
public class CpPhoneLibraryTask {
    @Autowired
    private CpPhoneTaskService cpPhoneTaskService;
    @Autowired
    private CpPhoneLibraryService cpPhoneLibraryService;
    @Autowired
    private CpPhoneTaskUserService taskUserService;


    /**
     * 每天执行一次
     * 业务处理：
     * 1. 手机号引流手机号库与外部联系人匹配
     * 2. 手机号引流-任务结束需要将任务中的手机号更新为：处理未添加成功的手机号为 待分配状态
     */
    @XxlJob("refeshLibraryAndTask")
    public void refeshLibraryAndTask(){
        log.info("定时任务执行：手机号库与外部联系人匹配、任务结束回收手机号 开始======================");
        cpPhoneLibraryService.refeshStatus();

        cpPhoneTaskService.refeshFinishTaskStatus();

        log.info("定时任务执行：手机号库与外部联系人匹配、任务结束回收手机号 结束======================");
    }

    /**
     * 每天执行一次
     * 业务处理：
     * 1. 任务员工分配客户
     */
    @XxlJob("distributeTaskUser")
    public void distributeTaskUser(){
        log.info("定时任务执行：任务员工分配客户 开始======================");
        Long taskId=null;
        taskUserService.distributeTaskUser(taskId);
        log.info("定时任务执行：任务员工分配客户 结束======================");
    }


}
