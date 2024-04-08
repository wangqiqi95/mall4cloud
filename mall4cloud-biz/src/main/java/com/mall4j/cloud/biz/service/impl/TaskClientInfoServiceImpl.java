package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.TaskClientTypeEnum;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.MicroPageBurialPointRecordMapper;
import com.mall4j.cloud.biz.mapper.TaskClientInfoMapper;
import com.mall4j.cloud.biz.model.MicroPageBurialPointRecord;
import com.mall4j.cloud.biz.model.TaskClientInfo;
import com.mall4j.cloud.biz.service.TaskClientInfoService;
import com.mall4j.cloud.biz.service.TaskClientTagInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskClientInfoServiceImpl extends ServiceImpl<TaskClientInfoMapper, TaskClientInfo> implements TaskClientInfoService {
    @Resource
    private TaskClientTagInfoService taskClientTagInfoService;

    @Override
    public void saveTaskClientInfo(TaskInfoDTO taskInfoDTO) {
        // 判断类型 全部客户和指定标签时可不存入，生成任务时动态查询
        if (ObjectUtil.equals(taskInfoDTO.getTaskClientType(), TaskClientTypeEnum.IMPORT_CUSTOMER.getValue())) {
            List<TaskClientInfo> taskClientInfos = taskInfoDTO.getTaskClientInfos().stream().map(clientDto -> {
                TaskClientInfo taskClientInfo = new TaskClientInfo();
                taskClientInfo.setCreateTime(new Date());
                taskClientInfo.setUpdateTime(new Date());
                taskClientInfo.setCreateBy(AuthUserContext.get().getUsername());
                taskClientInfo.setUpdateBy(AuthUserContext.get().getUsername());
                taskClientInfo.setDelFlag(DeleteEnum.NORMAL.value());
                taskClientInfo.setTaskId(taskInfoDTO.getTaskId());
                taskClientInfo.setClientId(clientDto.getClientId());
                taskClientInfo.setClientNickname(clientDto.getClientNickname());
                taskClientInfo.setClientPhone(clientDto.getClientPhone());
                taskClientInfo.setClientRemark(clientDto.getClientRemark());
                return taskClientInfo;
            }).collect(Collectors.toList());
            saveBatch(taskClientInfos);
        }

        // 指定标签时存入标签信息
        if (ObjectUtil.equals(taskInfoDTO.getTaskClientType(), TaskClientTypeEnum.SPECIFY_LABEL.getValue())) {
            taskClientTagInfoService.saveClientTagInfo(taskInfoDTO);
        }
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        remove(Wrappers.<TaskClientInfo>lambdaQuery().eq(TaskClientInfo::getTaskId, taskId).eq(TaskClientInfo::getDelFlag, DeleteEnum.NORMAL.value()));
    }

}

