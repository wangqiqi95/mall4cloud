package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskMaterialInfoMapper;
import com.mall4j.cloud.biz.model.TaskClientTagInfo;
import com.mall4j.cloud.biz.model.TaskFrequencyInfo;
import com.mall4j.cloud.biz.model.TaskMaterialInfo;
import com.mall4j.cloud.biz.service.TaskMaterialInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskMaterialInfoServiceImpl extends ServiceImpl<TaskMaterialInfoMapper, TaskMaterialInfo> implements TaskMaterialInfoService {

    @Override
    public void saveTaskMaterialInfo(TaskInfoDTO taskInfoDTO) {

    }

    @Override
    public void deleteByTaskId(Long taskId) {
        remove(Wrappers.<TaskMaterialInfo>lambdaQuery().eq(TaskMaterialInfo::getTaskId, taskId).eq(TaskMaterialInfo::getDelFlag, DeleteEnum.NORMAL.value()));

    }

    @Override
    public void copyTaskMaterialInfo(Long taskId) {
        List<TaskMaterialInfo> taskMaterialInfos = list(Wrappers.<TaskMaterialInfo>lambdaQuery().eq(TaskMaterialInfo::getTaskId, taskId).eq(TaskMaterialInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        if (CollUtil.isEmpty(taskMaterialInfos)) {
            log.error("copyTaskMaterialInfo时未获取到任务id为：{}的数据", taskId);
            return;
        }
        List<TaskMaterialInfo> taskMaterialInfoList = taskMaterialInfos.stream().map(temp -> {
            TaskMaterialInfo taskMaterialInfo=new TaskMaterialInfo();
            taskMaterialInfo.setCreateTime(new Date());
            taskMaterialInfo.setUpdateTime(new Date());
            taskMaterialInfo.setCreateBy(AuthUserContext.get().getUsername());
            taskMaterialInfo.setUpdateBy(AuthUserContext.get().getUsername());
            taskMaterialInfo.setDelFlag(DeleteEnum.NORMAL.value());
            taskMaterialInfo.setTaskId(taskId);
            taskMaterialInfo.setMaterialType(temp.getMaterialType());
            taskMaterialInfo.setFileInfo(temp.getFileInfo());
            return taskMaterialInfo;
        }).collect(Collectors.toList());
        saveBatch(taskMaterialInfoList);
    }
}

