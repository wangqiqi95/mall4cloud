package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.ExtendWxCpMsgTemplateDTO;
import com.mall4j.cloud.api.biz.dto.cp.media.UploadUrlMediaDTO;
import com.mall4j.cloud.api.biz.feign.CpCustGroupClient;
import com.mall4j.cloud.api.biz.feign.MaterialFeignClient;
import com.mall4j.cloud.api.biz.vo.CustGroupVO;
import com.mall4j.cloud.api.biz.vo.MsgAttachment;
import com.mall4j.cloud.api.biz.vo.WeixinUploadMediaResultVO;
import com.mall4j.cloud.api.platform.dto.FinishDownLoadDTO;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.SysUserVO;
import com.mall4j.cloud.api.user.crm.request.TaskSaveRequest;
import com.mall4j.cloud.api.user.dto.StartGroupPushDTO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.common.cache.constant.UserCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.user.bo.AddGroupPushSonTaskBO;
import com.mall4j.cloud.user.bo.AddGroupPushTaskStaffRelationBO;
import com.mall4j.cloud.user.bo.AddGroupPushTaskVipRelationBO;
import com.mall4j.cloud.user.bo.AddPushSonTaskMediaBO;
import com.mall4j.cloud.user.bo.BatchWrapperTaskStaffAndVipBO;
import com.mall4j.cloud.user.bo.ExportGroupPushTaskStatisticsBO;
import com.mall4j.cloud.user.bo.GroupPushSonTaskBO;
import com.mall4j.cloud.user.bo.GroupPushSonTaskMediaBO;
import com.mall4j.cloud.user.config.UploadUrlMediaProperties;
import com.mall4j.cloud.user.constant.GroupPushTaskOperateStatusEnum;
import com.mall4j.cloud.user.constant.StaffCpFriendStateEnum;
import com.mall4j.cloud.user.dto.AddPushSonTaskDTO;
import com.mall4j.cloud.user.dto.AddPushTaskDTO;
import com.mall4j.cloud.user.dto.EditPushSonTaskDTO;
import com.mall4j.cloud.user.dto.EditPushSonTaskMediaDTO;
import com.mall4j.cloud.user.dto.EditPushTaskDTO;
import com.mall4j.cloud.user.dto.GetGroupPushTaskStatisticDTO;
import com.mall4j.cloud.user.dto.QueryGroupPushRecordDTO;
import com.mall4j.cloud.user.dto.QueryGroupPushSonTaskPageDetailDTO;
import com.mall4j.cloud.user.dto.QueryGroupPushTaskPageDTO;
import com.mall4j.cloud.user.dto.QueryGroupPushTaskPageDetailDTO;
import com.mall4j.cloud.user.dto.openapi.SendTaskDTO;
import com.mall4j.cloud.user.manager.GroupPushSonTaskManager;
import com.mall4j.cloud.user.manager.GroupPushSonTaskMediaManager;
import com.mall4j.cloud.user.manager.GroupPushTaskManager;
import com.mall4j.cloud.user.manager.GroupPushTaskStaffRelationManager;
import com.mall4j.cloud.user.manager.GroupPushTaskVipRelationManager;
import com.mall4j.cloud.user.manager.StaffBatchSendCpMsgManager;
import com.mall4j.cloud.user.manager.TagManager;
import com.mall4j.cloud.user.manager.UserStaffCpRelationManager;
import com.mall4j.cloud.user.manager.UserTagRelationManager;
import com.mall4j.cloud.user.mapper.CrmUserTagRelationMapper;
import com.mall4j.cloud.user.mapper.GroupPushSonTaskMapper;
import com.mall4j.cloud.user.mapper.GroupPushSonTaskMediaMapper;
import com.mall4j.cloud.user.mapper.GroupPushTaskMapper;
import com.mall4j.cloud.user.mapper.GroupPushTaskStaffRelationMapper;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationMapper;
import com.mall4j.cloud.user.model.GroupPushSonTask;
import com.mall4j.cloud.user.model.GroupPushSonTaskMedia;
import com.mall4j.cloud.user.model.GroupPushTag;
import com.mall4j.cloud.user.model.GroupPushTask;
import com.mall4j.cloud.user.model.GroupPushTaskStaffRelation;
import com.mall4j.cloud.user.model.GroupSonTaskSendRecord;
import com.mall4j.cloud.user.model.StaffBatchSendCpMsg;
import com.mall4j.cloud.user.model.UserStaffCpRelation;
import com.mall4j.cloud.user.service.GroupPushSonTaskService;
import com.mall4j.cloud.user.service.GroupPushTagService;
import com.mall4j.cloud.user.service.GroupPushTaskService;
import com.mall4j.cloud.user.service.GroupSonTaskSendRecordService;
import com.mall4j.cloud.user.service.StaffBatchSendCpMsgService;
import com.mall4j.cloud.user.service.async.AsyncGroupPushTaskService;
import com.mall4j.cloud.user.service.crm.CrmService;
import com.mall4j.cloud.user.vo.*;
import com.mall4j.cloud.user.vo.openapi.ApiResponse;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import me.chanjar.weixin.cp.bean.external.WxCpMsgTemplateAddResult;
import me.chanjar.weixin.cp.bean.external.msg.*;
import me.chanjar.weixin.cp.constant.WxCpConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RefreshScope
public class GroupPushTaskServiceImpl extends ServiceImpl<GroupPushTaskMapper, GroupPushTask> implements GroupPushTaskService {

    @Value("${mall4cloud.user.pushCdpTask}")
    private boolean pushCdpTask=false;
    @Resource
    private GroupPushTaskMapper groupPushTaskMapper;

    @Autowired
    private UserTagRelationManager userTagRelationManager;

    @Autowired
    private TagManager tagManager;

    @Autowired
    private CrmService crmService;

    @Resource
    private CrmUserTagRelationMapper crmUserTagRelationMapper;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private GroupPushTaskVipRelationManager groupPushTaskVipRelationManager;

    @Autowired
    private GroupPushTaskStaffRelationManager groupPushTaskStaffRelationManager;

    @Resource
    private GroupPushTaskStaffRelationMapper groupPushTaskStaffRelationMapper;

    @Autowired
    private GroupPushSonTaskManager groupPushSonTaskManager;

    @Resource
    private GroupPushSonTaskMapper groupPushSonTaskMapper;

    @Resource
    private GroupPushSonTaskService groupPushSonTaskService;

    @Autowired
    private GroupPushSonTaskMediaManager groupPushSonTaskMediaManager;

    @Autowired
    private GroupPushTaskManager groupPushTaskManager;

    //    @Autowired
    //    private OnsMQTemplate startGroupPushTemplate;

    @Autowired
    private StaffBatchSendCpMsgManager staffBatchSendCpMsgManager;

    @Resource
    private StaffBatchSendCpMsgService staffBatchSendCpMsgService;

    @Resource
    private GroupSonTaskSendRecordService groupSonTaskSendRecordService;

    @Resource
    private GroupPushSonTaskMediaMapper groupPushSonTaskMediaMapper;
//    @Autowired
//    private OnsMQTemplate endGroupPushTemplate;

    @Resource
    private GroupPushTagService groupPushTagService;

    @Resource
    private CpCustGroupClient cpCustGroupClient;

    @Autowired
    private UserStaffCpRelationManager userStaffCpRelationManager;

    @Resource
    private UserStaffCpRelationMapper userStaffCpRelationMapper;

    @Autowired
    private AsyncGroupPushTaskService asyncGroupPushTaskService;

    @Autowired
    private UploadUrlMediaProperties uploadUrlMediaProperties;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private MaterialFeignClient materialFeignClient;


    private final static Long PAGES = 1L;
    private final static Integer PAGE = 0;
    private final static Integer PAGE_SIZE = 2000;

    private final static Long NOT_BINDING_STAFF = 0L;

    //群发素材缓存失效时间
    private final static Long CP_EXTERN_CONTACT_ATTACHMENT_LIST_CACHE_TIME = 216000L;

    private final static String EXPORT_NULL_MASSAGE = "无相关明细数据导出";

    private final static String CREATING = "CREATING";

    private static final String FAIL = "FAIL";

