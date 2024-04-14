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
import com.mall4j.cloud.biz.constant.task.*;
import com.mall4j.cloud.biz.dto.TaskClientGroupInfoDTO;
import com.mall4j.cloud.biz.dto.TaskClientInfoDTO;
import com.mall4j.cloud.biz.dto.TaskExecuteDetailInfoSearchParamDTO;
import com.mall4j.cloud.biz.dto.TaskExecuteInfoSearchParamDTO;
import com.mall4j.cloud.biz.mapper.TaskExecuteInfoMapper;
import com.mall4j.cloud.biz.model.*;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.biz.vo.cp.taskInfo.*;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
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
    @Resource
    private TaskExecuteInfoMapper taskExecuteInfoMapper;
    @Resource
    private TaskClientGroupInfoService taskClientGroupInfoService;


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

        List<TaskInfo> taskInfos = taskInfoService.list(Wrappers.<TaskInfo>lambdaQuery()
                .eq(TaskInfo::getDelFlag, DeleteEnum.NORMAL.value())
                .eq(TaskInfo::getTaskStatus, TaskStatusEnum.NOT_START.getValue())
                .in(TaskInfo::getId, taskIds));
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
                case SHARE_MATERIAL:
                    TaskShareTypeEnum taskShareTypeEnum = TaskShareTypeEnum.getEnum(taskInfo.getShareType());
                    if (taskShareTypeEnum == null) {
                        break;
                    }
                    switch (taskShareTypeEnum) {
                        // 除分享客户群外的任务生成规则一致
                        case WORK_WECHAT_CUSTOMER_BASE:
                            generateNotStartClientGroupTask(taskInfo);
                            break;
                        default:
                            generateNotStartClientTask(taskInfo);
                            break;
                    }
                case VISIT_CUSTOMER:
                    generateNotStartWorkWechatAddFriendTask(taskInfo);
                    break;
            }

            // 更新任务状态为进行中
            taskInfoService.updateTaskStatus(taskInfo.getId(), TaskStatusEnum.PROGRESS.getValue());
        }

    }

    @Override
    public void generateProcessTaskExecuteInfo() {
        // 获取今天的任务
        Date current = new Date();
        List<Long> taskIds = taskFrequencyInfoService.list(Wrappers.<TaskFrequencyInfo>lambdaQuery()
                        .eq(TaskFrequencyInfo::getDelFlag, DeleteEnum.NORMAL.value())
                        .ge(TaskFrequencyInfo::getStartTime, current)
                        .le(TaskFrequencyInfo::getEndTime, current))
                .stream().map(TaskFrequencyInfo::getTaskId).collect(Collectors.toList());
        log.info("获取到的任务数量为:{}", taskIds.size());
        if (CollUtil.isEmpty(taskIds)) {
            return;
        }

        List<TaskInfo> taskInfos = taskInfoService.list(Wrappers.<TaskInfo>lambdaQuery()
                .eq(TaskInfo::getDelFlag, DeleteEnum.NORMAL.value())
                .eq(TaskInfo::getTaskStatus, TaskStatusEnum.PROGRESS.getValue())
                .in(TaskInfo::getId, taskIds));
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
                    generateProcessWorkWechatAddFriendTask(taskInfo);
                    break;
                case SHARE_MATERIAL:
                    TaskShareTypeEnum taskShareTypeEnum = TaskShareTypeEnum.getEnum(taskInfo.getShareType());
                    if (taskShareTypeEnum == null) {
                        break;
                    }
                    switch (taskShareTypeEnum) {
                        // 除分享客户群外的任务生成规则一致
                        case WORK_WECHAT_CUSTOMER_BASE:
                            generateProcessClientGroupTask(taskInfo);
                            break;
                        default:
                            generateProcessClientTask(taskInfo);
                            break;
                    }
                case VISIT_CUSTOMER:
                    generateProcessClientTask(taskInfo);
                    break;
            }

            // 更新任务状态为进行中
            taskInfoService.updateTaskStatus(taskInfo.getId(), TaskStatusEnum.PROGRESS.getValue());
        }

    }

    /**
     * 生成未启动的任务===》加企微好友任务
     * @param taskInfo 任务信息
     */
    private void generateNotStartWorkWechatAddFriendTask(TaskInfo taskInfo) {
        // 分配数量
        Integer allocatedQuantity = taskInfo.getAllocatedQuantity();

        List<TaskClientInfo> clientInfos = new ArrayList<>();
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos;

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
        if (ObjectUtil.equals(taskInfo.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.ALL.getValue())) {
            // todo 此处需要判断是否获取所有导购
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list();
        } else {
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list(Wrappers.<TaskShoppingGuideInfo>lambdaQuery().eq(TaskShoppingGuideInfo::getTaskId, taskInfo.getId()).eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        }


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

    /**
     * 生成未启动的客户任务
     * @param taskInfo 任务信息
     */
    private void generateNotStartClientTask(TaskInfo taskInfo) {

        List<TaskClientInfo> clientInfos;
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos;

        // todo 根据标签获取客户
        if (ObjectUtil.equals(taskInfo.getTaskClientType(), TaskClientTypeEnum.SPECIFY_LABEL.getValue())) {
            clientInfos = Collections.emptyList();
        } else if (ObjectUtil.equals(taskInfo.getTaskClientType(), TaskClientTypeEnum.ALL.getValue())) {
            // todo 获取全部用户
            clientInfos = Collections.emptyList();
        }

        // 获取导购信息
        if (ObjectUtil.equals(taskInfo.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.ALL.getValue())) {
            // todo 此处需要判断是否获取所有导购
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list();
        } else {
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list(Wrappers.<TaskShoppingGuideInfo>lambdaQuery().eq(TaskShoppingGuideInfo::getTaskId, taskInfo.getId()).eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        }

        // todo 需要接口判断与客户最近联系的导购
        List<TaskAllocateBO> taskAllocateBOList = Collections.emptyList();

        for (TaskAllocateBO taskAllocateBO : taskAllocateBOList) {
            // 保存任务调度主表
            TaskExecuteInfo taskExecuteInfo = initTaskExecuteInfo(taskAllocateBO);
            save(taskExecuteInfo);

            taskAllocateBO.setExecuteId(taskExecuteInfo.getId());

            // 保存调度详情
            taskExecuteDetailInfoService.saveBatch(taskExecuteDetailInfoService.buildTaskExecuteDetailInfoList(taskAllocateBO));

        }
    }

    /**
     * 生成未启动的分享客户群任务
     * @param taskInfo 任务信息
     */
    private void generateNotStartClientGroupTask(TaskInfo taskInfo) {

        List<TaskClientGroupInfo> taskClientGroupInfos;
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos;

        // todo 根据标签获取客户群
        if (ObjectUtil.equals(taskInfo.getTaskClientGroupType(), TaskClientGroupTypeEnum.SPECIFY.getValue())) {
            taskClientGroupInfos = Collections.emptyList();
        } else if (ObjectUtil.equals(taskInfo.getTaskClientGroupType(), TaskClientGroupTypeEnum.ALL.getValue())) {
            // todo 获取全部用户群
            taskClientGroupInfos = Collections.emptyList();
        }

        // 获取导购信息
        if (ObjectUtil.equals(taskInfo.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.ALL.getValue())) {
            // todo 此处需要判断是否获取所有导购
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list();
        } else {
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list(Wrappers.<TaskShoppingGuideInfo>lambdaQuery().eq(TaskShoppingGuideInfo::getTaskId, taskInfo.getId()).eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        }

        //todo 客户群分配规则
        List<TaskAllocateBO> taskAllocateBOList = Collections.emptyList();

        for (TaskAllocateBO taskAllocateBO : taskAllocateBOList) {
            // 保存任务调度主表
            TaskExecuteInfo taskExecuteInfo = initTaskExecuteInfo(taskAllocateBO);
            save(taskExecuteInfo);

            taskAllocateBO.setExecuteId(taskExecuteInfo.getId());

            // 保存调度详情
            taskExecuteDetailInfoService.saveBatch(taskExecuteDetailInfoService.buildTaskExecuteDetailInfoList(taskAllocateBO));

        }
    }

    /**
     * 生成进行中的任务===》加企微好友任务
     * 区分于未启动的任务，进行中的任务在生成时需要过滤已使用的客户，且不更新任务配置表的状态
     * @param taskInfo 任务信息
     */
    private void generateProcessWorkWechatAddFriendTask(TaskInfo taskInfo) {
        // 分配数量
        Integer allocatedQuantity = taskInfo.getAllocatedQuantity();

        List<TaskClientInfo> clientInfos = new ArrayList<>();
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos;

        // todo 根据标签获取客户
        if (ObjectUtil.equals(taskInfo.getTaskClientType(), TaskClientTypeEnum.SPECIFY_LABEL.getValue())) {
            // 需组装成TaskClientInfo类型

        } else {
            clientInfos = taskClientInfoService.list(Wrappers.<TaskClientInfo>lambdaQuery()
                    .eq(TaskClientInfo::getTaskId, taskInfo.getId())
                    .eq(TaskClientInfo::getDelFlag, DeleteEnum.NORMAL.value()));
            log.info("当前任务导入的导购数量为:{}", clientInfos.size());
        }

        // 获取已生成的客户信息
        List<String> genClients = taskExecuteDetailInfoService.list(Wrappers.<TaskExecuteDetailInfo>lambdaQuery()
                        .eq(TaskExecuteDetailInfo::getTaskId, taskInfo.getId())
                        .eq(TaskExecuteDetailInfo::getDelFlag, DeleteEnum.NORMAL.value()))
                .stream().map(TaskExecuteDetailInfo::getClientId).distinct().collect(Collectors.toList());

        // 获取导购信息
        // 获取导购信息
        if (ObjectUtil.equals(taskInfo.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.ALL.getValue())) {
            // todo 此处需要判断是否获取所有导购
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list();
        } else {
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list(Wrappers.<TaskShoppingGuideInfo>lambdaQuery().eq(TaskShoppingGuideInfo::getTaskId, taskInfo.getId()).eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        }

        List<TaskAllocateBO> taskAllocateBOList = allocateSales(clientInfos.stream().filter(clientInfo -> !CollUtil.contains(genClients, clientInfo.getClientId())).collect(Collectors.toList()),
                taskShoppingGuideInfos, allocatedQuantity).stream().filter(bo -> CollUtil.isNotEmpty(bo.getTaskClientInfoList())).collect(Collectors.toList());

        for (TaskAllocateBO taskAllocateBO : taskAllocateBOList) {
            // 保存任务调度主表
            TaskExecuteInfo taskExecuteInfo = initTaskExecuteInfo(taskAllocateBO);
            save(taskExecuteInfo);

            taskAllocateBO.setExecuteId(taskExecuteInfo.getId());
            // 保存调度详情
            taskExecuteDetailInfoService.saveBatch(taskExecuteDetailInfoService.buildTaskExecuteDetailInfoList(taskAllocateBO));
        }
    }

    /**
     * 生成进行中的客户任务
     * 区分于未启动的任务，进行中的任务在生成时需要过滤已使用的客户，且不更新任务配置表的状态
     * @param taskInfo 任务信息
     */
    private void generateProcessClientTask(TaskInfo taskInfo) {

        List<TaskClientInfo> clientInfos = new ArrayList<>();
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos;

        // todo 根据标签获取客户
        if (ObjectUtil.equals(taskInfo.getTaskClientType(), TaskClientTypeEnum.SPECIFY_LABEL.getValue())) {
            // 需组装成TaskClientInfo类型

        } else if (ObjectUtil.equals(taskInfo.getTaskClientType(), TaskClientTypeEnum.ALL.getValue())) {
            clientInfos = Collections.emptyList();
        }

        // 获取已生成的客户信息
        List<String> genClients = taskExecuteDetailInfoService.list(Wrappers.<TaskExecuteDetailInfo>lambdaQuery()
                        .eq(TaskExecuteDetailInfo::getTaskId, taskInfo.getId())
                        .eq(TaskExecuteDetailInfo::getDelFlag, DeleteEnum.NORMAL.value()))
                .stream().map(TaskExecuteDetailInfo::getClientId).distinct().collect(Collectors.toList());

        // 获取导购信息
        if (ObjectUtil.equals(taskInfo.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.ALL.getValue())) {
            // todo 此处需要判断是否获取所有导购
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list();
        } else {
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list(Wrappers.<TaskShoppingGuideInfo>lambdaQuery().eq(TaskShoppingGuideInfo::getTaskId, taskInfo.getId()).eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        }

        // todo 需要接口判断与客户最近联系的导购
        List<TaskAllocateBO> taskAllocateBOList = Collections.emptyList();

        for (TaskAllocateBO taskAllocateBO : taskAllocateBOList) {
            // 保存任务调度主表
            TaskExecuteInfo taskExecuteInfo = initTaskExecuteInfo(taskAllocateBO);
            save(taskExecuteInfo);

            taskAllocateBO.setExecuteId(taskExecuteInfo.getId());
            // 保存调度详情
            taskExecuteDetailInfoService.saveBatch(taskExecuteDetailInfoService.buildTaskExecuteDetailInfoList(taskAllocateBO));
        }
    }

    /**
     * 生成进行中的客户群任务
     * @param taskInfo 任务信息
     */
    private void generateProcessClientGroupTask(TaskInfo taskInfo) {

        List<TaskClientGroupInfo> clientGroupInfos = new ArrayList<>();
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos;

        // todo 根据标签获取客户群
        if (ObjectUtil.equals(taskInfo.getTaskClientGroupType(), TaskClientGroupTypeEnum.SPECIFY.getValue())) {

        } else if (ObjectUtil.equals(taskInfo.getTaskClientGroupType(), TaskClientGroupTypeEnum.ALL.getValue())) {
            // todo获取所有客户群
            clientGroupInfos = Collections.emptyList();
        }

        // 获取已生成的客户信息
        List<String> genClients = taskExecuteDetailInfoService.list(Wrappers.<TaskExecuteDetailInfo>lambdaQuery()
                        .eq(TaskExecuteDetailInfo::getTaskId, taskInfo.getId())
                        .eq(TaskExecuteDetailInfo::getDelFlag, DeleteEnum.NORMAL.value()))
                .stream().map(TaskExecuteDetailInfo::getClientId).distinct().collect(Collectors.toList());

        // 获取导购信息
        if (ObjectUtil.equals(taskInfo.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.ALL.getValue())) {
            // todo 此处需要判断是否获取所有导购
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list();
        } else {
            taskShoppingGuideInfos = taskShoppingGuideInfoService.list(Wrappers.<TaskShoppingGuideInfo>lambdaQuery().eq(TaskShoppingGuideInfo::getTaskId, taskInfo.getId()).eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        }

        // todo 需要接口判断与客户最近联系的导购
        List<TaskAllocateBO> taskAllocateBOList = Collections.emptyList();

        for (TaskAllocateBO taskAllocateBO : taskAllocateBOList) {
            // 保存任务调度主表
            TaskExecuteInfo taskExecuteInfo = initTaskExecuteInfo(taskAllocateBO);
            save(taskExecuteInfo);

            taskAllocateBO.setExecuteId(taskExecuteInfo.getId());
            // 保存调度详情
            taskExecuteDetailInfoService.saveBatch(taskExecuteDetailInfoService.buildTaskExecuteDetailInfoList(taskAllocateBO));
        }
    }

    /**
     * 初始化任务调度信息
     * @param taskAllocateBO 业务参数
     */
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

    /**
     * 给导购分配客户
     * @param taskClientInfos 客户信息
     * @param taskShoppingGuideInfos 导购信息
     * @param maxSalesPerPerson 导购可分配的数量
     */
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


    @Override
    public PageVO<TaskExecuteInfoVO> page(PageDTO pageDTO, TaskExecuteInfoSearchParamDTO taskExecuteInfoSearchParamDTO) {
        return PageUtil.doPage(pageDTO, () -> taskExecuteInfoMapper.list(taskExecuteInfoSearchParamDTO));
    }

    @Override
    public TaskExecuteDetailInfoVO getTaskExecuteDetailInfo(Long executeId) {
        Assert.isTrue(ObjectUtil.isEmpty(executeId), "参数异常");

        TaskExecuteInfo taskExecuteInfo = getById(executeId);
        Assert.isTrue(ObjectUtil.isEmpty(taskExecuteInfo), "任务获取异常");

        // 组装任务信息
        ShoppingGuideTaskDetailVO shoppingGuideTaskDetail = taskInfoService.buildShoppingGuideTaskDetailVO(taskExecuteInfo.getTaskId());
        return TaskExecuteDetailInfoVO.builder()
                .shoppingGuideTaskDetail(shoppingGuideTaskDetail)
                .build();
    }
}

