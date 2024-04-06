package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.*;
import com.mall4j.cloud.biz.dto.TaskInfoDTO;
import com.mall4j.cloud.biz.mapper.TaskInfoMapper;
import com.mall4j.cloud.biz.model.TaskInfo;
import com.mall4j.cloud.biz.service.TaskClientGroupInfoService;
import com.mall4j.cloud.biz.service.TaskInfoService;
import com.mall4j.cloud.common.constant.DeleteEnum;
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
        save(taskInfoModel);

        // 保存客户信息


        // 保存客户群信息
        // todo 全部客户群时需接口

        // 保存门店信息
        // todo 全部门店时需接口

        // 保存导购信息
        // todo 全部导购时需接口

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
        // 客户类型为不为全部时，前端传来所有客户信息
        Assert.isTrue(ObjectUtil.notEqual(taskInfoDTO.getTaskClientType(), TaskClientTypeEnum.ALL.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getTaskClientInfos()), "除客户为全部客户时，需传入客户信息！");
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
}

