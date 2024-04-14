package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.MicroPageBurialPointRecordMapper;
import com.mall4j.cloud.biz.mapper.TaskFrequencyInfoMapper;
import com.mall4j.cloud.biz.model.MicroPageBurialPointRecord;
import com.mall4j.cloud.biz.model.TaskClientInfo;
import com.mall4j.cloud.biz.model.TaskFrequencyInfo;
import com.mall4j.cloud.biz.service.TaskFrequencyInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class TaskFrequencyInfoServiceImpl extends ServiceImpl<TaskFrequencyInfoMapper, TaskFrequencyInfo> implements TaskFrequencyInfoService {

    @Override
    public void saveTaskFrequencyInfo(TaskInfoDTO taskInfoDTO) {
        if (ObjectUtil.isNotEmpty(taskInfoDTO.getId())) {
            deleteByTaskId(taskInfoDTO.getId());
        }
        // 保存任务频率
        TaskFrequencyInfo taskFrequencyInfo = new TaskFrequencyInfo();
        taskFrequencyInfo.setCreateTime(new Date());
        taskFrequencyInfo.setUpdateTime(new Date());
        taskFrequencyInfo.setCreateBy(AuthUserContext.get().getUsername());
        taskFrequencyInfo.setUpdateBy(AuthUserContext.get().getUsername());
        taskFrequencyInfo.setDelFlag(DeleteEnum.NORMAL.value());
        taskFrequencyInfo.setTaskId(taskInfoDTO.getTaskId());
        taskFrequencyInfo.setStartTime(taskInfoDTO.getTaskStartTime());
        taskFrequencyInfo.setEndTime(taskInfoDTO.getTaskEndTime());
        save(taskFrequencyInfo);
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        remove(Wrappers.<TaskFrequencyInfo>lambdaQuery().eq(TaskFrequencyInfo::getTaskId, taskId).eq(TaskFrequencyInfo::getDelFlag, DeleteEnum.NORMAL.value()));
    }

    @Override
    public void copyTaskFrequencyInfo(Long taskId) {
        TaskFrequencyInfo taskFrequencyInfo = getOne(Wrappers.<TaskFrequencyInfo>lambdaQuery().eq(TaskFrequencyInfo::getTaskId, taskId).eq(TaskFrequencyInfo::getDelFlag, DeleteEnum.NORMAL.value()));
        if (ObjectUtil.isEmpty(taskFrequencyInfo)) {
            log.error("copyTaskFrequencyInfo时未获取到任务id为：{}的数据", taskId);
            return;
        }
        TaskFrequencyInfo copyTaskFrequencyInfo = new TaskFrequencyInfo();
        copyTaskFrequencyInfo.setCreateTime(new Date());
        copyTaskFrequencyInfo.setUpdateTime(new Date());
        copyTaskFrequencyInfo.setCreateBy(AuthUserContext.get().getUsername());
        copyTaskFrequencyInfo.setUpdateBy(AuthUserContext.get().getUsername());
        copyTaskFrequencyInfo.setDelFlag(DeleteEnum.NORMAL.value());
        copyTaskFrequencyInfo.setTaskId(taskId);
        copyTaskFrequencyInfo.setStartTime(taskFrequencyInfo.getStartTime());
        copyTaskFrequencyInfo.setEndTime(taskFrequencyInfo.getEndTime());
        save(taskFrequencyInfo);
    }
}

