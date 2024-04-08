package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.TaskShoppingGuideTypeEnum;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskShoppingGuideInfoMapper;
import com.mall4j.cloud.biz.model.TaskShoppingGuideInfo;
import com.mall4j.cloud.biz.service.TaskShoppingGuideInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskShoppingGuideInfoServiceImpl extends ServiceImpl<TaskShoppingGuideInfoMapper, TaskShoppingGuideInfo> implements TaskShoppingGuideInfoService {
    @Override
    public void saveShoppingGuideInfo(TaskInfoDTO taskInfoDTO) {
        List<TaskShoppingGuideInfo> taskShoppingGuideInfos = new ArrayList<>();
        if (ObjectUtil.equals(taskInfoDTO.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.SPECIFY.getValue())) {
            taskShoppingGuideInfos = taskInfoDTO.getShoppingGuideIds().stream().map(taskShoppingGuideId -> {
                TaskShoppingGuideInfo taskShoppingGuideInfo = new TaskShoppingGuideInfo();
                taskShoppingGuideInfo.setCreateTime(new Date());
                taskShoppingGuideInfo.setUpdateTime(new Date());
                taskShoppingGuideInfo.setCreateBy(AuthUserContext.get().getUsername());
                taskShoppingGuideInfo.setUpdateBy(AuthUserContext.get().getUsername());
                taskShoppingGuideInfo.setDelFlag(DeleteEnum.NORMAL.value());
                taskShoppingGuideInfo.setTaskId(taskInfoDTO.getTaskId());
                taskShoppingGuideInfo.setShopGuideId(taskShoppingGuideId);

                return taskShoppingGuideInfo;
            }).collect(Collectors.toList());
        }
        saveBatch(taskShoppingGuideInfos);
    }
}

