package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.TaskRemindTypeEnum;
import com.mall4j.cloud.biz.constant.task.TaskShoppingGuideInfoTypeEnum;
import com.mall4j.cloud.biz.constant.task.TaskShoppingGuideTypeEnum;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.dto.TaskRemindInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskShoppingGuideInfoMapper;
import com.mall4j.cloud.biz.model.TaskFrequencyInfo;
import com.mall4j.cloud.biz.model.TaskShoppingGuideInfo;
import com.mall4j.cloud.biz.model.TaskStoreInfo;
import com.mall4j.cloud.biz.service.TaskShoppingGuideInfoService;
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
public class TaskShoppingGuideInfoServiceImpl extends ServiceImpl<TaskShoppingGuideInfoMapper, TaskShoppingGuideInfo> implements TaskShoppingGuideInfoService {
    @Override
    public void saveShoppingGuideInfo(TaskInfoDTO taskInfoDTO) {
        if (ObjectUtil.isNotEmpty(taskInfoDTO.getId())) {
            deleteByTaskId(taskInfoDTO.getId());
        }
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos = new ArrayList<>();

        // 处理任务导购
        if (ObjectUtil.equals(taskInfoDTO.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.SPECIFY.getValue())) {
            taskShoppingGuideInfos = taskInfoDTO.getShoppingGuideIds().stream().map(taskShoppingGuideId -> {
                TaskShoppingGuideInfo taskShoppingGuideInfo = initTaskShoppingGuideInfo();
                taskShoppingGuideInfo.setTaskId(taskInfoDTO.getTaskId());
                taskShoppingGuideInfo.setShopGuideId(taskShoppingGuideId);
                taskShoppingGuideInfo.setShopGuideType(TaskShoppingGuideInfoTypeEnum.TASK_SHOPPING_GUIDE.getValue());

                return taskShoppingGuideInfo;
            }).collect(Collectors.toList());
        }

        // 处理任务提醒中指定的导购
        if (taskInfoDTO.getTaskRemindInfos().stream()
                .map(TaskRemindInfoDTO::getRemindType).distinct()
                .anyMatch(remindType -> ObjectUtil.equals(remindType, TaskRemindTypeEnum.ALL.getValue())
                        || ObjectUtil.equals(remindType, TaskRemindTypeEnum.SPECIFY_SHOPPING_GUIDE.getValue()))) {
            List<TaskShoppingGuideInfo> temp = taskInfoDTO.getRemindShoppingGuideIds().stream().map(remindShoppingGuideId -> {
                TaskShoppingGuideInfo taskShoppingGuideInfo = initTaskShoppingGuideInfo();
                taskShoppingGuideInfo.setTaskId(taskInfoDTO.getTaskId());
                taskShoppingGuideInfo.setShopGuideId(remindShoppingGuideId);
                taskShoppingGuideInfo.setShopGuideType(TaskShoppingGuideInfoTypeEnum.SPECIFY_SHOPPING_GUIDE.getValue());
                return taskShoppingGuideInfo;
            }).collect(Collectors.toList());
            taskShoppingGuideInfos.addAll(temp);
        }

        saveBatch(taskShoppingGuideInfos);
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        remove(Wrappers.<TaskShoppingGuideInfo>lambdaQuery().eq(TaskShoppingGuideInfo::getTaskId, taskId).eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value()));
    }

    private TaskShoppingGuideInfo initTaskShoppingGuideInfo() {
        TaskShoppingGuideInfo taskShoppingGuideInfo = new TaskShoppingGuideInfo();
        taskShoppingGuideInfo.setCreateTime(new Date());
        taskShoppingGuideInfo.setUpdateTime(new Date());
        taskShoppingGuideInfo.setCreateBy(AuthUserContext.get().getUsername());
        taskShoppingGuideInfo.setUpdateBy(AuthUserContext.get().getUsername());
        taskShoppingGuideInfo.setDelFlag(DeleteEnum.NORMAL.value());
        return taskShoppingGuideInfo;
    }

    @Override
    public void copyShoppingGuideInfo(Long taskId) {
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos = list(Wrappers.<TaskShoppingGuideInfo>lambdaQuery().eq(TaskShoppingGuideInfo::getTaskId, taskId).eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        if (CollUtil.isEmpty(taskShoppingGuideInfos)) {
            log.error("copyShoppingGuideInfo时未获取到任务id为：{}的数据", taskId);
            return;
        }

        List<TaskShoppingGuideInfo> taskShoppingGuideInfoList = taskShoppingGuideInfos.stream().map(temp -> {
            TaskShoppingGuideInfo taskShoppingGuideInfo = initTaskShoppingGuideInfo();
            taskShoppingGuideInfo.setTaskId(taskId);
            taskShoppingGuideInfo.setShopGuideId(temp.getShopGuideId());
            return taskShoppingGuideInfo;
        }).collect(Collectors.toList());
        saveBatch(taskShoppingGuideInfoList);
    }
}

