package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.bo.TaskAllocateBO;
import com.mall4j.cloud.biz.constant.task.TaskClientTypeEnum;
import com.mall4j.cloud.biz.constant.task.TaskExecuteStatusEnum;
import com.mall4j.cloud.biz.constant.task.TaskTypeEnum;
import com.mall4j.cloud.biz.mapper.TaskExecuteInfoMapper;
import com.mall4j.cloud.biz.model.*;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskExecuteInfoServiceImpl extends ServiceImpl<TaskExecuteInfoMapper, TaskExecuteInfo> implements TaskExecuteInfoService {
    @Resource
    private TaskFrequencyInfoService taskFrequencyInfoService;
    @Resource
    private TaskClientInfoService taskClientInfoService;
    @Resource
    private TaskInfoService taskInfoService;
    @Resource
    private TaskShoppingGuideInfoService taskShoppingGuideInfoService;
    @Resource
    private TaskExecuteDetailInfoService taskExecuteDetailInfoService;


    @Override
    public void generateNotStartTaskExecuteInfo() {
        // 获取前后十五分钟的未启动的任务信息
        Date current = new Date();

        DateTime fifteenMinutesBefore = DateUtil.offsetMinute(current, -15);
        DateTime fifteenMinutesAfter = DateUtil.offsetMinute(current, 15);
        log.info("当前时间为:{}，开始获取前十五分钟和后十五分钟的任务信息", DateUtil.format(current, DatePattern.NORM_DATETIME_PATTERN));

        List<Long> taskIds = taskFrequencyInfoService.list(Wrappers.<TaskFrequencyInfo>lambdaQuery()
                        .eq(TaskFrequencyInfo::getDelFlag, DeleteEnum.NORMAL.value())
                        .between(TaskFrequencyInfo::getStartTime, fifteenMinutesBefore, fifteenMinutesAfter))
                .stream().map(TaskFrequencyInfo::getTaskId).collect(Collectors.toList());
        log.info("获取到的任务数量为:{}", taskIds.size());
        if (CollUtil.isEmpty(taskIds)) {
            return;
        }

        List<TaskInfo> taskInfos = taskInfoService.list(Wrappers.<TaskInfo>lambdaQuery().eq(TaskInfo::getDelFlag, DeleteEnum.NORMAL.value()).in(TaskInfo::getId, taskIds));
        log.info("获取到的任务配置数量为:{}", taskInfos.size());
        if (CollUtil.isEmpty(taskInfos)) {
            return;
        }


        for (TaskInfo taskInfo : taskInfos) {
            TaskTypeEnum taskTypeEnum = TaskTypeEnum.getTaskTypeEnum(taskInfo.getTaskStatus());
            if (taskTypeEnum == null) {
                continue;
            }
            switch (taskTypeEnum) {
                case WORK_WECHAT_ADD_FRIEND:
                    generateNotStartWorkWechatAddFriendTask(taskInfo);
                    break;
            }

        }

    }

    @Override
    public void generateProcessTaskExecuteInfo(TaskInfo taskInfo) {

    }

    // 生成加企微好友任务
    private void generateNotStartWorkWechatAddFriendTask(TaskInfo taskInfo) {
        // 分配数量
        Integer allocatedQuantity = taskInfo.getAllocatedQuantity();

        List<TaskClientInfo> clientInfos = new ArrayList<>();

        // todo 根据标签获取客户
        if (ObjectUtil.equals(taskInfo.getTaskClientType(), TaskClientTypeEnum.SPECIFY_LABEL.getValue())) {
            // 需组装成TaskClientInfo类型

        } else {
            clientInfos = taskClientInfoService.list(Wrappers.<TaskClientInfo>lambdaQuery()
                    .eq(TaskClientInfo::getTaskId, taskInfo.getId())
                    .eq(TaskClientInfo::getDelFlag, DeleteEnum.NORMAL.value()));
            log.info("当前任务导入的导购数量为:{}", clientInfos.size());
        }

        // 获取导购信息
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos = taskShoppingGuideInfoService.list(Wrappers.<TaskShoppingGuideInfo>lambdaQuery().eq(TaskShoppingGuideInfo::getTaskId, taskInfo.getId()).eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value()));

        List<TaskAllocateBO> taskAllocateBOList = allocateSales(clientInfos, taskShoppingGuideInfos, allocatedQuantity).stream().filter(bo -> CollUtil.isNotEmpty(bo.getTaskClientInfoList())).collect(Collectors.toList());

        for (TaskAllocateBO taskAllocateBO : taskAllocateBOList) {
            // 保存任务调度主表
            TaskExecuteInfo taskExecuteInfo = initTaskExecuteInfo(taskAllocateBO);
            save(taskExecuteInfo);

            taskAllocateBO.setExecuteId(taskExecuteInfo.getId());

            // 保存调度详情
            taskExecuteDetailInfoService.saveBatch(taskExecuteDetailInfoService.buildTaskExecuteDetailInfoList(taskAllocateBO));

        }
    }

    public TaskExecuteInfo initTaskExecuteInfo(TaskAllocateBO taskAllocateBO) {
        TaskExecuteInfo taskExecuteInfo = new TaskExecuteInfo();
        taskExecuteInfo.setCreateTime(new Date());
        taskExecuteInfo.setUpdateTime(new Date());

        taskExecuteInfo.setDelFlag(DeleteEnum.NORMAL.value());
        taskExecuteInfo.setTaskId(taskAllocateBO.getTaskId());
        taskExecuteInfo.setTaskType(taskAllocateBO.getTaskType());
        taskExecuteInfo.setShoppingGuideId(taskAllocateBO.getShoppingGuideId());
        taskExecuteInfo.setStatus(TaskExecuteStatusEnum.NOT_START.getValue());
        taskExecuteInfo.setTaskTime(taskAllocateBO.getTaskTime());
        taskExecuteInfo.setClientSum(taskAllocateBO.getTaskClientInfoList().size());
        taskExecuteInfo.setSuccessClientSum(0);

        return taskExecuteInfo;
    }

    public static List<TaskAllocateBO> allocateSales(List<TaskClientInfo> taskClientInfos, List<TaskShoppingGuideInfo> taskShoppingGuideInfos, int maxSalesPerPerson) {
        int remainingCustomers = taskClientInfos.size();
        List<TaskAllocateBO> taskAllocateBOList = new ArrayList<>();
        for (int i = 0; i < taskShoppingGuideInfos.size(); i++) {
            if (remainingCustomers <= 0)
                break;


            if (remainingCustomers >= maxSalesPerPerson) {

                taskAllocateBOList.add(
                        TaskAllocateBO.builder()
                                .shoppingGuideId(taskShoppingGuideInfos.get(i).getShopGuideId())
                                .taskClientInfoList(taskClientInfos.subList(i * maxSalesPerPerson, maxSalesPerPerson * (i + 1)))
                                .build());

                remainingCustomers -= maxSalesPerPerson;
            } else {
                taskAllocateBOList.add(
                        TaskAllocateBO.builder()
                                .shoppingGuideId(taskShoppingGuideInfos.get(i).getShopGuideId())
                                .taskClientInfoList(taskClientInfos.subList(i * maxSalesPerPerson, (maxSalesPerPerson * i) + remainingCustomers))
                                .build());
                remainingCustomers = 0;
            }
        }
        return taskAllocateBOList;
    }

}

