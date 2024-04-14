package com.mall4j.cloud.biz.service.impl;

import java.util.Date;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.TaskVisitResultInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskVisitResultInfoMapper;
import com.mall4j.cloud.biz.model.TaskVisitResultInfo;
import com.mall4j.cloud.biz.service.TaskVisitResultInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

@Service
public class TaskVisitResultInfoServiceImpl extends ServiceImpl<TaskVisitResultInfoMapper, TaskVisitResultInfo> implements TaskVisitResultInfoService {
    @Override
    public void saveTaskVisitResultInfo(TaskVisitResultInfoDTO taskVisitResultInfoDTO) {
        TaskVisitResultInfo taskVisitResultInfo = new TaskVisitResultInfo();
        taskVisitResultInfo.setCreateTime(new Date());
        taskVisitResultInfo.setUpdateTime(new Date());
        taskVisitResultInfo.setCreateBy(AuthUserContext.get().getUsername());
        taskVisitResultInfo.setUpdateBy(AuthUserContext.get().getUsername());
        taskVisitResultInfo.setDelFlag(DeleteEnum.NORMAL.value());
        taskVisitResultInfo.setExecuteDetailId(taskVisitResultInfoDTO.getExecuteDetailId());
        taskVisitResultInfo.setResultInfo(taskVisitResultInfoDTO.getResultInfo());
        taskVisitResultInfo.setFileInfo(taskVisitResultInfoDTO.getFileInfo());
        save(taskVisitResultInfo);
    }

    @Override
    public TaskVisitResultInfo getTaskVisitResultInfo(Long executeDetailId) {
        return getOne(Wrappers.<TaskVisitResultInfo>lambdaQuery().eq(TaskVisitResultInfo::getExecuteDetailId, executeDetailId));
    }
}

