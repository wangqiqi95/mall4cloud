package com.mall4j.cloud.biz.service.impl;

import java.util.Date;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.bo.TaskAllocateBO;
import com.mall4j.cloud.biz.constant.task.TaskExecuteDetailAddStatusEnum;
import com.mall4j.cloud.biz.constant.task.TaskExecuteStatusEnum;
import com.mall4j.cloud.biz.mapper.TaskExecuteDetailInfoMapper;
import com.mall4j.cloud.biz.model.TaskExecuteDetailInfo;
import com.mall4j.cloud.biz.service.TaskExecuteDetailInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskExecuteDetailInfoServiceImpl extends ServiceImpl<TaskExecuteDetailInfoMapper, TaskExecuteDetailInfo> implements TaskExecuteDetailInfoService {

    @Override
    public List<TaskExecuteDetailInfo> buildTaskExecuteDetailInfoList(TaskAllocateBO taskAllocateBO) {
        return taskAllocateBO.getTaskClientInfoList().stream().map(client -> {
            TaskExecuteDetailInfo taskExecuteDetailInfo = new TaskExecuteDetailInfo();
            taskExecuteDetailInfo.setCreateTime(new Date());
            taskExecuteDetailInfo.setUpdateTime(new Date());
            taskExecuteDetailInfo.setDelFlag(DeleteEnum.NORMAL.value());
            taskExecuteDetailInfo.setExecuteId(taskAllocateBO.getExecuteId());
            taskExecuteDetailInfo.setTaskId(taskAllocateBO.getTaskId());
            taskExecuteDetailInfo.setClientId(client.getClientId());
            taskExecuteDetailInfo.setAddStatus(TaskExecuteDetailAddStatusEnum.NOT_ADD.getValue());
            taskExecuteDetailInfo.setStatus(TaskExecuteStatusEnum.NOT_START.getValue());
            return taskExecuteDetailInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public void endTaskExecuteDetailInfo(Long id) {
        TaskExecuteDetailInfo taskExecuteDetailInfo = getById(id);
        Assert.isTrue(ObjectUtil.isEmpty(taskExecuteDetailInfo), "未获取对应的任务详情");

        taskExecuteDetailInfo.setEndTime(new Date());
        taskExecuteDetailInfo.setUpdateBy(AuthUserContext.get().getUsername());
        taskExecuteDetailInfo.setUpdateTime(new Date());
        updateById(taskExecuteDetailInfo);
    }

    @Override
    public void updateTaskExecuteDetailAddStatus(Long id, Integer addStatus) {
        TaskExecuteDetailInfo taskExecuteDetailInfo = getById(id);
        Assert.isTrue(ObjectUtil.isEmpty(taskExecuteDetailInfo), "未获取对应的任务详情");

        taskExecuteDetailInfo.setAddStatus(addStatus);
        taskExecuteDetailInfo.setUpdateBy(AuthUserContext.get().getUsername());
        taskExecuteDetailInfo.setUpdateTime(new Date());
        updateById(taskExecuteDetailInfo);
    }
}

