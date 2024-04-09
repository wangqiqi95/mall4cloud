package com.mall4j.cloud.biz.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.dto.TaskRemindInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskRemindInfoMapper;
import com.mall4j.cloud.biz.model.TaskRemindInfo;
import com.mall4j.cloud.biz.model.TaskShoppingGuideInfo;
import com.mall4j.cloud.biz.model.TaskStoreInfo;
import com.mall4j.cloud.biz.service.TaskRemindInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskRemindInfoServiceImpl extends ServiceImpl<TaskRemindInfoMapper, TaskRemindInfo> implements TaskRemindInfoService {

    @Override
    public void saveTaskRemindInfo(TaskInfoDTO taskInfoDTO) {
        if (ObjectUtil.isNotEmpty(taskInfoDTO.getId())) {
            deleteByTaskId(taskInfoDTO.getId());
        }

        List<TaskRemindInfo> taskRemindInfos = taskInfoDTO.getTaskRemindInfos().stream().map(taskRemindInfoDTO -> {
            TaskRemindInfo taskRemindInfo = new TaskRemindInfo();
            taskRemindInfo.setCreateTime(new Date());
            taskRemindInfo.setUpdateTime(new Date());
            taskRemindInfo.setCreateBy(AuthUserContext.get().getUsername());
            taskRemindInfo.setUpdateBy(AuthUserContext.get().getUsername());
            taskRemindInfo.setDelFlag(DeleteEnum.NORMAL.value());
            taskRemindInfo.setTaskId(taskInfoDTO.getTaskId());
            taskRemindInfo.setRemindType(taskRemindInfoDTO.getRemindType());
            taskRemindInfo.setRemindScenes(taskRemindInfoDTO.getRemindScenes());
            taskRemindInfo.setStartAfterHours(taskRemindInfoDTO.getStartAfterHours());
            taskRemindInfo.setLastHours(taskRemindInfoDTO.getLastHours());
            taskRemindInfo.setStandardScale(taskInfoDTO.getTaskTargetScale());
            return taskRemindInfo;
        }).collect(Collectors.toList());
        saveBatch(taskRemindInfos);
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        remove(Wrappers.<TaskRemindInfo>lambdaQuery().eq(TaskRemindInfo::getTaskId, taskId).eq(TaskRemindInfo::getDelFlag, DeleteEnum.NORMAL.value()));
    }

    @Override
    public void copyTaskRemindInfo(Long taskId) {
        List<TaskRemindInfo> taskRemindInfos = list(Wrappers.<TaskRemindInfo>lambdaQuery().eq(TaskRemindInfo::getTaskId, taskId).eq(TaskRemindInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        if (CollUtil.isEmpty(taskRemindInfos)) {
            log.error("copyTaskRemindInfo时未获取到任务id为：{}的数据", taskId);
            return;
        }
        List<TaskRemindInfo> taskRemindInfoList = taskRemindInfos.stream().map(temp -> {
            TaskRemindInfo taskRemindInfo = new TaskRemindInfo();
            BeanUtil.copyProperties(temp, taskRemindInfo);
            taskRemindInfo.setCreateTime(new Date());
            taskRemindInfo.setUpdateTime(new Date());
            taskRemindInfo.setCreateBy(AuthUserContext.get().getUsername());
            taskRemindInfo.setUpdateBy(AuthUserContext.get().getUsername());
            taskRemindInfo.setTaskId(taskId);

            return taskRemindInfo;
        }).collect(Collectors.toList());
        saveBatch(taskRemindInfoList);
    }
}

