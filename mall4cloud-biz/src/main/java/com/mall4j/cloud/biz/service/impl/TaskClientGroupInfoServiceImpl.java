package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.TaskClientGroupTypeEnum;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskClientGroupInfoMapper;
import com.mall4j.cloud.biz.model.TaskClientGroupInfo;
import com.mall4j.cloud.biz.service.TaskClientGroupInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskClientGroupInfoServiceImpl extends ServiceImpl<TaskClientGroupInfoMapper, TaskClientGroupInfo> implements TaskClientGroupInfoService {


    @Override
    public void saveTaskClientGroupInfo(TaskInfoDTO taskInfoDTO) {
        if (ObjectUtil.isNotEmpty(taskInfoDTO.getId())) {
            deleteByTaskId(taskInfoDTO.getId());
        }
        // 保存客户群信息
        List<TaskClientGroupInfo> taskClientGroupInfos = new ArrayList<>();
        if (ObjectUtil.equals(taskInfoDTO.getTaskClientGroupType(), TaskClientGroupTypeEnum.SPECIFY.getValue())) {
            taskClientGroupInfos = taskInfoDTO.getTaskClientGroupIds().stream().map(taskClientGroupId -> {
                TaskClientGroupInfo taskClientGroupInfo = new TaskClientGroupInfo();
                taskClientGroupInfo.setCreateTime(new Date());
                taskClientGroupInfo.setUpdateTime(new Date());
                taskClientGroupInfo.setCreateBy(AuthUserContext.get().getUsername());
                taskClientGroupInfo.setUpdateBy(AuthUserContext.get().getUsername());
                taskClientGroupInfo.setDelFlag(DeleteEnum.NORMAL.value());
                taskClientGroupInfo.setTaskId(taskInfoDTO.getTaskId());
                taskClientGroupInfo.setClientGroupId(taskClientGroupId);
                return taskClientGroupInfo;
            }).collect(Collectors.toList());
        }
        saveBatch(taskClientGroupInfos);
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        remove(Wrappers.<TaskClientGroupInfo>lambdaQuery().eq(TaskClientGroupInfo::getTaskId, taskId).eq(TaskClientGroupInfo::getDelFlag, DeleteEnum.NORMAL.value()));
    }

    @Override
    public void copyTaskClientGroupInfo(Long taskId) {
        List<TaskClientGroupInfo> taskClientGroupInfos = list(Wrappers.<TaskClientGroupInfo>lambdaQuery().eq(TaskClientGroupInfo::getTaskId, taskId).eq(TaskClientGroupInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        if (CollUtil.isEmpty(taskClientGroupInfos)) {
            log.error("copyTaskClientGroupInfo时未获取到任务id为：{}的数据", taskId);
            return;
        }
        List<TaskClientGroupInfo> taskClientGroupInfoList = taskClientGroupInfos.stream().map(temp -> {
            TaskClientGroupInfo taskClientGroupInfo = new TaskClientGroupInfo();
            taskClientGroupInfo.setCreateTime(new Date());
            taskClientGroupInfo.setUpdateTime(new Date());
            taskClientGroupInfo.setCreateBy(AuthUserContext.get().getUsername());
            taskClientGroupInfo.setUpdateBy(AuthUserContext.get().getUsername());
            taskClientGroupInfo.setDelFlag(DeleteEnum.NORMAL.value());
            taskClientGroupInfo.setTaskId(taskId);
            taskClientGroupInfo.setClientGroupId(temp.getClientGroupId());
            return taskClientGroupInfo;
        }).collect(Collectors.toList());
        saveBatch(taskClientGroupInfoList);

    }
}

