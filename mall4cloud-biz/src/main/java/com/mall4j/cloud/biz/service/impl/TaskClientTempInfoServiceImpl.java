package com.mall4j.cloud.biz.service.impl;

import java.util.Collections;
import java.util.Date;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.TaskClientInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskClientTempInfoMapper;
import com.mall4j.cloud.biz.model.TaskClientTempInfo;
import com.mall4j.cloud.biz.service.TaskClientTempInfoService;
import com.mall4j.cloud.biz.vo.TaskClientTempInfoVO;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskClientTempInfoServiceImpl extends ServiceImpl<TaskClientTempInfoMapper, TaskClientTempInfo> implements TaskClientTempInfoService {

    @Override
    public void batchInsert(List<TaskClientInfoDTO> taskClientInfoList, String uuid) {
        List<TaskClientTempInfo> clientTempInfos = taskClientInfoList.stream().map(taskClientInfoDTO -> {
            TaskClientTempInfo taskClientTempInfo = new TaskClientTempInfo();
            taskClientTempInfo.setCreateTime(new Date());
            taskClientTempInfo.setUpdateTime(new Date());
            taskClientTempInfo.setCreateBy(AuthUserContext.get().getUsername());
            taskClientTempInfo.setUpdateBy(AuthUserContext.get().getUsername());
            taskClientTempInfo.setDelFlag(DeleteEnum.NORMAL.value());
            taskClientTempInfo.setTempUuid(uuid);
            taskClientTempInfo.setClientNickname(taskClientInfoDTO.getClientNickname());
            taskClientTempInfo.setClientPhone(taskClientInfoDTO.getClientPhone());

            return taskClientTempInfo;
        }).collect(Collectors.toList());
        saveBatch(clientTempInfos);
    }

    @Override
    public List<TaskClientTempInfoVO> listTaskClientTempInfoByUuid(String uuid) {
        return list(Wrappers.<TaskClientTempInfo>lambdaQuery().eq(TaskClientTempInfo::getTempUuid, uuid)).stream().map(item -> {
            TaskClientTempInfoVO taskClientTempInfoVO = new TaskClientTempInfoVO();
            BeanUtil.copyProperties(item, taskClientTempInfoVO);
            return taskClientTempInfoVO;
        }).collect(Collectors.toList());
    }
}

