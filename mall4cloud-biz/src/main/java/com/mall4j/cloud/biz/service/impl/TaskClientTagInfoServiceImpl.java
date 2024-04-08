package com.mall4j.cloud.biz.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskClientTagInfoMapper;
import com.mall4j.cloud.biz.model.TaskClientTagInfo;
import com.mall4j.cloud.biz.service.TaskClientTagInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

@Service
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
}