    /**
     * 创建群发任务
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity<Void> addTask(AddPushTaskDTO addPushTaskDTO) {
        log.info("新建群发任务 params = {}", addPushTaskDTO);
        UserInfoInTokenBO tokenUser = AuthUserContext.get();
        GroupPushTask pushTask = new GroupPushTask();
        pushTask.setTaskName(addPushTaskDTO.getTaskName());
        pushTask.setTaskType(addPushTaskDTO.getTaskType());
        pushTask.setTaskMode(addPushTaskDTO.getTaskMode());
        pushTask.setCreateUserId(tokenUser.getUserId());
        pushTask.setTagType(addPushTaskDTO.getTagType());
        pushTask.setAllowSelect(Objects.nonNull(addPushTaskDTO.getAllowSelect())?addPushTaskDTO.getAllowSelect():0);
        pushTask.setOperateStatus(GroupPushTaskOperateStatusEnum.DRAFT.getOperateStatus());
        //新增群发任务记录
        this.baseMapper.insert(pushTask);

        // 新增相关信息
        addPushTaskDTO.setGroupPushTaskId(pushTask.getGroupPushTaskId());
        addPushTaskDTO.setCreateUserId(tokenUser.getUserId());
        saveRelationInfo(addPushTaskDTO);
        return ServerResponseEntity.success();
    }

    private void saveRelationInfo(AddPushTaskDTO addPushTaskDTO) {
        if(addPushTaskDTO.getTaskType()==0 && CollUtil.isEmpty(addPushTaskDTO.getStaffList())){
            throw new LuckException("非CDP任务执行员工不能为空");
        }
        // 发送员工信息
        if(addPushTaskDTO.getTaskType()==0 && CollUtil.isNotEmpty(addPushTaskDTO.getStaffList())){
            List<GroupPushTaskStaffRelation> staffList = addPushTaskDTO.getStaffList();
            ServerResponseEntity<List<StaffVO>> staffs = staffFeignClient
                    .getStaffByIds(staffList.stream().map(GroupPushTaskStaffRelation::getStaffId).collect(Collectors.toList()));
            Map<Long, List<StaffVO>> staffUserMap = staffs.getData().stream().collect(Collectors.groupingBy(StaffVO::getId));
            staffList.forEach(v-> {
                v.setGroupPushTaskId(addPushTaskDTO.getGroupPushTaskId());
                v.setStaffCpUserId(staffUserMap.get(v.getStaffId()).get(0).getQiWeiUserId());
            });
            groupPushTaskStaffRelationMapper.insertBatch(staffList);
        }


        // 任务模式：0-客户群发 1-群群发
        Integer taskMode = addPushTaskDTO.getTaskMode();

        if (taskMode == 0) {
        // 触达人群：0-全部客户触达 1-按客户标签筛选 2-按客户分组筛选 3-按部门员工筛选
        Integer tagType = addPushTaskDTO.getTagType();
            if (tagType != 0) {
                // 选择标签
                List<GroupPushTag> groupPushTags = addPushTaskDTO.getTagList();
                groupPushTags.forEach(v -> v.setGroupPushTaskId(addPushTaskDTO.getGroupPushTaskId()));
                groupPushTagService.saveBatch(groupPushTags);
            }
        }

        List<AddPushSonTaskMediaBO> mediaBOList = new ArrayList<>();
        for (AddPushSonTaskDTO addPushSonTaskDTO : addPushTaskDTO.getPushSonTaskList()) {
            //新建推送子任务
            AddGroupPushSonTaskBO sonTaskBO = new AddGroupPushSonTaskBO();
            BeanUtils.copyProperties(addPushSonTaskDTO, sonTaskBO);
            sonTaskBO.setTaskType(addPushTaskDTO.getTaskType());
            sonTaskBO.setGroupPushTaskId(addPushTaskDTO.getGroupPushTaskId());
            sonTaskBO.setCreateUserId(addPushTaskDTO.getCreateUserId());
            if (addPushTaskDTO.getTaskType() == 1) {
                sonTaskBO.setStartTime(null);
                sonTaskBO.setEndTime(null);
            }
            //新增群发子任务并获取子任务ID
            Long sonTaskId = groupPushSonTaskManager.add(sonTaskBO);
            //组装子任务素材列表
            List<AddPushSonTaskMediaBO> thisMediaBOList = addPushSonTaskDTO.getMediaList().stream().map(media -> {
                AddPushSonTaskMediaBO pushSonTaskMediaBO = new AddPushSonTaskMediaBO();

                BeanUtils.copyProperties(media, pushSonTaskMediaBO);
                pushSonTaskMediaBO.setGroupPushTaskId(addPushTaskDTO.getGroupPushTaskId());
                pushSonTaskMediaBO.setCreateUserId(addPushTaskDTO.getCreateUserId());
                pushSonTaskMediaBO.setGroupPushSonTaskId(sonTaskId);
                return pushSonTaskMediaBO;
            }).collect(Collectors.toList());

            mediaBOList.addAll(thisMediaBOList);
        }

        //新增子任务素材
        groupPushSonTaskMediaManager.addBatch(mediaBOList);
    }

    /**
     * 任务最终创建逻辑
     */
    //@Transactional(rollbackFor = Exception.class)
    @Override
    public void wrappingGroupPushTask(GroupPushTask groupPushTask) {
        // 触达人群：0-全部客户触达 1-按客户标签筛选 2-按客户分组筛选
        Integer tagType = groupPushTask.getTagType();
        List<String> tagIdList = new ArrayList<>();
        List<String> selectTagUnionIdList = new ArrayList<>();
        List<String> selectStgIdList = new ArrayList<>();
        if (tagType == 1) {
            List<GroupPushTag> tagList = groupPushTagService.list(new LambdaQueryWrapper<GroupPushTag>()
                .eq(GroupPushTag::getGroupPushTaskId, groupPushTask.getGroupPushTaskId()));
            tagIdList = crmUserTagRelationMapper
                .listUnionIdByTagId(tagList.stream().map(GroupPushTag::getTagId).collect(
                    Collectors.toList()));
            selectTagUnionIdList=tagIdList;
        } else if (tagType == 2) {
            List<GroupPushTag> tagList = groupPushTagService.list(new LambdaQueryWrapper<GroupPushTag>()
                .eq(GroupPushTag::getGroupPushTaskId, groupPushTask.getGroupPushTaskId()));
            tagIdList = tagList.stream().map(GroupPushTag::getTagId).collect(
                Collectors.toList());
            selectStgIdList=tagIdList;
        }
        // 查询对应子任务
        List<GroupPushSonTask> sonTaskList = groupPushSonTaskMapper.selectList(new LambdaUpdateWrapper<GroupPushSonTask>()
            .eq(GroupPushSonTask::getGroupPushTaskId, groupPushTask.getGroupPushTaskId()));

        // 查询对应的staff
        List<GroupPushTaskStaffRelation> staffRelations = groupPushTaskStaffRelationMapper
            .selectList(new LambdaUpdateWrapper<GroupPushTaskStaffRelation>()
                .eq(GroupPushTaskStaffRelation::getGroupPushTaskId, groupPushTask.getGroupPushTaskId()));

        List<StaffBatchSendCpMsg> staffBatchSendCpMsgs = new ArrayList<>();
        List<GroupSonTaskSendRecord> groupSonTaskSendRecordList = new ArrayList<>();

        List<Long> staffIdList = staffRelations.stream().map(GroupPushTaskStaffRelation::getStaffId)
            .collect(Collectors.toList());
        Integer taskMode = groupPushTask.getTaskMode();
        if (taskMode == 0) {
            // 客户群发
            // 查询staff对应的好友
            List<UserStaffCpRelation> userStaffCpRelations = userStaffCpRelationManager.getByStaffIds(staffIdList,selectTagUnionIdList,selectStgIdList);
            if (CollectionUtil.isEmpty(userStaffCpRelations)) {
                return;
            }
            // 排除不是好友的数据 并以staffId分组
            Map<Long, List<UserStaffCpRelation>> staffUserMap = userStaffCpRelations.stream().filter(v -> v.getStatus() == 1)
                .collect(Collectors.groupingBy(UserStaffCpRelation::getStaffId));

            if (CollectionUtil.isEmpty(staffUserMap)) {
                return;
            }

            // 组装数据
            for (GroupPushSonTask sonTask : sonTaskList) {
                for (GroupPushTaskStaffRelation userStaffCpRelation : staffRelations) {
                    List<UserStaffCpRelation> relationList = staffUserMap.get(userStaffCpRelation.getStaffId());
                    if (CollectionUtil.isEmpty(relationList)) {
                        // 若无好友则直接调过
                        continue;
                    }
                    StaffBatchSendCpMsg staffBatchSendCpMsg = new StaffBatchSendCpMsg(sonTask.getGroupPushTaskId(),
                        sonTask.getGroupPushSonTaskId(), groupPushTask.getTaskMode(), userStaffCpRelation.getStaffId(),
                        userStaffCpRelation.getStaffName(), 0, userStaffCpRelation.getStaffCpUserId(), 0, 0,
                        relationList.size());
                    staffBatchSendCpMsgs.add(staffBatchSendCpMsg);

                    // 组装发送记录
                    int sendCount = 0;
                    for (UserStaffCpRelation staffCpRelation : relationList) {
                        // 判断标签
//                        if (tagType == 1) {
//                            if (StringUtils.isBlank(staffCpRelation.getUserUnionId()) || !tagIdList
//                                .contains(staffCpRelation.getUserUnionId())) {
//                                continue;
//                            }
//                        } else if (tagType == 2) {
//                            if (null == staffCpRelation.getStageId() || !tagIdList.contains(staffCpRelation.getStageId().toString())) {
//                                continue;
//                            }
//                        }
                        GroupSonTaskSendRecord sendRecord = new GroupSonTaskSendRecord(sonTask.getGroupPushSonTaskId(),
                            sonTask.getGroupPushTaskId(), groupPushTask.getTaskMode(), groupPushTask.getTaskType(),
                            sonTask.getSonTaskName(), staffCpRelation.getStaffId(), staffBatchSendCpMsg.getStaffName(),
                            staffCpRelation.getQiWeiStaffId(), 0, 2, staffCpRelation.getQiWeiUserId(),
                            staffCpRelation.getQiWeiNickName(), staffCpRelation.getCpRemark(),
                            StringUtils.isBlank(staffCpRelation.getCpRemarkMobiles()) ? "[]"
                                : staffCpRelation.getCpRemarkMobiles(), staffCpRelation.getUserUnionId());
                        groupSonTaskSendRecordList.add(sendRecord);
                        sendCount++;
                    }
                    staffBatchSendCpMsg.setHeadCount(sendCount);
                }
            }
        } else {
            // 群群发
            // 查询员工拥有的群
            ServerResponseEntity<List<CustGroupVO>> groupByStaffIdList = cpCustGroupClient.getGroupByStaffIdList(staffIdList);
            List<CustGroupVO> groupList = groupByStaffIdList.getData();
            if (CollectionUtils.isEmpty(groupList)) {
                return;
            }
            Map<Long, List<CustGroupVO>> groupMap = groupList.stream()
                .collect(Collectors.groupingBy(CustGroupVO::getOwnerId));
            // 组装数据
            for (GroupPushSonTask sonTask : sonTaskList) {
                for (GroupPushTaskStaffRelation userStaffCpRelation : staffRelations) {
                    List<CustGroupVO> groupVOS = groupMap.get(userStaffCpRelation.getStaffId());
                    if(CollUtil.isEmpty(groupVOS)){
                        continue;
                    }
                    StaffBatchSendCpMsg staffBatchSendCpMsg = new StaffBatchSendCpMsg(sonTask.getGroupPushTaskId(),
                        sonTask.getGroupPushSonTaskId(), groupPushTask.getTaskMode(), userStaffCpRelation.getStaffId(),
                        userStaffCpRelation.getStaffName(), 0, groupVOS.get(0).getUserId(), 0, 0, groupVOS.size());
                    staffBatchSendCpMsgs.add(staffBatchSendCpMsg);

                    // 组装发送记录
                    for (CustGroupVO group : groupVOS) {
                        GroupSonTaskSendRecord sendRecord = new GroupSonTaskSendRecord(
                            sonTask.getGroupPushSonTaskId(), sonTask.getGroupPushTaskId(),
                            groupPushTask.getTaskMode(), groupPushTask.getTaskType(), sonTask.getSonTaskName(),
                            userStaffCpRelation.getStaffId(), staffBatchSendCpMsg.getStaffName(), group.getUserId(), 0, 2,
                            group.getId(), group.getGroupName(), null, null, null);
                        groupSonTaskSendRecordList.add(sendRecord);
                    }
                }
            }
        }
        if (!CollectionUtil.isEmpty(staffBatchSendCpMsgs)) {
            staffBatchSendCpMsgService.saveBatch(staffBatchSendCpMsgs);
            groupSonTaskSendRecordService.saveBatch(groupSonTaskSendRecordList);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity createFail(GroupPushTask groupPushTask) {
       /* //        GroupPushTask pushTask = getById(addPushTaskDTO.getGroupPushTaskId());
        groupPushTask.setOperateStatus(GroupPushTaskOperateStatusEnum.ENABLE_FAIL.getOperateStatus());
        groupPushTask.setFailParam(JSONObject.toJSONString(groupPushTask));
        updateById(groupPushTask);
*/
        return ServerResponseEntity.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity updateFail(EditPushTaskDTO editPushTaskDTO) {
        GroupPushTask pushTask = getById(editPushTaskDTO.getGroupPushTaskId());
        // pushTask.setOperateStatus(GroupPushTaskOperateStatusEnum.UPDATE_FAIL.getCreateStatus());
        pushTask.setFailParam(JSONObject.toJSONString(editPushTaskDTO));
        updateById(pushTask);

        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageVO<GroupPushTaskPageVO>> getPage(QueryGroupPushTaskPageDTO pageDTO) {

        IPage<GroupPushTaskPageVO> page = new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize());

        //查询群发主任务分页数据
        page = groupPushTaskMapper.selectGroupPushTaskPage(page, pageDTO);

        //校验是否存在相关数据
        if (CollectionUtil.isNotEmpty(page.getRecords())) {

            //聚合任务ID
            List<Long> taskIdList = page.getRecords().stream()
                .map(GroupPushTaskPageVO::getGroupPushTaskId)
                .collect(Collectors.toList());
            //聚合创建人ID
            List<Long> createUserIdList = page.getRecords().stream()
                .map(GroupPushTaskPageVO::getCreateUserId)
                .distinct()
                .collect(Collectors.toList());

            //执行人集合
            List<GroupPushTaskStaffRelation> groupPushTaskStaffRelations = groupPushTaskStaffRelationMapper
                .selectList(new LambdaUpdateWrapper<GroupPushTaskStaffRelation>()
                    .in(GroupPushTaskStaffRelation::getGroupPushTaskId, taskIdList));
            //根据任务ID进行聚合
            Map<Long, List<GroupPushTaskStaffRelation>> staffRelationMap = groupPushTaskStaffRelations.stream()
                .collect(Collectors.groupingBy(GroupPushTaskStaffRelation::getGroupPushTaskId));

            //获取创建人相关数据
            List<SysUserVO> sysUserVOList = tagManager.getCreateUserByIdList(createUserIdList);
            //根据ID进行聚合
            Map<Long, SysUserVO> sysUserVOMap = sysUserVOList.stream()
                .collect(Collectors.toMap(SysUserVO::getSysUserId, sysUserVO -> sysUserVO));

            //进行遍历包装
            page.getRecords().forEach(taskVO -> {
                // 执行人
                taskVO.setStaffRelationList(staffRelationMap.get(taskVO.getGroupPushTaskId()));

                //包装创建人数据
                SysUserVO sysUserVO;
                if (Objects.nonNull(sysUserVO = sysUserVOMap.get(taskVO.getCreateUserId()))) {
                    taskVO.setCreateUserNickName(sysUserVO.getNickName());
                }
            });
        }

        PageVO<GroupPushTaskPageVO> pageVO = new PageVO<>();

        pageVO.setPages((int) page.getPages());
        pageVO.setList(page.getRecords());
        pageVO.setTotal(page.getTotal());
        return ServerResponseEntity.success(pageVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity editTask(AddPushTaskDTO editPushTaskDTO) {
        log.info("修改群发任务 params = {}", editPushTaskDTO);
        UserInfoInTokenBO tokenUser = AuthUserContext.get();
        GroupPushTask groupPushTask = getById(editPushTaskDTO.getGroupPushTaskId());
        if (groupPushTask == null || groupPushTask.getDeleteFlag() == 1) {
            throw new LuckException("任务不存在");
        }
        if (!groupPushTask.getOperateStatus().equals(GroupPushTaskOperateStatusEnum.DRAFT.getOperateStatus())) {
            throw new LuckException("只有草稿状态下的任务才能编辑");
        }
        groupPushTask.setTaskName(editPushTaskDTO.getTaskName());
        groupPushTask.setUpdateUserId(tokenUser.getUserId());
        groupPushTask.setUpdater(tokenUser.getUsername());
        groupPushTask.setTagType(editPushTaskDTO.getTagType());
        groupPushTask.setAllowSelect(Objects.nonNull(editPushTaskDTO.getAllowSelect())?editPushTaskDTO.getAllowSelect():0);
        updateById(groupPushTask);

        // 删除相关信息记录
        groupPushSonTaskMapper.delete(new LambdaQueryWrapper<GroupPushSonTask>()
            .eq(GroupPushSonTask::getGroupPushTaskId, editPushTaskDTO.getGroupPushTaskId()));
        groupPushSonTaskMediaMapper.delete(new LambdaQueryWrapper<GroupPushSonTaskMedia>()
            .eq(GroupPushSonTaskMedia::getGroupPushTaskId, editPushTaskDTO.getGroupPushTaskId()));
        groupPushTaskStaffRelationManager.removeByPushTaskId(editPushTaskDTO.getGroupPushTaskId());
        groupPushTagService.remove(new LambdaQueryWrapper<GroupPushTag>()
            .eq(GroupPushTag::getGroupPushTaskId, editPushTaskDTO.getGroupPushTaskId()));

        // 新增相关信息
        editPushTaskDTO.setCreateUserId(tokenUser.getUserId());
        saveRelationInfo(editPushTaskDTO);
        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity enableOrDisableTask(Long pushTaskId) {

        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        GroupPushTask groupPushTask = getById(pushTaskId);
        if(GroupPushTaskOperateStatusEnum.DISBALE.getOperateStatus().equals(groupPushTask.getOperateStatus())){
            throw new LuckException("任务已禁用不允许启用");
        }
        if (GroupPushTaskOperateStatusEnum.DRAFT.getOperateStatus().equals(groupPushTask.getOperateStatus())) {
            groupPushTask.setOperateStatus(GroupPushTaskOperateStatusEnum.SUCCESS.getOperateStatus());
            groupPushTask.setUpdateUserId(tokenUser.getUserId());
            updateById(groupPushTask);
            // 修改子任务为启用
            groupPushSonTaskService.update(new LambdaUpdateWrapper<GroupPushSonTask>()
                .eq(GroupPushSonTask::getGroupPushTaskId, pushTaskId).set(GroupPushSonTask::getDeleteFlag, 0));
            // CDP任务要同步crm
            if (groupPushTask.getTaskType() == 1) {
                if(pushCdpTask){
                    TaskSaveRequest taskSaveRequest = new TaskSaveRequest();
                    BeanUtils.copyProperties(groupPushTask, taskSaveRequest);
                    taskSaveRequest.setTaskId(groupPushTask.getGroupPushTaskId());
                    taskSaveRequest.setEnableState(1);
                    taskSaveRequest.setTagType(0);
                    taskSaveRequest.setCreateTime(DateUtil.format(groupPushTask.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    taskSaveRequest.setUpdateTime(DateUtil.format(groupPushTask.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                    taskSaveRequest.setUpdater(groupPushTask.getUpdater());
                    if (groupPushTask.getCreateUserId() != null) {
                        taskSaveRequest.setCreator(tokenUser.getUsername());
                    }
                    crmService.taskSave(taskSaveRequest);
                }
            } else {
                // 生成待发布的数据
                this.wrappingGroupPushTask(groupPushTask);
            }


            return ServerResponseEntity.success();
        } else {
            throw new LuckException("状态异常不允许启用");
        }
    }

    @Override
    public ServerResponseEntity disableTaskTask(Long pushTaskId) {
        GroupPushTask groupPushTask = getById(pushTaskId);
        if(Objects.isNull(groupPushTask)){
            throw new LuckException("任务数据未获取到");
        }
        if(groupPushTask.getTaskType()==1){
            throw new LuckException("CDP任务不允许禁用");
        }
        if (GroupPushTaskOperateStatusEnum.DISBALE.getOperateStatus().equals(groupPushTask.getOperateStatus())) {
            throw new LuckException("任务已禁用");
        }
        groupPushTask.setOperateStatus(GroupPushTaskOperateStatusEnum.DISBALE.getOperateStatus());
        groupPushTask.setUpdateTime(new Date());
        groupPushTask.setUpdater(AuthUserContext.get().getUsername());
        groupPushTask.setUpdateUserId(AuthUserContext.get().getUserId());
        this.updateById(groupPushTask);
        return ServerResponseEntity.success();
    }

    @Override
    public void wrappingEditGroupPushTask(EditPushTaskDTO editPushTaskDTO, boolean changeTag) {

     /*   //查询所有子任务列表
        List<GroupPushSonTaskVO> sonTaskList = groupPushSonTaskManager
            .getTheSonTaskListByTaskId(editPushTaskDTO.getGroupPushTaskId());

        //聚合需要新建的子任务列表
        List<EditPushSonTaskDTO> addSonTaskList = editPushTaskDTO.getPushSonTaskList().stream()
            .filter(sonTask -> Objects.isNull(sonTask.getGroupPushSonTaskId()))
            .collect(Collectors.toList());

        List<AddPushSonTaskMediaBO> mediaBOList = new ArrayList<>();

        //存在新建的子任务，先新建子任务
        if (CollectionUtil.isNotEmpty(addSonTaskList)) {
            addSonTaskList.stream()
                .forEach(addPushSonTaskDTO -> {
                    //新建推送子任务
                    AddGroupPushSonTaskBO sonTaskBO = new AddGroupPushSonTaskBO();
                    BeanUtils.copyProperties(addPushSonTaskDTO, sonTaskBO);
                    sonTaskBO.setCreateUserId(editPushTaskDTO.getUpdateUserId());
                    sonTaskBO.setGroupPushTaskId(editPushTaskDTO.getGroupPushTaskId());
                    //新增群发子任务并获取子任务ID
                    Long sonTaskId = groupPushSonTaskManager.add(sonTaskBO);
                    //组装子任务素材列表
                    List<AddPushSonTaskMediaBO> thisMediaBOList = addPushSonTaskDTO.getMediaList().stream().map(media -> {
                        AddPushSonTaskMediaBO pushSonTaskMediaBO = new AddPushSonTaskMediaBO();

                        BeanUtils.copyProperties(media, pushSonTaskMediaBO);

                        pushSonTaskMediaBO.setCreateUserId(editPushTaskDTO.getUpdateUserId());
                        pushSonTaskMediaBO.setGroupPushSonTaskId(sonTaskId);
                        return pushSonTaskMediaBO;
                    }).collect(Collectors.toList());

                    mediaBOList.addAll(thisMediaBOList);
                });
        }

        List<GroupPushSonTaskMediaBO> editMediaList = new ArrayList<>();

        //聚合需要修改的子任务IDList
        List<Long> pushSonTaskIdList = editPushTaskDTO.getPushSonTaskList().stream()
            .filter(editTask -> Objects.nonNull(editTask.getGroupPushSonTaskId()))
            .map(EditPushSonTaskDTO::getGroupPushSonTaskId)
            .collect(Collectors.toList());

        //筛选被删除的子任务
        List<Long> removeList = sonTaskList.stream()
            .filter(sonTask -> !pushSonTaskIdList.contains(sonTask.getGroupPushSonTaskId()))
            .map(GroupPushSonTaskVO::getGroupPushSonTaskId)
            .collect(Collectors.toList());

        //当存在需要删除的子任务
        if (CollectionUtil.isNotEmpty(removeList)) {
            //批量删除子任务
            groupPushSonTaskManager.removeBySonTaskIdList(removeList);

            //批量删除子任务素材
            groupPushSonTaskMediaManager.removeBySonTaskIdList(removeList);

            //发送异步消息，删除的子任务相关的企微推送任务需要取消
            *//**
             * 后续需求变动，草稿状态下可以修改任意数据。但是草稿状态并未创建企微推送任务等，所以不用删除
             *//*
            //endGroupPushTemplate.syncSend(removeList);
        }

        //获取需要修改的子任务（可能修改，记录未被删除的记录）
        List<EditPushSonTaskDTO> editSonTaskList = editPushTaskDTO.getPushSonTaskList().stream()
            .filter(editTask -> Objects.nonNull(editTask.getGroupPushSonTaskId()))
            .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(editSonTaskList)) {
            //遍历编辑的子任务列表
            editSonTaskList.stream().forEach(edit -> {
                //设置相关的属性
                GroupPushSonTaskBO groupPushSonTaskBO = new GroupPushSonTaskBO();

                groupPushSonTaskBO.setGroupPushSonTaskId(edit.getGroupPushSonTaskId());
                groupPushSonTaskBO.setSonTaskName(edit.getSonTaskName());
                groupPushSonTaskBO.setPushContent(edit.getPushContent());
                groupPushSonTaskBO.setStartTime(edit.getStartTime());
                groupPushSonTaskBO.setEndTime(edit.getEndTime());
                //更新子任务
                groupPushSonTaskManager.update(groupPushSonTaskBO);

                //查询子任务全部素材
                List<GroupPushSonTaskMediaVO> mediaList = groupPushSonTaskMediaManager
                    .getMediaListBySonTaskId(edit.getGroupPushSonTaskId());

                //聚合可能修改的子素材（未被删除的素材）
                List<Long> checkMedia = edit.getMediaList().stream()
                    .filter(media -> Objects.nonNull(media.getSonTaskMediaId()))
                    .map(EditPushSonTaskMediaDTO::getSonTaskMediaId)
                    .collect(Collectors.toList());

                //聚合已被删除的素材
                List<Long> removeIdList = mediaList.stream()
                    .filter(media -> !checkMedia.contains(media.getSonTaskMediaId()))
                    .map(GroupPushSonTaskMediaVO::getSonTaskMediaId)
                    .collect(Collectors.toList());

                if (CollectionUtil.isNotEmpty(removeIdList)) {
                    groupPushSonTaskMediaManager.removeByMediaIdList(removeIdList);
                }

                //聚合新增素材，批量处理新增的素材数据
                List<EditPushSonTaskMediaDTO> addMediaList = edit.getMediaList().stream()
                    .filter(media -> Objects.isNull(media.getSonTaskMediaId()))
                    .collect(Collectors.toList());

                List<AddPushSonTaskMediaBO> currentMediaBOList = addMediaList.stream().map(media -> {
                    AddPushSonTaskMediaBO pushSonTaskMediaBO = new AddPushSonTaskMediaBO();

                    BeanUtils.copyProperties(media, pushSonTaskMediaBO);

                    pushSonTaskMediaBO.setCreateUserId(editPushTaskDTO.getUpdateUserId());
                    pushSonTaskMediaBO.setGroupPushSonTaskId(edit.getGroupPushSonTaskId());
                    return pushSonTaskMediaBO;
                }).collect(Collectors.toList());

                mediaBOList.addAll(currentMediaBOList);

                //处理批量修改的素材数据
                List<GroupPushSonTaskMediaBO> currentEditMediList = edit.getMediaList().stream()
                    .filter(media -> Objects.nonNull(media.getSonTaskMediaId()))
                    .map(media -> {
                        GroupPushSonTaskMediaBO groupPushSonTaskMediaBO = new GroupPushSonTaskMediaBO();
                        groupPushSonTaskMediaBO.setSonTaskMediaId(media.getSonTaskMediaId());
                        groupPushSonTaskMediaBO.setMedia(media.getMedia());
                        groupPushSonTaskMediaBO.setType(media.getType());

                        return groupPushSonTaskMediaBO;
                    }).collect(Collectors.toList());

                editMediaList.addAll(currentEditMediList);
            });
        }

        //新增子任务素材
        if (CollectionUtil.isNotEmpty(mediaBOList)) {
            groupPushSonTaskMediaManager.addBatch(mediaBOList);
        }

        groupPushSonTaskMediaManager.updateBatch(editMediaList);

        // GroupPushTask groupPushTask = getById(editPushTaskDTO.getGroupPushTaskId());
        // groupPushTask.setOperateStatus(GroupPushTaskOperateStatusEnum.SUCCESS.getCreateStatus());
        // updateById(groupPushTask);*/

    }

    /**
     * 获取群发任务详情
     */
    @Override
    public GroupPushTaskDetailVO getTheGroupPushTask(Long pushTaskId) {

        //根据ID查询群发任务
        GroupPushTask groupPushTask = getById(pushTaskId);
        if (groupPushTask == null || groupPushTask.getDeleteFlag() == 1) {
            throw new LuckException("任务不存在");
        }

        GroupPushTaskDetailVO groupPushTaskDetailVO = new GroupPushTaskDetailVO();
        //复制属性
        BeanUtils.copyProperties(groupPushTask, groupPushTaskDetailVO);

        // 发送人员
        List<GroupPushTaskStaffRelation> staffRelationList = groupPushTaskStaffRelationMapper
            .selectList(new LambdaUpdateWrapper<GroupPushTaskStaffRelation>()
                .eq(GroupPushTaskStaffRelation::getGroupPushTaskId, pushTaskId));
        groupPushTaskDetailVO.setStaffList(staffRelationList);

        // 选择标签
        if (groupPushTask.getTagType() != 0) {
            List<GroupPushTag> groupPushTags = groupPushTagService.list(new LambdaUpdateWrapper<GroupPushTag>()
                .eq(GroupPushTag::getGroupPushTaskId, pushTaskId));
            groupPushTaskDetailVO.setTagList(groupPushTags);
        }

        //正常组装，子任务和子任务素材
        List<GroupPushSonTaskVO> sonTaskVOList = groupPushSonTaskManager.getTheSonTaskListByTaskId(pushTaskId);

        sonTaskVOList.forEach(sonTask -> {

            List<GroupPushSonTaskMediaVO> mediaVOList = groupPushSonTaskMediaManager
                .getMediaListBySonTaskId(sonTask.getGroupPushSonTaskId());

            sonTask.setGroupPushSonTaskMediaList(mediaVOList);

        });

        groupPushTaskDetailVO.setGroupPushSonTaskList(sonTaskVOList);
        return groupPushTaskDetailVO;
    }

    @Override
    public ServerResponseEntity<List<GroupPushTaskStatisticVO>> getStatistic(GetGroupPushTaskStatisticDTO statisticDTO) {

        List<GroupPushSonTaskVO> sonTaskList = groupPushSonTaskManager.getTheSonTaskListByTaskId(statisticDTO.getPushTaskId());

        List<GroupPushTaskStatisticVO> pushTaskStatisticVOS = sonTaskList.stream()
            .map(sonTask -> {
                GroupPushTaskStatisticVO statistic = this
                    .getSonTaskStatistic(statisticDTO.getPushTaskId(), sonTask.getGroupPushSonTaskId());
                statistic.setSonTaskId(sonTask.getGroupPushSonTaskId());
                statistic.setSonTaskName(sonTask.getSonTaskName());
                statistic.setStatus(sonTask.getStatus());
                return statistic;
            }).collect(Collectors.toList());

        return ServerResponseEntity.success(pushTaskStatisticVOS);
    }

    @Override
    public ServerResponseEntity removeTask(Long pushTaskId) {
        GroupPushTask pushTask = getById(pushTaskId);
        pushTask.setDeleteFlag(1);
        updateById(pushTask);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity downloadData(Long taskId, Long sonTaskId, HttpServletResponse response) {
        FinishDownLoadDTO finishDownLoadDTO;
        //拼接文件名称
        GroupPushTask pushTask = getById(taskId);
        GroupPushSonTaskVO sonTask = groupPushSonTaskManager.getById(sonTaskId);
        StringBuffer fileName = new StringBuffer();
        fileName.append("群发任务数据统计-");
        fileName.append(pushTask.getGroupPushTaskId());
        fileName.append("(" + "-" + sonTask.getGroupPushSonTaskId() + ")");
        fileName.append("-" + ExportGroupPushTaskStatisticsBO.EXCEL_NAME);
        //获取下载中心文件对应ID
        finishDownLoadDTO = userTagRelationManager.wrapperDownLoadCenterParam(fileName.toString());

        if (Objects.isNull(finishDownLoadDTO)) {
            return ServerResponseEntity.showFailMsg("文件下载失败，请重试");
        }
        asyncGroupPushTaskService.wrapperExcelData(taskId, sonTaskId, response, finishDownLoadDTO);
        return ServerResponseEntity.success("操作成功，请转至下载中心下载");
    }

    /**
     * 导购生成群发任务
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity startGroupPush(StartGroupPushDTO startGroupPushDTO) {
        // 校验
        Date now = new Date();
        GroupPushSonTaskVO sonTaskVO = groupPushSonTaskManager.getById(startGroupPushDTO.getSonTaskId());
        GroupPushTask task = this.getById(sonTaskVO.getGroupPushTaskId());
        if (!task.getOperateStatus().equals(GroupPushTaskOperateStatusEnum.SUCCESS.getOperateStatus())) {
            log.info("task = {}", task);
            throw new LuckException("当前主群发任务未启用");
        }
        long startTime = sonTaskVO.getStartTime().getTime();
        if (startTime > now.getTime()) {
            return ServerResponseEntity.showFailMsg("群发任务未开始，无法重复创建！");
        }

        long endTime = sonTaskVO.getEndTime().getTime();
        if (endTime < now.getTime()) {
            return ServerResponseEntity.showFailMsg("群发任务已结束，无法重复创建！");
        }

        // 导购发送列表
        StaffBatchSendCpMsgVO msgVO = staffBatchSendCpMsgManager
            .getBySonTaskIdAndStaffId(startGroupPushDTO.getSonTaskId(), startGroupPushDTO.getStaffId());

        if (Objects.nonNull(msgVO) && StrUtil.isNotEmpty(msgVO.getMsgId())) {
            return ServerResponseEntity.showFailMsg("群发任务已创建，无法重复创建！");
        }

        // 客户发送列表
        List<GroupSonTaskSendRecord> sendRecordList = groupSonTaskSendRecordService
            .list(new LambdaUpdateWrapper<GroupSonTaskSendRecord>()
                .eq(GroupSonTaskSendRecord::getPushSonTaskId, startGroupPushDTO.getSonTaskId())
                .eq(GroupSonTaskSendRecord::getStaffId, startGroupPushDTO.getStaffId()));

        //设置群发参数
        ExtendWxCpMsgTemplateDTO extendWxCpMsgTemplateDTO = new ExtendWxCpMsgTemplateDTO();
        //获取导购企微ID
        String staffCpUserId = msgVO.getStaffCpUserId();
        //获取已加好友的客户的企微ID
        List<String> friendList = sendRecordList.stream()
            .map(GroupSonTaskSendRecord::getVipCpUserId)
            .collect(Collectors.toList());

        Integer taskMode = task.getTaskMode();

        if (taskMode == 0) {
            extendWxCpMsgTemplateDTO.setChat_type("single");
            extendWxCpMsgTemplateDTO.setExternal_userid(friendList);
        } else {
            extendWxCpMsgTemplateDTO.setChat_type("group");
            extendWxCpMsgTemplateDTO.setChat_id_list(friendList);
        }
        log.info("发送客户企微id集合 {}", JSONObject.toJSONString(friendList));
        extendWxCpMsgTemplateDTO.setSender(staffCpUserId);

        //设置一个缓存临时素材的redisKey
        String redisKey = UserCacheNames.CP_EXTERN_CONTACT_ATTACHMENT_LIST + sonTaskVO.getGroupPushSonTaskId();
        List<Attachment> attachmentList = getAttachments(sonTaskVO.getGroupPushSonTaskId(), redisKey, startGroupPushDTO.getStaffId(),false);
        extendWxCpMsgTemplateDTO.setAttachments(attachmentList);
        Text text = new Text();
        log.info("SON TASK VO IS:{}", JSONObject.toJSONString(sonTaskVO));
        text.setContent(sonTaskVO.getPushContent());
        extendWxCpMsgTemplateDTO.setText(text);

        // 调用企微接口
        WxCpMsgTemplateAddResult wxCpMsgTemplateAddResult = groupPushTaskManager
            .addExternalContactMsgTemplate(extendWxCpMsgTemplateDTO.toJson());

        // 修改发送状态
        //int failCount = 0;
        List<String> failList = wxCpMsgTemplateAddResult.getFailList();
        LambdaUpdateWrapper<GroupSonTaskSendRecord> updateWrapper = new LambdaUpdateWrapper<GroupSonTaskSendRecord>()
            .eq(GroupSonTaskSendRecord::getPushSonTaskId, startGroupPushDTO.getSonTaskId())
            .eq(GroupSonTaskSendRecord::getStaffId, startGroupPushDTO.getStaffId())
            .set(GroupSonTaskSendRecord::getSendStatus, 0);
        groupSonTaskSendRecordService.update(updateWrapper);
        if (!CollectionUtils.isEmpty(failList)) {
            //failCount = failList.size();
            groupSonTaskSendRecordService.update(new LambdaUpdateWrapper<GroupSonTaskSendRecord>()
                .eq(GroupSonTaskSendRecord::getPushSonTaskId, startGroupPushDTO.getSonTaskId())
                .eq(GroupSonTaskSendRecord::getStaffId, startGroupPushDTO.getStaffId())
                .in(GroupSonTaskSendRecord::getVipCpUserId, failList)
                .set(GroupSonTaskSendRecord::getSendStatus, 2));
        }
        staffBatchSendCpMsgManager.updateMsgIdById(msgVO.getStaffBatchSendCpMsgId(), wxCpMsgTemplateAddResult.getMsgId(),null);
        return ServerResponseEntity.success();
    }

  /*  *//**
     * 导购生成群发任务消费者逻辑
     *//*
    //@Transactional(rollbackFor = Exception.class)
    @Override
    public void startCpGroupPush(StartGroupPushDTO startGroupPushDTO, GroupPushSonTaskVO sonTaskVO) {

        //设置群发参数
        ExtendWxCpMsgTemplateDTO extendWxCpMsgTemplateDTO = new ExtendWxCpMsgTemplateDTO();
        //获取导购企微ID
        String staffCpUserId = vipList.get(0).getStaffCpUserId();
        //获取已加好友的客户的企微ID
        List<String> friendList = vipList.stream()
            .map(UsableGroupPushVipVO::getVipCpUserId)
            .collect(Collectors.toList());

        extendWxCpMsgTemplateDTO.setChatType("single");
        log.info("GROUP PUSH FRIEND LIST IS:{}", JSONObject.toJSONString(friendList));
        extendWxCpMsgTemplateDTO.setExternalUserid(friendList);
        extendWxCpMsgTemplateDTO.setSender(staffCpUserId);

        //组装发送记录
        List<AddGroupSonTaskSendRecordBO> addGroupSonTaskSendRecordBOS = vipList.stream().map(vip -> {
            AddGroupSonTaskSendRecordBO addGroupSonTaskSendRecordBO = new AddGroupSonTaskSendRecordBO();

            addGroupSonTaskSendRecordBO.setPushSonTaskId(sonTaskVO.getGroupPushSonTaskId());
            addGroupSonTaskSendRecordBO.setPushTaskId(sonTaskVO.getGroupPushTaskId());
            addGroupSonTaskSendRecordBO.setSendModel(GroupPushTaskSendModelEnum.TO_GROUP_PUSH.value());
            addGroupSonTaskSendRecordBO.setSendStatus(GroupPushTaskSendStatusEnum.NOT_SENT.getSendStatus());
            addGroupSonTaskSendRecordBO.setStaffId(vip.getStaffId());
            addGroupSonTaskSendRecordBO.setVipUserId(vip.getVipUserId());
            addGroupSonTaskSendRecordBO.setVipCpUserId(vip.getVipCpUserId());

            return addGroupSonTaskSendRecordBO;
        }).collect(Collectors.toList());
        //新增发送记录关系
        groupSonTaskSendRecordManager.addBatch(addGroupSonTaskSendRecordBOS);

        //设置一个缓存临时素材的redisKey
        String redisKey = CP_EXTERN_CONTACT_ATTACHMENT_LIST + sonTaskVO.getGroupPushSonTaskId();

        List<Attachment> attachmentList = getAttachments(sonTaskVO.getGroupPushSonTaskId(), redisKey, false);

        extendWxCpMsgTemplateDTO.setAttachments(attachmentList);
        Text text = new Text();
        log.info("SON TASK VO IS:{}", JSONObject.toJSONString(sonTaskVO));
        text.setContent(sonTaskVO.getPushContent());
        extendWxCpMsgTemplateDTO.setText(text);

        WxCpMsgTemplateAddResult wxCpMsgTemplateAddResult = groupPushTaskManager
            .addExternalContactMsgTemplate(extendWxCpMsgTemplateDTO);

        StaffBatchSendCpMsgVO sendCpMsgVO = staffBatchSendCpMsgManager
            .getBySonTaskIdAndStaffId(startGroupPushDTO.getSonTaskId(), startGroupPushDTO.getStaffId());

        staffBatchSendCpMsgManager.updateMsgIdById(sendCpMsgVO.getStaffBatchSendCpMsgId(), wxCpMsgTemplateAddResult.getMsgId());

    }*/

    @Override
    public List<Attachment> getAttachments(Long sonTaskId, String redisKey,Long staffId, boolean urlFlag) {

        redisKey = redisKey + urlFlag;

        //查询子任务所有的素材
        List<GroupPushSonTaskMediaVO> mediaVOList = groupPushSonTaskMediaManager.getMediaListBySonTaskId(sonTaskId);
        List<Attachment> attachmentList = new ArrayList<>();

        List<MediaJsonVO> mediaJsonVOList = new ArrayList<>();

        mediaVOList.forEach(media -> {
                MediaJsonVO mediaJsonVO = JSONObject.parseObject(media.getMedia(), MediaJsonVO.class);
                mediaJsonVO.setSonTaskMediaId(media.getSonTaskMediaId());
                mediaJsonVOList.add(mediaJsonVO);
            });

        //1、判断是否有特殊需求场景的素材
        List<MediaJsonVO> businessList = mediaJsonVOList.stream()
            .filter(mediaJsonVO -> StringUtils.isNotEmpty(mediaJsonVO.getAppPage()))
            .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(businessList)) {
            //2、特殊场景的素材每次都需要直接上报
            List<Attachment> appPageAttachmentList = this.getAttachmentList(businessList,staffId, urlFlag);
            attachmentList.addAll(appPageAttachmentList);
        }
        //3、从缓存中获取当前子任的素材，没有的情况判断是否存在 非特殊需求场景 的素材，是的话进行这部分素材上报，随后缓存
        List<MediaJsonVO> normalList = mediaJsonVOList.stream()
            .filter(mediaJsonVO -> StringUtils.isEmpty(mediaJsonVO.getAppPage()))
            .collect(Collectors.toList());

        //从缓存中获取素材，如果没有则重新上传
        if (CollectionUtil.isNotEmpty(normalList)) {
            List<Attachment> normalAttachmentList;
            if (!RedisUtil.hasKey(redisKey) || RedisUtil.getExpire(redisKey) < 0) {
                normalAttachmentList = this.getAttachmentList(normalList,staffId, urlFlag);
                RedisUtil.set(UserCacheNames.CP_EXTERN_CONTACT_ATTACHMENT_LIST + sonTaskId,
                    normalAttachmentList, CP_EXTERN_CONTACT_ATTACHMENT_LIST_CACHE_TIME);
            } else {
                normalAttachmentList = RedisUtil.get(redisKey);
                log.info("缓存获取素材ATTACHMENT CACHE DATA:{}", JSONObject.toJSONString(attachmentList));
            }
            attachmentList.addAll(normalAttachmentList);
        }

        return attachmentList;
    }

    @Override
    public List<GroupPushTaskRecordStatisticVO> getTaskRecordStatistic(QueryGroupPushRecordDTO params) {
        List<GroupPushTaskRecordStatisticVO> groupPushTaskRecordStatisticVOS = groupSonTaskSendRecordService.getTaskRecordStatistic(params);

        long betweenDay = DateUtil.between(params.getStartTime(), params.getEndTime(), DateUnit.DAY);
        List<String> dateList = new ArrayList<>();
        dateList.add(DateUtil.format(params.getStartTime(), "yyyy-MM-dd"));
        for (int i = 0; i < betweenDay; i++) {
            dateList.add(DateUtil.format(DateUtil.offsetDay(params.getStartTime(), i + 1), "yyyy-MM-dd"));
        }
        List<GroupPushTaskRecordStatisticVO> result = new ArrayList<>();
        for (String s : dateList) {
            GroupPushTaskRecordStatisticVO r = new GroupPushTaskRecordStatisticVO();
            result.add(r);
            r.setDate(s);
            r.setCount(0);
            for (GroupPushTaskRecordStatisticVO groupPushTaskRecordStatisticVO : groupPushTaskRecordStatisticVOS) {
                if (r.getDate().equals(groupPushTaskRecordStatisticVO.getDate())) {
                    r.setCount(groupPushTaskRecordStatisticVO.getCount());
                }
            }
        }
        return result;
    }

    @Override
    public ServerResponseEntity<PageVO<GroupPushTaskDetailStatisticVO>> getTaskDetailStatistic(
        QueryGroupPushTaskPageDetailDTO params) {
        LambdaQueryWrapper<GroupSonTaskSendRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(params.getUserName())) {
            queryWrapper.like(GroupSonTaskSendRecord::getQiWeiNickName, params.getUserName());
        }
        if (params.getPhone() != null) {
            queryWrapper.like(GroupSonTaskSendRecord::getCpRemarkMobiles, params.getPhone());
        }
        if (params.getSendStatus() != null) {
            queryWrapper.eq(GroupSonTaskSendRecord::getSendStatus, params.getSendStatus());
        }
        queryWrapper.orderByDesc(GroupSonTaskSendRecord::getCreateTime);
        Page<GroupSonTaskSendRecord> page = groupSonTaskSendRecordService.page(new Page<>(params.getPageNum(), params.getPageSize()), queryWrapper);
        List<GroupSonTaskSendRecord> records = page.getRecords();
        PageVO<GroupPushTaskDetailStatisticVO> pageVO = new PageVO<>();
        if (CollectionUtils.isEmpty(records)) {
            return ServerResponseEntity.success(pageVO);
        }
        List<GroupPushTaskDetailStatisticVO> list = new ArrayList<>();
        for (GroupSonTaskSendRecord record : records) {
            GroupPushTaskDetailStatisticVO g = new GroupPushTaskDetailStatisticVO();
            g.setTaskMode(record.getTaskMode());
            g.setStaffName(record.getStaffName());
            g.setCreatTime(record.getCreateTime());
            g.setSonTaskName(record.getSonTaskName());
            g.setUserName(record.getQiWeiNickName());
            g.setUserRemark(record.getCpRemark());
            g.setPhone(record.getCpRemarkMobiles());
            g.setSendStatus(record.getSendStatus());
            g.setTaskType(record.getTaskType());
            list.add(g);
        }
        pageVO.setPages((int) page.getPages());
        pageVO.setList(list);
        pageVO.setTotal(page.getTotal());

        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public ServerResponseEntity<PageVO<GroupPushSonTaskDetailStatisticVO>> getSonTaskDetailStatistic(
        QueryGroupPushSonTaskPageDetailDTO params) {
        LambdaQueryWrapper<StaffBatchSendCpMsg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StaffBatchSendCpMsg::getPushSonTaskId, params.getSonTaskId()).eq(StaffBatchSendCpMsg::getTaskMode, params.getTaskMode());
        if (StringUtils.isNotBlank(params.getStaffName())) {
            queryWrapper.like(StaffBatchSendCpMsg::getStaffName, params.getStaffName());
        }
        if (params.getStartTime() != null) {
            queryWrapper.ge(StaffBatchSendCpMsg::getFinishTime, params.getStartTime());
        }
        if (params.getEndTime() != null) {
            queryWrapper.le(StaffBatchSendCpMsg::getFinishTime, params.getEndTime());
        }
        if (params.getFinishState() != null) {
            queryWrapper.eq(StaffBatchSendCpMsg::getFinishState, params.getFinishState());
        }

        Page<StaffBatchSendCpMsg> page = staffBatchSendCpMsgService.page(new Page<>(params.getPageNum(), params.getPageSize()), queryWrapper);
        List<StaffBatchSendCpMsg> records = page.getRecords();
        PageVO<GroupPushSonTaskDetailStatisticVO> pageVO = new PageVO<>();
        if (CollectionUtils.isEmpty(records)) {
            return ServerResponseEntity.success(pageVO);
        }
        List<GroupPushSonTaskDetailStatisticVO> list = new ArrayList<>();
        for (StaffBatchSendCpMsg record : records) {
            GroupPushSonTaskDetailStatisticVO g = new GroupPushSonTaskDetailStatisticVO();
            g.setFinishState(record.getFinishState());
            g.setFinishTime(record.getFinishTime());
            g.setStaffName(record.getStaffName());
            g.setPushIssueFinishCount(record.getReachCount());
            g.setIssueCount(record.getHeadCount());
            list.add(g);
        }
        pageVO.setPages((int) page.getPages());
        pageVO.setList(list);
        pageVO.setTotal(page.getTotal());

        return ServerResponseEntity.success(pageVO);
    }

    @Override
    public List<SoldGroupPushSonTaskDetailStatisticVO> soldSonTaskDetailStatistic(QueryGroupPushSonTaskPageDetailDTO params) {
        LambdaQueryWrapper<StaffBatchSendCpMsg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StaffBatchSendCpMsg::getPushSonTaskId, params.getSonTaskId()).eq(StaffBatchSendCpMsg::getTaskMode, params.getTaskMode());
        if (StringUtils.isNotBlank(params.getStaffName())) {
            queryWrapper.like(StaffBatchSendCpMsg::getStaffName, params.getStaffName());
        }
        if (params.getStartTime() != null) {
            queryWrapper.ge(StaffBatchSendCpMsg::getFinishTime, params.getStartTime());
        }
        if (params.getEndTime() != null) {
            queryWrapper.le(StaffBatchSendCpMsg::getFinishTime, params.getEndTime());
        }
        List<StaffBatchSendCpMsg> records=staffBatchSendCpMsgService.list(queryWrapper);
        if (CollectionUtils.isEmpty(records)) {
            return ListUtil.empty();
        }
        List<SoldGroupPushSonTaskDetailStatisticVO> list = new ArrayList<>();
        for (StaffBatchSendCpMsg record : records) {
            SoldGroupPushSonTaskDetailStatisticVO g = mapperFacade.map(record,SoldGroupPushSonTaskDetailStatisticVO.class);
            //完成状态；0未完成，1完成
            if(Objects.nonNull(record.getFinishState())){
                if(record.getFinishState()==0){
                    g.setFinishState("否");
                }
                if(record.getFinishState()==1){
                    g.setFinishState("是");
                }
            }
            g.setStaffName(record.getStaffName());
            g.setIssueCount(record.getReachCount()+"/"+record.getHeadCount());
            list.add(g);
        }
        return list;
    }

    @Override
    //@Transactional(rollbackFor = Exception.class)
    public ApiResponse crmSendTaskNew(SendTaskDTO request) {
        log.info("crmSendTaskNew 入参 = {}", request);
        Long taskId = request.getTaskId();
        GroupPushTask task = this.getById(taskId);
        if (task == null || task.getDeleteFlag() == 1 || task.getTaskType() != 1 || task.getTaskMode() != 0) {
            return ApiResponse.fail("任务不存在或已删除");
        }
        if(Objects.nonNull(task.getExecuteStatus()) && task.getExecuteStatus()>0){
            return ApiResponse.fail("任务执行中");
        }
        //校验主任务是否正常
        if(task.getOperateStatus()!= GroupPushTaskOperateStatusEnum.SUCCESS.getOperateStatus()){
            log.info("任务执行失败，主任务未启用：{}", JSON.toJSONString(task));
            return ApiResponse.fail("任务执行失败，主任务未启用");
        }
        // 子任务
        List<GroupPushSonTask> groupPushSonTasks = groupPushSonTaskMapper
                .selectList(new LambdaUpdateWrapper<GroupPushSonTask>().eq(GroupPushSonTask::getGroupPushTaskId, taskId));
        if (CollectionUtil.isEmpty(groupPushSonTasks)) {
            log.warn("未找到字任务数据");
            return ApiResponse.fail("任务执行失败，未找到字任务数据");
        }
        // 发送对象
        List<String> userIds = request.getUserIds().stream().distinct().collect(Collectors.toList());
        // 查询staff对应的好友
        List<UserStaffCpRelation> userStaffCpRelations = userStaffCpRelationManager.getByQiWeiUserIds(userIds)
                .stream().filter(item->Objects.nonNull(item.getStaffId())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(userStaffCpRelations)) {
            log.warn("未找到对应好友记录");
            return ApiResponse.fail("任务执行失败，未找到对应好友记录");
        }
        List<Long> staffIds=userStaffCpRelations.stream().filter(item->Objects.nonNull(item.getStaffId())).map(item->item.getStaffId()).collect(Collectors.toList());
        //根据客户对应员工分组
        StaffQueryDTO staffQueryDTO=new StaffQueryDTO();
        staffQueryDTO.setStaffIdList(staffIds);
        staffQueryDTO.setStatus(0);
        staffQueryDTO.setIsDelete(0);
        ServerResponseEntity<List<StaffVO>> staffResponse=staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
        if(staffResponse.isFail()){
            log.warn("根据客户筛选对应执行员工失败");
            return ApiResponse.fail("任务执行失败，根据客户筛选对应执行员工失败");
        }
        if(CollUtil.isEmpty(staffResponse.getData())){
            log.warn("根据客户未筛选对应执行员工");
            return ApiResponse.fail("任务执行失败，根据客户未筛选对应执行员工");
        }
        Map<Long,StaffVO> staffMaps=LambdaUtils.toMap(staffResponse.getData(),StaffVO::getId);
        Map<Long,List<UserStaffCpRelation>> staffRelationMaps=LambdaUtils.groupList(userStaffCpRelations,UserStaffCpRelation::getStaffId);

        //需要发送的数据
        List<StaffBatchSendCpMsg> staffBatchSendCpMsgs = new ArrayList<>();
        List<GroupSonTaskSendRecord> groupSonTaskSendRecordList = new ArrayList<>();
        // 发送员工信息
        Map<String,GroupPushTaskStaffRelation> pushTaskStaffRelationHashMap = new HashMap<>();

        /**
         * TODO 需要校验员工关联任务重复、校验客户关联任务重复
         */
        for (GroupPushSonTask sonTask : groupPushSonTasks) {
            // 修改发送开始时间
            sonTask.setStartTime(new Date(request.getSendTime()));
            groupPushSonTaskMapper.updateById(sonTask);
            //根据员工组合数据
            for (Map.Entry<Long, List<UserStaffCpRelation>> entry : staffRelationMaps.entrySet()) {
                Long staffId = entry.getKey(); // 获取key值
                StaffVO staffVO=staffMaps.get(staffId);
                if(Objects.isNull(staffVO)){
                    log.info("匹配员工数据-未匹配到：id->{}",staffId);
                    continue;
                }
                List<UserStaffCpRelation> relations = entry.getValue(); // 获取value值
                String staffTaskKey=sonTask.getGroupPushTaskId()+""+staffId;
                if(!pushTaskStaffRelationHashMap.containsKey(staffTaskKey)){
                    GroupPushTaskStaffRelation taskStaffRelation=new GroupPushTaskStaffRelation();
                    taskStaffRelation.setStaffCpUserId(staffVO.getQiWeiUserId());
                    taskStaffRelation.setStaffId(staffId);
                    taskStaffRelation.setStaffName(staffVO.getStaffName());
                    taskStaffRelation.setGroupPushTaskId(sonTask.getGroupPushTaskId());
                    pushTaskStaffRelationHashMap.put(staffTaskKey,taskStaffRelation);
                }

                StaffBatchSendCpMsg staffBatchSendCpMsg = new StaffBatchSendCpMsg(sonTask.getGroupPushTaskId(),
                        sonTask.getGroupPushSonTaskId(), task.getTaskMode(), staffVO.getId(),
                        staffVO.getStaffName(), 0,staffVO.getQiWeiUserId() ,0 ,0 , userIds.size() );
                staffBatchSendCpMsgs.add(staffBatchSendCpMsg);

                for (UserStaffCpRelation u : relations) {
                    GroupSonTaskSendRecord sendRecord = new GroupSonTaskSendRecord(sonTask.getGroupPushSonTaskId(),
                            sonTask.getGroupPushTaskId(), task.getTaskMode(), task.getTaskType(),
                            sonTask.getSonTaskName(), u.getStaffId(), staffBatchSendCpMsg.getStaffName(),
                            u.getQiWeiStaffId(),
                            0, 2, u.getQiWeiUserId(), u.getQiWeiNickName(), u.getCpRemark(),
                            StringUtils.isBlank(u.getCpRemarkMobiles()) ? "[]" : u.getCpRemarkMobiles(),
                            u.getUserUnionId());
                    groupSonTaskSendRecordList.add(sendRecord);
                }
            }
        }

        if(CollUtil.isEmpty(groupSonTaskSendRecordList)){
            log.warn("根据客户未匹配到任务执行员工");
            return ApiResponse.fail("任务执行失败，根据客户未匹配到对应任务执行的员工");
        }
        if(CollUtil.isEmpty(staffBatchSendCpMsgs)){
            log.warn("根据客户未匹配到任务执行员工");
            return ApiResponse.fail("任务执行失败，根据任务未匹配到对应的执行员工");
        }
        if (!CollectionUtil.isEmpty(staffBatchSendCpMsgs)) {
            staffBatchSendCpMsgService.saveBatch(staffBatchSendCpMsgs);
            groupSonTaskSendRecordService.saveBatch(groupSonTaskSendRecordList);

            //保存任务执行员工信息
            if(CollUtil.isNotEmpty(pushTaskStaffRelationHashMap)){
                groupPushTaskStaffRelationMapper.insertBatch(new ArrayList<>(pushTaskStaffRelationHashMap.values()));
            }
            task.setExecuteStatus(1);
            task.setUpdater("执行CDP任务更新执行状态");
            task.setUpdateTime(new Date());
            this.updateById(task);
        }
        log.info("crmSendTask处理结束");
        return ApiResponse.success();
    }

    @Override
    //@Transactional(rollbackFor = Exception.class)
    public ApiResponse crmSendTask(SendTaskDTO request) {
        log.info("crmSendTask入参 = {}", request);
        Long taskId = request.getTaskId();
        GroupPushTask task = this.getById(taskId);
        if (task == null || task.getDeleteFlag() == 1 || task.getTaskType() != 1 || task.getTaskMode() != 0) {
            return ApiResponse.fail("任务不存在或已删除");
        }
        if(Objects.nonNull(task.getExecuteStatus()) && task.getExecuteStatus()>0){
            return ApiResponse.fail("任务执行中");
        }
        //校验主任务是否正常
        if(task.getOperateStatus()!= GroupPushTaskOperateStatusEnum.SUCCESS.getOperateStatus()){
            log.info("任务执行失败，主任务未启用：{}", JSON.toJSONString(task));
            return ApiResponse.fail("任务执行失败，主任务未启用");
        }
        // 子任务
        List<GroupPushSonTask> groupPushSonTasks = groupPushSonTaskMapper
            .selectList(new LambdaUpdateWrapper<GroupPushSonTask>().eq(GroupPushSonTask::getGroupPushTaskId, taskId));
        if (CollectionUtil.isEmpty(groupPushSonTasks)) {
            log.warn("未找到字任务数据");
            return ApiResponse.fail("任务执行失败，未找到字任务数据");
        }
        // 查询对应的staff
        List<GroupPushTaskStaffRelation> staffRelations = groupPushTaskStaffRelationMapper
            .selectList(new LambdaUpdateWrapper<GroupPushTaskStaffRelation>()
                .eq(GroupPushTaskStaffRelation::getGroupPushTaskId, taskId));
        // 发送对象
        List<String> userIds = request.getUserIds().stream().distinct().collect(Collectors.toList());
        // 查询staff对应的好友
        List<UserStaffCpRelation> userStaffCpRelations = userStaffCpRelationManager.getByQiWeiUserIds(userIds);
        if (CollectionUtil.isEmpty(userStaffCpRelations)) {
            userStaffCpRelations=userStaffCpRelationManager.getByQiWeiUserUnionIds(userIds);
        }
        if (CollectionUtil.isEmpty(userStaffCpRelations)) {
            log.warn("未找到对应好友记录");
            return ApiResponse.fail("任务执行失败，未找到对应好友记录");
        }
        Map<String, List<UserStaffCpRelation>> staffUserUserIdMap = userStaffCpRelations.stream().filter(v -> v.getStatus() == 1)
            .collect(Collectors.groupingBy(UserStaffCpRelation::getQiWeiUserId));
        Map<String, List<UserStaffCpRelation>> staffUserUnionIdMap = userStaffCpRelations.stream().filter(v -> v.getStatus() == 1)
                .collect(Collectors.groupingBy(UserStaffCpRelation::getUserUnionId));

//        Map<String,UserStaffCpRelation> staffUserMapByUserId= LambdaUtils.toMap(userStaffCpRelations,UserStaffCpRelation::getQiWeiUserId);
//        Map<String,UserStaffCpRelation> staffUserMapByUnionId= LambdaUtils.toMap(userStaffCpRelations,UserStaffCpRelation::getUserUnionId);

        // 塞入发送数据
        List<StaffBatchSendCpMsg> staffBatchSendCpMsgs = new ArrayList<>();
        List<GroupSonTaskSendRecord> groupSonTaskSendRecordList = new ArrayList<>();

        for (GroupPushSonTask sonTask : groupPushSonTasks) {
            // 修改发送开始时间
            sonTask.setStartTime(new Date(request.getSendTime()));
            groupPushSonTaskMapper.updateById(sonTask);
            for (GroupPushTaskStaffRelation staffRelation : staffRelations) {
                StaffBatchSendCpMsg staffBatchSendCpMsg = new StaffBatchSendCpMsg(sonTask.getGroupPushTaskId(),
                    sonTask.getGroupPushSonTaskId(), task.getTaskMode(), staffRelation.getStaffId(),
                    staffRelation.getStaffName(), 0,staffRelation.getStaffCpUserId() ,0 ,0 , userIds.size() );
                staffBatchSendCpMsgs.add(staffBatchSendCpMsg);

                for (String qiWeiUserId : userIds) {
                    List<UserStaffCpRelation> users = staffUserUserIdMap.get(qiWeiUserId);
                    if (CollectionUtils.isEmpty(users)) {
                        users = staffUserUnionIdMap.get(qiWeiUserId);
                    }
                    if (CollectionUtils.isEmpty(users)) {
                        log.info("用户没有好友关系1 {}", qiWeiUserId);
                        continue;
                    }
                    for (UserStaffCpRelation u : users) {
                        // 匹配对应的销售
                        if (u.getStaffId().equals(staffRelation.getStaffId())) {
                            GroupSonTaskSendRecord sendRecord = new GroupSonTaskSendRecord(sonTask.getGroupPushSonTaskId(),
                                    sonTask.getGroupPushTaskId(), task.getTaskMode(), task.getTaskType(),
                                    sonTask.getSonTaskName(), u.getStaffId(), staffBatchSendCpMsg.getStaffName(),
                                    u.getQiWeiStaffId(),
                                    0, 2, u.getQiWeiUserId(), u.getQiWeiNickName(), u.getCpRemark(),
                                    StringUtils.isBlank(u.getCpRemarkMobiles()) ? "[]" : u.getCpRemarkMobiles(),
                                    u.getUserUnionId());
                            groupSonTaskSendRecordList.add(sendRecord);
                            break;
                        }
                    }
                }
            }
        }
        if(CollUtil.isEmpty(groupSonTaskSendRecordList)){
            log.warn("根据客户未匹配到任务执行员工");
            return ApiResponse.fail("任务执行失败，根据客户未匹配到对应任务执行的员工");
        }
        if(CollUtil.isEmpty(staffBatchSendCpMsgs)){
            log.warn("根据客户未匹配到任务执行员工");
            return ApiResponse.fail("任务执行失败，根据任务未匹配到对应的执行员工");
        }
        if (!CollectionUtil.isEmpty(staffBatchSendCpMsgs)) {
            staffBatchSendCpMsgService.saveBatch(staffBatchSendCpMsgs);
            groupSonTaskSendRecordService.saveBatch(groupSonTaskSendRecordList);

            task.setExecuteStatus(1);
            task.setUpdater("执行CDP任务更新执行状态");
            task.setUpdateTime(new Date());
            this.updateById(task);
        }
        log.info("crmSendTask处理结束");
        return ApiResponse.success();
    }

    /**
     * 导出数据的统计
     */
    @Override
    public ExportGroupPushTaskStatisticsBO getExportStatistic(Long pushTaskId, Long sonTaskId) {
        //获取任务相关统计数据
        GroupPushTaskStatisticVO statistic = this.getSonTaskStatistic(pushTaskId, sonTaskId);

        ExportGroupPushTaskStatisticsBO statisticsBO = new ExportGroupPushTaskStatisticsBO();

        BeanUtils.copyProperties(statistic, statisticsBO);

        statisticsBO.setPushRateStr(statisticsBO.getPushRate() + "%");

        //查询推送任务中已被添加好友关系的用户
        Integer addFriendCount = groupPushTaskVipRelationManager.getTheAddFriendCountByPushTaskId(pushTaskId);

        //推送任务绑定的全部用户数量
        int issueCount = statistic.getNotPushCount() + statistic.getPushIssueFinishCount();

        //总绑定用户数-已加好友数=未加好友数
        statisticsBO.setNotAddFriend(issueCount - addFriendCount);
        statisticsBO.setIssueCount(issueCount);

        return statisticsBO;

    }

    /**
     * 获取推送统计数据
     */
    @Override
    public GroupPushTaskStatisticVO getSonTaskStatistic(Long pushTaskId, Long sonTaskId) {
        //查询所有需要推送的人数
        List<StaffBatchSendCpMsg> staffBatchSendCpMsgs = staffBatchSendCpMsgService.list(
            new LambdaQueryWrapper<StaffBatchSendCpMsg>()
                .eq(StaffBatchSendCpMsg::getPushSonTaskId, sonTaskId));

        int issueCount = staffBatchSendCpMsgs.stream().mapToInt(StaffBatchSendCpMsg::getHeadCount).sum();

        //查询目前已经推送成功的人数
        Integer pushFinishCount = staffBatchSendCpMsgs.stream().mapToInt(StaffBatchSendCpMsg::getReachCount).sum();

        //还未发送的个数
        int notPushCount = issueCount - pushFinishCount;

        //Integer staffCount = groupPushTaskStaffRelationManager.getTheStaffCountByTaskId(pushTaskId);

        //完成率
        BigDecimal rate = BigDecimal.ZERO;
        if (!pushFinishCount.equals(rate.intValue())) {
            rate = new BigDecimal(pushFinishCount)
                .divide(new BigDecimal(issueCount), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
        }

        //声明一个统计数据视图类并设置相关参数
        GroupPushTaskStatisticVO groupPushTaskStatisticVO = new GroupPushTaskStatisticVO();

        groupPushTaskStatisticVO.setPushRate(rate);
        groupPushTaskStatisticVO.setNotPushCount(notPushCount);
        groupPushTaskStatisticVO.setPushIssueFinishCount(pushFinishCount);
        //groupPushTaskStatisticVO.setStaffCount(staffCount);
        groupPushTaskStatisticVO.setIssueCount(issueCount);

        return groupPushTaskStatisticVO;
    }

    /**
     * 上传素材并组装群发素材参数
     */
    private List<Attachment> getAttachmentList(List<MediaJsonVO> mediaJsonVOList,Long staffId, boolean urlFlag) {

        List<Attachment> attachments = new ArrayList<>();

        for (MediaJsonVO mediaJsonVO : mediaJsonVOList) {

            Attachment attachment;
            if(mediaJsonVO.getType().equals("material")){
                //素材库内容
                Long materialId=mediaJsonVO.getMaterialId();
                attachment=getMaterialAttachment(materialId,staffId);
            } else if (!mediaJsonVO.getType().equals("link")) {

                UploadUrlMediaDTO uploadUrlMediaDTO = new UploadUrlMediaDTO();
                uploadUrlMediaDTO.setUrlFlag(urlFlag);

                if (mediaJsonVO.getType().equals("miniprogram")) {
                    uploadUrlMediaDTO.setMediaType("image");
                    uploadUrlMediaDTO.setMediaUrl(mediaJsonVO.getAppPic());
                }else{
                    uploadUrlMediaDTO.setMediaType(mediaJsonVO.getType());
                    uploadUrlMediaDTO.setMediaUrl(mediaJsonVO.getUrl());
                }

                WeixinUploadMediaResultVO weixinUploadMediaResultVO;

                //上传素材
                if (StringUtils.isNotEmpty(mediaJsonVO.getAppPage()) && mediaJsonVO.getType().equals("image")) {

                    //需要创建触点及导购信息的小程序页面url
                    this.getTheBusinessPageUrl(mediaJsonVO);

                    uploadUrlMediaDTO.setPage(mediaJsonVO.getAppPage());
                    uploadUrlMediaDTO.setRemark(uploadUrlMediaProperties.getRemark());
                    //                    uploadUrlMediaDTO.setRemark(mediaJsonVO.getRemark);
                    weixinUploadMediaResultVO = groupPushTaskManager.uploadEventCodeFile(uploadUrlMediaDTO);
                } else {
                    weixinUploadMediaResultVO = groupPushTaskManager.uploadByUrlFile(uploadUrlMediaDTO);
                }

                attachment = this.wrapperAttachments(mediaJsonVO, weixinUploadMediaResultVO);
            } else {
                attachment = this.wrapperAttachments(mediaJsonVO, null);
            }
            attachments.add(attachment);
        }

        return attachments;
    }

    @Override
    public Attachment getMaterialAttachment(Long materialId,Long staffId){
        ServerResponseEntity<MsgAttachment> responseEntity=materialFeignClient.useAndFindAttachmentById(materialId,staffId);
        ServerResponseEntity.checkResponse(responseEntity);
        Attachment attachment=responseEntity.getData();
        if(Objects.nonNull(attachment.getImage())){ //图像
            attachment.setMsgType(WxCpConsts.WelcomeMsgType.IMAGE);
        }else if(Objects.nonNull(attachment.getVideo())){//视频
            attachment.setMsgType(WxCpConsts.WelcomeMsgType.VIDEO);
        }else if(Objects.nonNull(attachment.getMiniProgram())){//小程序
            attachment.setMsgType(WxCpConsts.WelcomeMsgType.MINIPROGRAM);
        }else if(Objects.nonNull(attachment.getLink())){//h5
            attachment.setMsgType(WxCpConsts.WelcomeMsgType.LINK);
        }else if(Objects.nonNull(attachment.getFile())){//文件
            attachment.setMsgType(WxCpConsts.WelcomeMsgType.FILE);
        }
        log.info("materialId:{} staffId:{} 引用素材库内容：{}",materialId,staffId,JSON.toJSONString(attachment));
        return attachment;
    }

    private Attachment wrapperAttachments(MediaJsonVO mediaJsonVO, WeixinUploadMediaResultVO weixinUploadMediaResultVO) {
        log.info("wrapperAttachments---->mediaJsonVO:{}", JSON.toJSONString(mediaJsonVO));
        Attachment attachment = new Attachment();
        attachment.setMsgType(mediaJsonVO.getType());
        //        attachment.setSonTaskMediaId(mediaJsonVO.getSonTaskMediaId());
        if (mediaJsonVO.getType().equals("image")) {
            Image image = new Image();
            image.setMediaId(weixinUploadMediaResultVO.getMediaId());
            image.setPicUrl(weixinUploadMediaResultVO.getMediaUrl());

            attachment.setImage(image);

            return attachment;
        }

        if (mediaJsonVO.getType().equals("link")) {
            Link link = new Link();
            link.setPicUrl(mediaJsonVO.getAppPic());
            link.setDesc(mediaJsonVO.getContent());
            link.setTitle(mediaJsonVO.getAppTitle());
            link.setUrl(mediaJsonVO.getAppPage());

            attachment.setLink(link);
            return attachment;
        }

        if (mediaJsonVO.getType().equals("video")) {
            Video video = new Video();
            video.setMediaId(weixinUploadMediaResultVO.getMediaId());

            attachment.setVideo(video);
            return attachment;
        }

        if (mediaJsonVO.getType().equals("miniprogram")) {
            MiniProgram miniprogram = new MiniProgram();
            miniprogram.setTitle(mediaJsonVO.getAppTitle());
            miniprogram.setAppid(mediaJsonVO.getAppId());
            if (Objects.isNull(mediaJsonVO.getOutSide())) {
                mediaJsonVO.setOutSide(false);
            }
            if (mediaJsonVO.getOutSide()) {
                this.getTheBusinessPageUrl(mediaJsonVO);
            }
            miniprogram.setPage(mediaJsonVO.getAppPage());

//            miniprogram.setPicMediaId(mediaJsonVO.getAppPic());
            miniprogram.setPicMediaId(weixinUploadMediaResultVO.getMediaId());

            attachment.setMiniProgram(miniprogram);
            return attachment;
        }

        return null;
    }


    BatchWrapperTaskStaffAndVipBO wrapperAddGroupPushTaskVipRelationBO(GroupPushTask groupPushTask,
        List<MarkingUserPageVO> markingUserPageVOList,
        Map<Long, UserStaffCpRelationListVO> thisCpMap, Map<Long, StaffVO> thisManagerMap,
        Map<Long, String> currentStaff) {

        BatchWrapperTaskStaffAndVipBO batchWrapperTaskStaffAndVipBO = new BatchWrapperTaskStaffAndVipBO();

        List<AddGroupPushTaskVipRelationBO> relationBOList;

        //声明一个导购和任务关系BO集合
        Set<AddGroupPushTaskStaffRelationBO> staffRelationBOSet = new HashSet<>();
        Map<Long, AddGroupPushTaskStaffRelationBO> taskStaffMap = new HashMap<>();

        relationBOList = markingUserPageVOList.stream()
            .map(user -> {
                String staffCpUserId;

                UserStaffCpRelationListVO cpVO = null;

                Integer friendState = StaffCpFriendStateEnum.NO.getFriendState();

                if (StringUtils.isNotEmpty(user.getStaffCpUserId())) {
                    friendState = StaffCpFriendStateEnum.YES.getFriendState();
                }

                //组装导购和任务关系列表
                AddGroupPushTaskStaffRelationBO staffRelationBO = new AddGroupPushTaskStaffRelationBO();
                staffRelationBO.setGroupPushTaskId(groupPushTask.getGroupPushTaskId());
                //如果导购ID不为空，证明用户已绑定导购
                if (Objects.nonNull(user.getStaffId())) {
                    staffRelationBO.setStaffId(user.getStaffId());
                    if (MapUtil.isNotEmpty(currentStaff) && StringUtils
                        .isNotEmpty(staffCpUserId = currentStaff.get(user.getStaffId()))) {
                        staffRelationBO.setStaffCpUserId(staffCpUserId);
                    }

                } else {
                    //如果用户没有绑定导购，但添加了导购好友，则将其任务关系分配给相关导购
                    if (MapUtil.isNotEmpty(thisCpMap) && Objects.nonNull(cpVO = thisCpMap.get(user.getVipUserId()))) {
                        staffRelationBO.setStaffId(cpVO.getStaffId());
                        staffRelationBO.setStaffCpUserId(cpVO.getQiWeiStaffId());
                        friendState = StaffCpFriendStateEnum.YES.getFriendState();
                    }
                    //如果没有添加过任何导购，则将门店店长设置为本次任务的负责导购
                    if (Objects.isNull(cpVO)) {
                        StaffVO manager;
                        if (MapUtil.isNotEmpty(thisManagerMap) && Objects
                            .nonNull(manager = thisManagerMap.get(user.getServiceStoreId()))) {
                            staffRelationBO.setStaffId(manager.getId());
                            staffRelationBO.setStaffCpUserId(manager.getQiWeiUserId());
                        }
                    }

                }

                taskStaffMap.put(user.getStaffId(), staffRelationBO);
                //                    staffRelationBOSet.add(staffRelationBO);

                AddGroupPushTaskVipRelationBO relationBO = new AddGroupPushTaskVipRelationBO();
                relationBO.setGroupPushTaskId(groupPushTask.getGroupPushTaskId());
                relationBO.setCreateUserId(groupPushTask.getCreateUserId());
                relationBO.setVipCode(user.getVipCode());
                relationBO.setVipUserId(user.getVipUserId());
                relationBO.setStaffStoreId(user.getServiceStoreId());
                relationBO.setStaffId(staffRelationBO.getStaffId());
                relationBO.setVipCpUserId(user.getVipCpUserId());
                relationBO.setStaffCpUserId(staffRelationBO.getStaffCpUserId());

                if (StringUtils.isNotEmpty(user.getVipCpUserId())) {
                    relationBO.setVipCpUserId(user.getVipCpUserId());
                }
                relationBO.setFriendState(friendState);
                return relationBO;
            }).collect(Collectors.toList());

        batchWrapperTaskStaffAndVipBO.setTaskStaffMap(taskStaffMap);
        batchWrapperTaskStaffAndVipBO.setRelationBOList(relationBOList);
        return batchWrapperTaskStaffAndVipBO;
    }

    //    public static void main(String[] args) {
    ////        String y = "FAIL";
    //
    //        String y = "CREATE";
    //
    //        System.out.println(!y.equals(FAIL));
    //    }


    private void getTheBusinessPageUrl(MediaJsonVO mediaJsonVO) {

        Long staffId = AuthUserContext.get().getUserId();
        //        Long staffId = 1L;
        MiniProgram miniprogram = new MiniProgram();
        miniprogram.setTitle(mediaJsonVO.getAppTitle());
        miniprogram.setAppid(mediaJsonVO.getAppId());

        String checkPage = mediaJsonVO.getAppPage();
        if (checkPage.indexOf("/") == 0) {
            checkPage = checkPage.substring(1);
        }
        miniprogram.setPage(checkPage);

        Attachment miniprogramAttachment = new Attachment();
        miniprogramAttachment.setMiniProgram(miniprogram);

        StaffVO staff = groupPushTaskManager.getStaffById(staffId);

        String page = groupPushTaskManager.defineStaffMiniProgram(miniprogramAttachment, staff.getId(), staff.getStoreId());
        mediaJsonVO.setAppPage(page);

    }

}
