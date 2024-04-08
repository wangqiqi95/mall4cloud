package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.*;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.dto.TaskInfoSearchParamDTO;
import com.mall4j.cloud.biz.mapper.TaskInfoMapper;
import com.mall4j.cloud.biz.model.*;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.biz.vo.cp.CustGroupVO;
import com.mall4j.cloud.biz.vo.cp.taskInfo.TaskInfoPageVO;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.security.AuthUserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo> implements TaskInfoService {
    @Resource
    private TaskInfoMapper taskInfoMapper;
    @Resource
    private TaskClientGroupInfoService taskClientGroupInfoService;
    @Resource
    private TaskClientInfoService taskClientInfoService;
    @Resource
    private TaskStoreInfoService taskStoreInfoService;
    @Resource
    private TaskShoppingGuideInfoService taskShoppingGuideInfoService;
    @Resource
    private TaskFrequencyInfoService taskFrequencyInfoService;

    public static final Integer ZERO = 0;
    // 拥有话术的任务类型
    public static final List<Integer> SPEECH_SKILLS_TASK_TYPE_LIST = Arrays.asList(TaskTypeEnum.FRIEND_TO_VIP.getValue()
            , TaskTypeEnum.FRIEND_TO_VIP.getValue(), TaskTypeEnum.SHARE_MATERIAL.getValue(), TaskTypeEnum.VISIT_CUSTOMER.getValue());

    @Override
    @Transactional
    public void saveTaskInfo(TaskInfoDTO taskInfo) {
        // 因字段之间有联动作用，所以此次采用自定义校验
        validate(taskInfo);
        // 保存主表信息
        TaskInfo taskInfoModel = new TaskInfo();
        BeanUtil.copyProperties(taskInfo, taskInfoModel);

        // 设置非业务字段
        taskInfoModel.setCreateBy(AuthUserContext.get().getUsername());
        taskInfoModel.setCreateTime(new Date());
        taskInfoModel.setUpdateBy(AuthUserContext.get().getUsername());
        taskInfoModel.setUpdateTime(new Date());
        taskInfoModel.setDelFlag(DeleteEnum.NORMAL.value());
        taskInfoModel.setTaskStatus(TaskStatusEnum.NOT_START.getValue());
        save(taskInfoModel);
        taskInfo.setTaskId(taskInfoModel.getId());


        // 保存频率信息
        taskFrequencyInfoService.saveTaskFrequencyInfo(taskInfo);
        // 保存客户信息
        taskClientInfoService.saveTaskClientInfo(taskInfo);
        // 保存客户群信息
        taskClientGroupInfoService.saveTaskClientGroupInfo(taskInfo);
        // 保存门店信息
        taskStoreInfoService.saveTaskStoreInfo(taskInfo);
        // 保存导购信息
        taskShoppingGuideInfoService.saveShoppingGuideInfo(taskInfo);
        // 保存任务提醒信息

    }

    private void validate(TaskInfoDTO taskInfoDTO) {
        Assert.isTrue(ObjectUtil.isEmpty(taskInfoDTO), "参数异常！");
        Assert.isTrue(StrUtil.isBlank(taskInfoDTO.getTaskName()), "任务名称不能为空！");
        Assert.isTrue(ObjectUtil.isEmpty(taskInfoDTO.getTaskType()), "任务类型不能为空！");
        Assert.isTrue(ObjectUtil.isEmpty(taskInfoDTO.getTaskClientType()), "任务客户不能为空！");
        Assert.isTrue(ObjectUtil.isEmpty(taskInfoDTO.getImplementationType()), "执行方式不能为空！");
        Assert.isTrue(ObjectUtil.isEmpty(taskInfoDTO.getTaskStoreType()), "任务门店不能为空！");
        Assert.isTrue(ObjectUtil.isEmpty(taskInfoDTO.getTaskShoppingGuideType()), "任务导购不能为空！");
        Assert.isTrue(ObjectUtil.isEmpty(taskInfoDTO.getTaskFrequency()), "任务时间不能为空！");


        // 任务类型为分享素材时，分享方式不能为空
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskType(), TaskTypeEnum.SHARE_MATERIAL.getValue())
                && ObjectUtil.isEmpty(taskInfoDTO.getShareType()), "分享方式不能为空！");
        // 分享方式为群发客户群时，客户群类型不能为空
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getShareType(), TaskShareTypeEnum.WORK_WECHAT_CUSTOMER_BASE.getValue())
                && ObjectUtil.isEmpty(taskInfoDTO.getTaskClientGroupType()), "任务客户群类型不能为空！");
        // 分配数量必须为大于0的正整数
        Assert.isTrue(ObjectUtil.isEmpty(taskInfoDTO.getAllocatedQuantity()) ||
                ObjectUtil.equals(ZERO, taskInfoDTO.getAllocatedQuantity()), "分配数量不能为空且必须为大于0的整数！");
        // 除加企微好友外都需要话术
        Assert.isTrue(CollUtil.contains(SPEECH_SKILLS_TASK_TYPE_LIST, taskInfoDTO.getTaskType())
                && StrUtil.isBlank(taskInfoDTO.getSpeechSkills()), "执行方式不能为空！");
        // 导入客户时，前端传来所有客户信息
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskClientType(), TaskClientTypeEnum.IMPORT_CUSTOMER.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getTaskClientInfos()), "导入客户时，需传入客户信息！");
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskClientType(), TaskClientTypeEnum.SPECIFY_LABEL.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getClientTagIds()), "执行标签时，需传入标签信息");
        // 指定客户群时必须传入客户群信息
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskClientGroupType(), TaskClientGroupTypeEnum.SPECIFY.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getTaskClientGroupIds()), "指定客户群时必须传入客户群信息！");
        // 指定门店时必须传入门店信息
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskStoreType(), TaskStoreTypeEnum.SPECIFY.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getTaskStoreIds()), "指定门店时必须传入门店信息！");
        // 指定导购时必须传入导购信息
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.SPECIFY.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getShoppingGuideIds()), "指定导购时必须传入导购信息！");


    }

    @Override
    public void updateTaskStatus(Long id, Integer status) {
        lambdaUpdate()
                .set(TaskInfo::getUpdateBy, AuthUserContext.get().getUsername())
                .set(TaskInfo::getUpdateTime, new Date())
                .set(TaskInfo::getTaskStatus, status)
                .eq(TaskInfo::getId, id)
                .update();
    }

    @Override
    public PageVO<TaskInfoPageVO> page(PageDTO pageDTO, TaskInfoSearchParamDTO taskInfoSearchParamDTO) {
        return PageUtil.doPage(pageDTO, () -> taskInfoMapper.list(taskInfoSearchParamDTO));
    }
}

