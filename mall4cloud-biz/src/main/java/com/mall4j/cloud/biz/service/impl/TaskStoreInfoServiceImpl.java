package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.TaskStoreTypeEnum;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.MicroPageBurialPointRecordMapper;
import com.mall4j.cloud.biz.mapper.TaskStoreInfoMapper;
import com.mall4j.cloud.biz.model.MicroPageBurialPointRecord;
import com.mall4j.cloud.biz.model.TaskFrequencyInfo;
import com.mall4j.cloud.biz.model.TaskStoreInfo;
import com.mall4j.cloud.biz.service.TaskStoreInfoService;
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
public class TaskStoreInfoServiceImpl extends ServiceImpl<TaskStoreInfoMapper, TaskStoreInfo> implements TaskStoreInfoService {
    @Override
    public void saveTaskStoreInfo(TaskInfoDTO taskInfoDTO) {
        if (ObjectUtil.isNotEmpty(taskInfoDTO.getId())) {
            deleteByTaskId(taskInfoDTO.getId());
        }
        List<TaskStoreInfo> taskStoreInfos = new ArrayList<>();
        if (ObjectUtil.equals(taskInfoDTO.getTaskStoreType(), TaskStoreTypeEnum.SPECIFY.getValue())) {
            taskStoreInfos = taskInfoDTO.getTaskStoreIds().stream().map(taskStoreId -> {
                TaskStoreInfo taskStoreInfo = new TaskStoreInfo();

                taskStoreInfo.setCreateTime(new Date());
                taskStoreInfo.setUpdateTime(new Date());
                taskStoreInfo.setCreateBy(AuthUserContext.get().getUsername());
                taskStoreInfo.setUpdateBy(AuthUserContext.get().getUsername());
                taskStoreInfo.setDelFlag(DeleteEnum.NORMAL.value());

                taskStoreInfo.setTaskId(taskInfoDTO.getTaskId());
                taskStoreInfo.setStoreId(taskStoreId);
                return taskStoreInfo;
            }).collect(Collectors.toList());
        }
        saveBatch(taskStoreInfos);
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        remove(Wrappers.<TaskStoreInfo>lambdaQuery().eq(TaskStoreInfo::getTaskId, taskId).eq(TaskStoreInfo::getDelFlag, DeleteEnum.NORMAL.value()));
    }

    @Override
    public void copyTaskStoreInfo(Long taskId) {
        List<TaskStoreInfo> taskStoreInfos = list(Wrappers.<TaskStoreInfo>lambdaQuery().eq(TaskStoreInfo::getTaskId, taskId).eq(TaskStoreInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        if (CollUtil.isEmpty(taskStoreInfos)) {
            log.error("copyTaskStoreInfo时未获取到任务id为：{}的数据", taskId);
            return;
        }
        List<TaskStoreInfo> taskStoreInfoList = taskStoreInfos.stream().map(temp -> {
            TaskStoreInfo taskStoreInfo = new TaskStoreInfo();

            taskStoreInfo.setCreateTime(new Date());
            taskStoreInfo.setUpdateTime(new Date());
            taskStoreInfo.setCreateBy(AuthUserContext.get().getUsername());
            taskStoreInfo.setUpdateBy(AuthUserContext.get().getUsername());
            taskStoreInfo.setDelFlag(DeleteEnum.NORMAL.value());

            taskStoreInfo.setTaskId(taskId);
            taskStoreInfo.setStoreId(temp.getStoreId());
            return taskStoreInfo;
        }).collect(Collectors.toList());
        saveBatch(taskStoreInfoList);
    }
}

