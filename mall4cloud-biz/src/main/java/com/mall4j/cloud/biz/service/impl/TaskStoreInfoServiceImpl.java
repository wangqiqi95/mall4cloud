package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.TaskStoreTypeEnum;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.MicroPageBurialPointRecordMapper;
import com.mall4j.cloud.biz.mapper.TaskStoreInfoMapper;
import com.mall4j.cloud.biz.model.MicroPageBurialPointRecord;
import com.mall4j.cloud.biz.model.TaskStoreInfo;
import com.mall4j.cloud.biz.service.TaskStoreInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskStoreInfoServiceImpl extends ServiceImpl<TaskStoreInfoMapper, TaskStoreInfo> implements TaskStoreInfoService {
    @Override
    public void saveTaskStoreInfo(TaskInfoDTO taskInfoDTO) {
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
}

