package com.mall4j.cloud.biz.service.impl;

import java.util.Date;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.bo.TaskAllocateBO;
import com.mall4j.cloud.biz.constant.task.TaskExecuteDetailAddStatusEnum;
import com.mall4j.cloud.biz.constant.task.TaskExecuteStatusEnum;
import com.mall4j.cloud.biz.dto.TaskExecuteDetailInfoSearchParamDTO;
import com.mall4j.cloud.biz.mapper.TaskExecuteDetailInfoMapper;
import com.mall4j.cloud.biz.model.TaskExecuteDetailInfo;
import com.mall4j.cloud.biz.service.TaskExecuteDetailInfoService;
import com.mall4j.cloud.biz.vo.cp.taskInfo.ShoppingGuideTaskClientGroupVO;
import com.mall4j.cloud.biz.vo.cp.taskInfo.ShoppingGuideTaskClientVO;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskExecuteDetailInfoServiceImpl extends ServiceImpl<TaskExecuteDetailInfoMapper, TaskExecuteDetailInfo> implements TaskExecuteDetailInfoService {
    @Resource
    private TaskExecuteDetailInfoMapper taskExecuteDetailInfoMapper;

    @Override
    public List<TaskExecuteDetailInfo> buildTaskExecuteDetailInfoList(TaskAllocateBO taskAllocateBO) {
        List<TaskExecuteDetailInfo> clientTaskExecuteDetailInfos = taskAllocateBO.getTaskClientInfoList().stream().map(client -> {
            TaskExecuteDetailInfo taskExecuteDetailInfo = new TaskExecuteDetailInfo();
            taskExecuteDetailInfo.setCreateTime(new Date());
            taskExecuteDetailInfo.setUpdateTime(new Date());
            taskExecuteDetailInfo.setDelFlag(DeleteEnum.NORMAL.value());
            taskExecuteDetailInfo.setExecuteId(taskAllocateBO.getExecuteId());
            taskExecuteDetailInfo.setTaskId(taskAllocateBO.getTaskId());
            taskExecuteDetailInfo.setClientId(client.getClientId());
            taskExecuteDetailInfo.setClientNickName(client.getClientNickname());
            taskExecuteDetailInfo.setClientPhone(client.getClientPhone());
            taskExecuteDetailInfo.setClientRemark(client.getClientRemark());
            taskExecuteDetailInfo.setAddStatus(TaskExecuteDetailAddStatusEnum.NOT_ADD.getValue());
            taskExecuteDetailInfo.setStatus(TaskExecuteStatusEnum.NOT_START.getValue());
            return taskExecuteDetailInfo;
        }).collect(Collectors.toList());

        List<TaskExecuteDetailInfo> clientGroupTaskExecuteDetailInfos = taskAllocateBO.getTaskClientGroupInfos().stream().map(clientGroupInfo -> {
            TaskExecuteDetailInfo taskExecuteDetailInfo = new TaskExecuteDetailInfo();
            taskExecuteDetailInfo.setCreateTime(new Date());
            taskExecuteDetailInfo.setUpdateTime(new Date());
            taskExecuteDetailInfo.setDelFlag(DeleteEnum.NORMAL.value());
            taskExecuteDetailInfo.setExecuteId(taskAllocateBO.getExecuteId());
            taskExecuteDetailInfo.setTaskId(taskAllocateBO.getTaskId());
            taskExecuteDetailInfo.setClientGroupId(clientGroupInfo.getClientGroupId());
            taskExecuteDetailInfo.setClientGroupName(clientGroupInfo.getClientGroupName());
            taskExecuteDetailInfo.setStatus(TaskExecuteStatusEnum.NOT_START.getValue());
            return taskExecuteDetailInfo;
        }).collect(Collectors.toList());
        return CollUtil.isNotEmpty(clientTaskExecuteDetailInfos) ? clientTaskExecuteDetailInfos : clientGroupTaskExecuteDetailInfos;
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

    @Override
    public List<ShoppingGuideTaskClientVO> listTaskClientInfo(TaskExecuteDetailInfoSearchParamDTO taskExecuteDetailInfoSearchParamDTO) {
        return list(Wrappers.<TaskExecuteDetailInfo>lambdaQuery()
                .eq(TaskExecuteDetailInfo::getExecuteId, taskExecuteDetailInfoSearchParamDTO.getExecuteId())
                .eq(StrUtil.isNotBlank(taskExecuteDetailInfoSearchParamDTO.getNickName()), TaskExecuteDetailInfo::getClientNickName, taskExecuteDetailInfoSearchParamDTO.getNickName())
                .eq(ObjectUtil.isNotEmpty(taskExecuteDetailInfoSearchParamDTO.getStatus()), TaskExecuteDetailInfo::getStatus, taskExecuteDetailInfoSearchParamDTO.getStatus())
        ).stream().map(item -> {
            ShoppingGuideTaskClientVO guideTaskClientVO = new ShoppingGuideTaskClientVO();
            BeanUtil.copyProperties(item, guideTaskClientVO);
            return guideTaskClientVO;

        }).collect(Collectors.toList());
    }

    @Override
    public List<ShoppingGuideTaskClientGroupVO> listTaskClientGroupInfo(TaskExecuteDetailInfoSearchParamDTO taskExecuteDetailInfoSearchParamDTO) {
        return list(Wrappers.<TaskExecuteDetailInfo>lambdaQuery()
                .eq(TaskExecuteDetailInfo::getExecuteId, taskExecuteDetailInfoSearchParamDTO.getExecuteId())
                .eq(StrUtil.isNotBlank(taskExecuteDetailInfoSearchParamDTO.getNickName()), TaskExecuteDetailInfo::getClientGroupName, taskExecuteDetailInfoSearchParamDTO.getNickName())
                .eq(ObjectUtil.isNotEmpty(taskExecuteDetailInfoSearchParamDTO.getStatus()), TaskExecuteDetailInfo::getStatus, taskExecuteDetailInfoSearchParamDTO.getStatus())
        ).stream().map(item -> {
            ShoppingGuideTaskClientGroupVO guideTaskClientVO = new ShoppingGuideTaskClientGroupVO();
            BeanUtil.copyProperties(item, guideTaskClientVO);
            return guideTaskClientVO;

        }).collect(Collectors.toList());
    }
}

