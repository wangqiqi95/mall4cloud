package com.mall4j.cloud.biz.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.MicroPageBurialPointRecordMapper;
import com.mall4j.cloud.biz.mapper.TaskFrequencyInfoMapper;
import com.mall4j.cloud.biz.model.MicroPageBurialPointRecord;
import com.mall4j.cloud.biz.model.TaskFrequencyInfo;
import com.mall4j.cloud.biz.service.TaskFrequencyInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskFrequencyInfoServiceImpl extends ServiceImpl<TaskFrequencyInfoMapper, TaskFrequencyInfo> implements TaskFrequencyInfoService {

    @Override
    public void saveTaskFrequencyInfo(TaskInfoDTO taskInfoDTO) {
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
}

