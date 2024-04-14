package com.mall4j.cloud.biz.service.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.TaskExecuteStatusEnum;
import com.mall4j.cloud.biz.constant.task.TaskShoppingGuideTypeEnum;
import com.mall4j.cloud.biz.constant.task.TaskStoreTypeEnum;
import com.mall4j.cloud.biz.dto.TaskExecuteInfoSearchParamDTO;
import com.mall4j.cloud.biz.mapper.TaskBatchInfoMapper;
import com.mall4j.cloud.biz.model.TaskBatchInfo;
import com.mall4j.cloud.biz.model.TaskFrequencyInfo;
import com.mall4j.cloud.biz.model.TaskInfo;
import com.mall4j.cloud.biz.service.TaskBatchInfoService;
import com.mall4j.cloud.biz.service.TaskFrequencyInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaskBatchInfoServiceImpl extends ServiceImpl<TaskBatchInfoMapper, TaskBatchInfo> implements TaskBatchInfoService {
    @Resource
    private TaskFrequencyInfoService taskFrequencyInfoService;

    @Override
    public TaskBatchInfo saveTaskBatchInfo(TaskInfo taskInfo) {
        TaskBatchInfo oldTaskBatchInfo = list(Wrappers.<TaskBatchInfo>lambdaQuery().eq(TaskBatchInfo::getTaskId, taskInfo.getId()))
                .stream().sorted(Comparator.comparingInt(TaskBatchInfo::getBatchNum).reversed()).findFirst().orElse(new TaskBatchInfo());
        TaskFrequencyInfo taskFrequencyInfo = Optional.ofNullable(taskFrequencyInfoService.getOne(Wrappers.<TaskFrequencyInfo>lambdaQuery().eq(TaskFrequencyInfo::getTaskId, taskInfo.getId()).eq(TaskFrequencyInfo::getDelFlag, DeleteEnum.NORMAL.value()))).orElse(new TaskFrequencyInfo());

        TaskBatchInfo taskBatchInfo = new TaskBatchInfo();

        taskBatchInfo.setCreateTime(new Date());
        taskBatchInfo.setUpdateTime(new Date());
        taskBatchInfo.setDelFlag(DeleteEnum.NORMAL.value());
        taskBatchInfo.setTaskId(taskInfo.getId());
        taskBatchInfo.setBatchNum(ObjectUtil.isEmpty(oldTaskBatchInfo.getBatchNum()) ? 1 : oldTaskBatchInfo.getBatchNum() + 1);
        // todo 暂定为当前时间
        taskBatchInfo.setStartTime(new Date());
        taskBatchInfo.setEndTime(taskFrequencyInfo.getEndTime());
        taskBatchInfo.setStatus(TaskExecuteStatusEnum.NOT_START.getValue());
        taskBatchInfo.setTaskStoreNum(ObjectUtil.equal(taskInfo.getTaskStoreType(), TaskStoreTypeEnum.ALL.getValue()) ? -1 : taskInfo.getStoreNum());
        taskBatchInfo.setTaskShoppingGuideNum(ObjectUtil.equal(taskInfo.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.ALL.getValue()) ? -1 : taskInfo.getShoppingGuideNum());
        return taskBatchInfo;
    }

    @Override
    public PageVO<TaskBatchInfo> taskBatchPage(PageDTO pageDTO, TaskExecuteInfoSearchParamDTO taskExecuteInfoSearchParamDTO) {
        return PageUtil.doPage(pageDTO, () -> list(Wrappers.<TaskBatchInfo>lambdaQuery().eq(TaskBatchInfo::getTaskId, taskExecuteInfoSearchParamDTO.getTaskId())));
    }
}

