package com.mall4j.cloud.biz.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskClientTagInfoMapper;
import com.mall4j.cloud.biz.model.TaskClientTagInfo;
import com.mall4j.cloud.biz.model.TaskStoreInfo;
import com.mall4j.cloud.biz.service.TaskClientTagInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskClientTagInfoServiceImpl extends ServiceImpl<TaskClientTagInfoMapper, TaskClientTagInfo> implements TaskClientTagInfoService {
    @Override
    public void saveClientTagInfo(TaskInfoDTO taskInfoDTO) {
        List<TaskClientTagInfo> taskClientTagInfos = taskInfoDTO.getClientTagIds().stream().map(tagId -> {
            TaskClientTagInfo taskClientTagInfo = new TaskClientTagInfo();

            taskClientTagInfo.setCreateTime(new Date());
            taskClientTagInfo.setUpdateTime(new Date());
            taskClientTagInfo.setCreateBy(AuthUserContext.get().getUsername());
            taskClientTagInfo.setUpdateBy(AuthUserContext.get().getUsername());
            taskClientTagInfo.setDelFlag(DeleteEnum.NORMAL.value());
            taskClientTagInfo.setTaskId(taskInfoDTO.getTaskId());
            taskClientTagInfo.setTagId(tagId);

            return taskClientTagInfo;
        }).collect(Collectors.toList());
        saveBatch(taskClientTagInfos);
    }

    @Override
    public void copyClientTagInfo(Long taskId) {
        List<TaskClientTagInfo> taskClientTagInfos = list(Wrappers.<TaskClientTagInfo>lambdaQuery().eq(TaskClientTagInfo::getTaskId, taskId).eq(TaskClientTagInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        if (CollUtil.isEmpty(taskClientTagInfos)) {
            log.error("copyClientTagInfo时未获取到任务id为：{}的数据", taskId);
            return;
        }
        List<TaskClientTagInfo> taskClientTagInfoList = taskClientTagInfos.stream().map(temp -> {
            TaskClientTagInfo taskClientTagInfo = new TaskClientTagInfo();

            taskClientTagInfo.setCreateTime(new Date());
            taskClientTagInfo.setUpdateTime(new Date());
            taskClientTagInfo.setCreateBy(AuthUserContext.get().getUsername());
            taskClientTagInfo.setUpdateBy(AuthUserContext.get().getUsername());
            taskClientTagInfo.setDelFlag(DeleteEnum.NORMAL.value());
            taskClientTagInfo.setTaskId(taskId);
            taskClientTagInfo.setTagId(temp.getTagId());

            return taskClientTagInfo;
        }).collect(Collectors.toList());
        saveBatch(taskClientTagInfoList);

    }
}

