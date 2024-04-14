package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.constant.task.*;
import com.mall4j.cloud.biz.dto.*;
import com.mall4j.cloud.biz.mapper.TaskInfoMapper;
import com.mall4j.cloud.biz.model.*;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.biz.util.AnnotationUtil;
import com.mall4j.cloud.biz.util.ExpressUtil;
import com.mall4j.cloud.biz.vo.cp.taskInfo.*;
import com.mall4j.cloud.common.constant.DeleteEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.ExcelUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private TaskRemindInfoService taskRemindInfoService;
    @Resource
    private TaskClientTagInfoService taskClientTagInfoService;
    @Resource
    private TaskClientTempInfoService taskClientTempInfoService;
    @Resource
    private TaskMaterialInfoService taskMaterialInfoService;

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
        TaskInfo taskInfoModel;
        if (ObjectUtil.isEmpty(taskInfo.getId())) {
            taskInfoModel = new TaskInfo();
            taskInfoModel.setCreateBy(AuthUserContext.get().getUsername());
            taskInfoModel.setCreateTime(new Date());
            taskInfoModel.setDelFlag(DeleteEnum.NORMAL.value());
        } else {
            taskInfoModel = taskInfoMapper.selectById(taskInfo.getId());
        }

        BeanUtil.copyProperties(taskInfo, taskInfoModel);

        // 设置非业务字段
        taskInfoModel.setUpdateBy(AuthUserContext.get().getUsername());
        taskInfoModel.setUpdateTime(new Date());
        taskInfoModel.setTaskStatus(TaskStatusEnum.NOT_START.getValue());

        // 设置门店数量
        ExpressUtil.isTrue(ObjectUtil.equals(taskInfo.getTaskStoreType(), TaskStoreTypeEnum.SPECIFY.getValue()), () -> {
            taskInfoModel.setStoreNum(taskInfo.getTaskStoreIds().size());
        });
        // 设置导购数量
        ExpressUtil.isTrue(ObjectUtil.equals(taskInfo.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.SPECIFY.getValue()), () -> {
            taskInfoModel.setShoppingGuideNum(taskInfo.getShoppingGuideIds().size());
        });
        saveOrUpdate(taskInfoModel);
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
        taskRemindInfoService.saveTaskRemindInfo(taskInfo);
        // 保存素材
        taskMaterialInfoService.saveTaskMaterialInfo(taskInfo);

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
                && CollUtil.isEmpty(taskInfoDTO.getClientTagIds()), "指定标签时，需传入标签信息");
        // 指定客户群时必须传入客户群信息
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskClientGroupType(), TaskClientGroupTypeEnum.SPECIFY.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getTaskClientGroups()), "指定客户群时必须传入客户群信息！");
        // 指定门店时必须传入门店信息
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskStoreType(), TaskStoreTypeEnum.SPECIFY.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getTaskStoreIds()), "指定门店时必须传入门店信息！");
        // 指定导购时必须传入导购信息
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskShoppingGuideType(), TaskShoppingGuideTypeEnum.SPECIFY.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getShoppingGuideIds()), "指定导购时必须传入导购信息！");

        // 分享素材任务必须上传素材信息
        Assert.isTrue(ObjectUtil.equals(taskInfoDTO.getTaskType(), TaskTypeEnum.SHARE_MATERIAL.getValue())
                && CollUtil.isEmpty(taskInfoDTO.getTaskMaterialInfos()), "分享素材任务必须上传素材信息！");

        Assert.isTrue(taskInfoDTO.getTaskRemindInfos().stream()
                .map(TaskRemindInfoDTO::getRemindType).distinct()
                .anyMatch(remindType -> ObjectUtil.equals(remindType, TaskRemindTypeEnum.ALL.getValue())
                        || ObjectUtil.equals(remindType, TaskRemindTypeEnum.SPECIFY_SHOPPING_GUIDE.getValue()))
                && CollUtil.isEmpty(taskInfoDTO.getTaskRemindInfos()), "任务提醒勾选了指定员工时必须传入指定导购信息");
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

    @Override
    public void endTask(Long id) {
        TaskInfo taskInfo = getById(id);
        Assert.isTrue(ObjectUtil.isEmpty(taskInfo), "未获取到对应的任务");
        Assert.isTrue(ObjectUtil.notEqual(taskInfo.getTaskStatus(), TaskStatusEnum.PROGRESS.getValue()), "当前任务状态不可结束");

        // 设置任务状态为结束
        taskInfo.setTaskStatus(TaskStatusEnum.END.getValue());
        updateById(taskInfo);
    }

    @Override
    public void copyTask(Long id) {
        TaskInfo originTaskInfo = getById(id);
        Assert.isTrue(ObjectUtil.isEmpty(originTaskInfo), "未获取到对应的任务");

        // 复制主表
        TaskInfo taskInfo = new TaskInfo();
        BeanUtil.copyProperties(originTaskInfo, taskInfo);

        // 设置新表属性
        taskInfo.setId(null);
        taskInfo.setCreateTime(new Date());
        taskInfo.setUpdateTime(new Date());
        taskInfo.setCreateBy(AuthUserContext.get().getUsername());
        taskInfo.setUpdateBy(AuthUserContext.get().getUsername());
        taskInfo.setDelFlag(DeleteEnum.NORMAL.value());
        taskInfo.setTaskStatus(TaskStatusEnum.NOT_START.getValue());
        save(taskInfo);

        // 复制频率信息
        taskFrequencyInfoService.copyTaskFrequencyInfo(taskInfo.getId());
        // 复制标签信息
        taskClientTagInfoService.copyClientTagInfo(taskInfo.getId());
        // 复制客户信息
        taskClientInfoService.copyTaskClientInfo(taskInfo.getId());
        // 复制客户群信息
        taskClientGroupInfoService.copyTaskClientGroupInfo(taskInfo.getId());
        // 复制门店信息
        taskStoreInfoService.copyTaskStoreInfo(taskInfo.getId());
        // 复制导购信息
        taskShoppingGuideInfoService.copyShoppingGuideInfo(taskInfo.getId());
        // 复制任务提醒信息
        taskRemindInfoService.copyTaskRemindInfo(taskInfo.getId());
        // 复制素材
        taskMaterialInfoService.copyTaskMaterialInfo(taskInfo.getId());
    }

    @Override
    public TaskInfoVO getTaskInfo(Long id) {
        TaskInfo taskInfo = getById(id);
        Assert.isTrue(ObjectUtil.isEmpty(taskInfo), "未获取到对应的任务");

        TaskInfoVO taskInfoVO = new TaskInfoVO();
        BeanUtil.copyProperties(taskInfo, taskInfoVO);

        // 处理任务时间
        TaskFrequencyInfo taskFrequencyInfo = Optional.ofNullable(taskFrequencyInfoService.getOne(Wrappers.<TaskFrequencyInfo>lambdaQuery()
                .eq(TaskFrequencyInfo::getTaskId, id)
                .eq(TaskFrequencyInfo::getDelFlag, DeleteEnum.NORMAL.value()))).orElse(new TaskFrequencyInfo());
        taskInfoVO.setTaskStartTime(taskFrequencyInfo.getStartTime());
        taskInfoVO.setTaskEndTime(taskFrequencyInfo.getEndTime());

        // 设置任务提醒配置
        List<TaskRemindInfoVO> taskRemindInfoVOList = taskRemindInfoService.list(Wrappers.<TaskRemindInfo>lambdaQuery()
                .eq(TaskRemindInfo::getTaskId, id)
                .eq(TaskRemindInfo::getDelFlag, DeleteEnum.NORMAL.value())).stream().map(item -> {
            TaskRemindInfoVO taskRemindInfoVO = new TaskRemindInfoVO();
            BeanUtil.copyProperties(item, taskRemindInfoVO);
            return taskRemindInfoVO;
        }).collect(Collectors.toList());
        taskInfoVO.setTaskRemindInfos(taskRemindInfoVOList);

        // 设置任务客户标签
        List<String> clientTagIds = taskClientTagInfoService.list(Wrappers.<TaskClientTagInfo>lambdaQuery()
                .eq(TaskClientTagInfo::getTaskId, id)
                .eq(TaskClientTagInfo::getDelFlag, DeleteEnum.NORMAL.value())).stream().map(TaskClientTagInfo::getTagId).collect(Collectors.toList());
        taskInfoVO.setClientTagIds(clientTagIds);


        // 设置任务客户
        List<TaskClientInfoVO> taskClientInfoVOList = taskClientInfoService.list(Wrappers.<TaskClientInfo>lambdaQuery()
                .eq(TaskClientInfo::getTaskId, id)
                .eq(TaskClientInfo::getDelFlag, DeleteEnum.NORMAL.value())).stream().map(clientInfo -> {
            TaskClientInfoVO taskClientInfoVO = new TaskClientInfoVO();
            BeanUtil.copyProperties(clientInfo, taskClientInfoVO);
            return taskClientInfoVO;
        }).collect(Collectors.toList());
        taskInfoVO.setTaskClientInfos(taskClientInfoVOList);


        // 设置任务客户群
        List<String> taskClientGroupIds = taskClientGroupInfoService.list(Wrappers.<TaskClientGroupInfo>lambdaQuery()
                .eq(TaskClientGroupInfo::getTaskId, id)
                .eq(TaskClientGroupInfo::getDelFlag, DeleteEnum.NORMAL.value())).stream().map(TaskClientGroupInfo::getClientGroupId).collect(Collectors.toList());
        taskInfoVO.setTaskClientGroupIds(taskClientGroupIds);


        // 设置任务门店
        List<String> taskStoreIds = taskStoreInfoService.list(Wrappers.<TaskStoreInfo>lambdaQuery()
                .eq(TaskStoreInfo::getTaskId, id)
                .eq(TaskStoreInfo::getDelFlag, DeleteEnum.NORMAL.value())).stream().map(TaskStoreInfo::getStoreId).collect(Collectors.toList());
        taskInfoVO.setTaskStoreIds(taskStoreIds);


        List<TaskShoppingGuideInfo> taskShoppingGuideInfos = taskShoppingGuideInfoService.list(Wrappers.<TaskShoppingGuideInfo>lambdaQuery()
                .eq(TaskShoppingGuideInfo::getTaskId, id)
                .eq(TaskShoppingGuideInfo::getDelFlag, DeleteEnum.NORMAL.value())).stream().collect(Collectors.toList());

        // 设置任务导购
        taskInfoVO.setShoppingGuideIds(taskShoppingGuideInfos.stream()
                .filter(item -> ObjectUtil.equals(item.getShopGuideType(), TaskShoppingGuideInfoTypeEnum.TASK_SHOPPING_GUIDE.getValue()))
                .map(TaskShoppingGuideInfo::getShopGuideId).collect(Collectors.toList()));

        // 设置提醒导购
        taskInfoVO.setRemindShoppingGuideIds(taskShoppingGuideInfos.stream()
                .filter(item -> ObjectUtil.equals(item.getShopGuideType(), TaskShoppingGuideInfoTypeEnum.SPECIFY_SHOPPING_GUIDE.getValue()))
                .map(TaskShoppingGuideInfo::getShopGuideId).collect(Collectors.toList()));
        // 设置提醒导购
        taskInfoVO.setTaskMaterialInfo(taskMaterialInfoService.list(Wrappers.<TaskMaterialInfo>lambdaQuery()
                        .eq(TaskMaterialInfo::getTaskId, id)
                        .eq(TaskMaterialInfo::getDelFlag, DeleteEnum.NORMAL.value())
                ).stream()
                .map(i -> {
                    TaskMaterialInfoVO taskMaterialInfoVO = new TaskMaterialInfoVO();
                    BeanUtil.copyProperties(i, taskMaterialInfoVO);
                    return taskMaterialInfoVO;
                }).collect(Collectors.toList()));


        return taskInfoVO;
    }

    @Override
    public void importClients(MultipartFile file, String uuid, HttpServletResponse response) {
        // 文件名校验
        com.mall4j.cloud.biz.util.ExcelUtil.checkFileName(file);
        try {


            EasyExcel.read(file.getInputStream(), TaskClientImportExcelDTO.class, new ReadListener<TaskClientImportExcelDTO>() {
                        // 校验是否处理过数据
                        private boolean handledData = false;

                        /**
                         * 单次缓存的数据量
                         */
                        public static final int BATCH_COUNT = 10;
                        /**
                         *临时存储
                         */

                        private List<TaskClientInfoDTO> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                        private List<TaskClientImportErrorExcelDTO> errorExcels = new ArrayList<>();

                        /**
                         * 校验数据。注意这是每一条数据
                         * @param data
                         * @param context
                         */
                        @Override
                        public void invoke(TaskClientImportExcelDTO data, AnalysisContext context) {
                            checkImportData(data, cachedDataList, errorExcels);
                            handledData = true;
                            // 每隔指定条数保存
                            if (cachedDataList.size() >= BATCH_COUNT) {
                                taskClientTempInfoService.batchInsert(cachedDataList, uuid);
                                // 存储完成清理 list
                                cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                            }
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {
                            Assert.isTrue(!handledData || CollUtil.isEmpty(cachedDataList), "导入的excel中不存在数据，请添加后重试！");
                            org.springframework.util.Assert.isTrue(CollUtil.isNotEmpty(cachedDataList) || handledData, "");
                            if (CollUtil.isNotEmpty(cachedDataList)) {
                                taskClientTempInfoService.batchInsert(cachedDataList, uuid);
                            }
                            // 不为空说明存在错误数据。导出错误数据
                            if (CollUtil.isNotEmpty(errorExcels)) {
                                ExcelWriter excelWriter = null;
                                try {
                                    // 先执行合并策略
                                    ExcelWriterBuilder excelWriterMerge = com.mall4j.cloud.common.util.ExcelUtil.getExcelWriterMerge(response, "导入客户名单错误数据", 1, new int[]{});
                                    // useDefaultStyle false 不需要默认的头部加粗/自动换行样式
                                    excelWriter = ExcelUtil.getExcelModel(excelWriterMerge, new HashMap<>(), 1).useDefaultStyle(false).build();
                                    // 业务代码
                                    if (CollUtil.isNotEmpty(errorExcels)) {
                                        // 进行写入操作
                                        WriteSheet sheetWriter = EasyExcel.writerSheet("导入客户名单错误数据").head(TaskClientImportErrorExcelDTO.class).build();
                                        excelWriter.write(errorExcels, sheetWriter);
                                    }
                                } catch (Exception e) {
                                } finally {
                                    // 千万别忘记finish 会帮忙关闭流
                                    if (excelWriter != null) {
                                        excelWriter.finish();
                                    }
                                }
                            }
                        }

                        /**
                         * 校验表头
                         * @param headMap
                         * @param context
                         */
                        @Autowired
                        public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
                            // 获取表头数据
                            Map<Integer, String> headInfos = ConverterUtils.convertToStringMap(headMap, context);
                            // 导入类的表头
                            Map<Integer, String> fieldAnnotationValue = AnnotationUtil.getFieldAnnotationValue(TaskClientImportExcelDTO.class);
                            org.springframework.util.Assert.isTrue(headInfos.equals(fieldAnnotationValue), "导入的excel表头与模板不一致，请重新下载模板填写后重试！");
                        }
                    }).

                    sheet().

                    doRead();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public void downloadClientImportTemplate(HttpServletResponse response) {
        List<TaskClientImportExcelDTO> list = new ArrayList<>();
        list.add(new TaskClientImportExcelDTO());

        ExcelWriter excelWriter = null;
        try {
            // 先执行合并策略
            ExcelWriterBuilder excelWriterMerge = com.mall4j.cloud.common.util.ExcelUtil.getExcelWriterMerge(response, "导入客户名单模板", 1, new int[]{});
            // useDefaultStyle false 不需要默认的头部加粗/自动换行样式
            excelWriter = ExcelUtil.getExcelModel(excelWriterMerge, new HashMap<>(), 1).useDefaultStyle(false).build();
            // 业务代码
            if (CollUtil.isNotEmpty(list)) {
                // 进行写入操作
                WriteSheet sheetWriter = EasyExcel.writerSheet("导入客户名单模板").head(TaskClientImportExcelDTO.class).build();
                excelWriter.write(list, sheetWriter);
            }
        } catch (Exception e) {
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private void checkImportData(TaskClientImportExcelDTO
                                         data, List<TaskClientInfoDTO> successData, List<TaskClientImportErrorExcelDTO> errorData) {
        org.springframework.util.Assert.isTrue(ObjectUtil.isNotEmpty(data), "excel中不存在数据，请填写后再次导入");

        // 错误提示
        StringBuffer errorMsg = new StringBuffer();
        if (StrUtil.isBlank(data.getPhone())) {
            errorMsg.append("手机号不能为空！");
        }
        if (!Validator.isMobile(data.getPhone())) {
            errorMsg.append("手机号格式错误！");
        }


        // 如果报错信息不为空时
        if (StrUtil.isNotBlank(errorMsg.toString())) {
            TaskClientImportErrorExcelDTO taskClientImportErrorExcelDTO = new TaskClientImportErrorExcelDTO();
            BeanUtils.copyProperties(data, taskClientImportErrorExcelDTO);
            taskClientImportErrorExcelDTO.setErrorMsg(errorMsg.toString());
            errorData.add(taskClientImportErrorExcelDTO);
        } else {
            TaskClientInfoDTO taskClientInfoDTO = new TaskClientInfoDTO();
            taskClientInfoDTO.setClientNickname(data.getName());
            taskClientInfoDTO.setClientPhone(data.getPhone());
            successData.add(taskClientInfoDTO);
        }
    }

    @Override
    public ShoppingGuideTaskDetailVO buildShoppingGuideTaskDetailVO(Long taskId) {
        TaskInfo taskInfo = Optional.ofNullable(getById(taskId)).orElse(new TaskInfo());
        TaskFrequencyInfo taskFrequencyInfo = Optional.ofNullable(taskFrequencyInfoService.getOne(Wrappers.<TaskFrequencyInfo>lambdaQuery().eq(TaskFrequencyInfo::getTaskId, taskId).eq(TaskFrequencyInfo::getDelFlag, DeleteEnum.NORMAL.value()))).orElse(new TaskFrequencyInfo());

        return ShoppingGuideTaskDetailVO.builder()
                .taskId(taskId)
                .taskName(taskInfo.getTaskName())
                .taskType(taskInfo.getTaskType())
                .taskStartTime(taskFrequencyInfo.getStartTime())
                .taskEndTime(taskFrequencyInfo.getEndTime())
                .taskTarget(taskInfo.getTaskTarget())
                .taskTargetScale(taskInfo.getTaskTargetScale())
                .speechSkills(taskInfo.getSpeechSkills())
                .taskMaterialInfo(taskMaterialInfoService.list(Wrappers.<TaskMaterialInfo>lambdaQuery()
                                .eq(TaskMaterialInfo::getTaskId, taskId)
                                .eq(TaskMaterialInfo::getDelFlag, DeleteEnum.NORMAL.value())
                        ).stream()
                        .map(i -> {
                            TaskMaterialInfoVO taskMaterialInfoVO = new TaskMaterialInfoVO();
                            BeanUtil.copyProperties(i, taskMaterialInfoVO);
                            return taskMaterialInfoVO;
                        }).collect(Collectors.toList()))
                .build();

    }
}

