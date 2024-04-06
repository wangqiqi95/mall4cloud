package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.MicroPageBurialPointRecordMapper;
import com.mall4j.cloud.biz.mapper.TaskClientGroupInfoMapper;
import com.mall4j.cloud.biz.model.MicroPageBurialPointRecord;
import com.mall4j.cloud.biz.model.TaskClientGroupInfo;
import com.mall4j.cloud.biz.service.TaskClientGroupInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskClientGroupInfoServiceImpl extends ServiceImpl<TaskClientGroupInfoMapper, TaskClientGroupInfo> implements TaskClientGroupInfoService {

    @Override
    public void initSaveModel(TaskClientGroupInfo taskClientGroupInfo) {
        taskClientGroupInfo.setDelFlag(DeleteEnum.NORMAL.value());
        taskClientGroupInfo.setCreateBy(AuthUserContext.get().getUsername());
        taskClientGroupInfo.setCreateTime(new Date());
        taskClientGroupInfo.setUpdateBy(AuthUserContext.get().getUsername());
        taskClientGroupInfo.setUpdateTime(new Date());
    }

    @Override
    public List<TaskClientGroupInfo> initTaskClientGroupInfo(TaskInfoDTO taskInfoDTO) {
        if (CollUtil.isEmpty(taskInfoDTO.getTaskClientGroupIds())) {
            return Collections.emptyList();
        }
        return taskInfoDTO.getTaskClientGroupIds().stream().map(taskClientGroupId -> {
            TaskClientGroupInfo taskClientGroupInfo = new TaskClientGroupInfo();
            initSaveModel(taskClientGroupInfo);

            taskClientGroupInfo.setTaskId(taskInfoDTO.getTaskId());
            taskClientGroupInfo.setClientGroupId(taskClientGroupId);
            return taskClientGroupInfo;
        }).collect(Collectors.toList());
    }
}

