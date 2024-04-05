package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.ExtendWxCpMsgTemplateDTO;
import com.mall4j.cloud.api.biz.feign.CpCustGroupClient;
import com.mall4j.cloud.api.biz.feign.WxCpGroupPushTaskClient;
import com.mall4j.cloud.api.biz.vo.*;
import com.mall4j.cloud.api.biz.vo.WxCpGroupMsgTaskVO.ExternalContactGroupMsgTaskInfo;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationSearchDTO;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.common.cache.constant.UserCacheNames;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.mall4j.cloud.user.constant.*;
import com.mall4j.cloud.api.user.dto.StaffSaveSonTaskSendRecordDTO;
import com.mall4j.cloud.api.user.vo.TagVO;
import com.mall4j.cloud.user.dto.QueryChuDaRenQunDetailDTO;
import com.mall4j.cloud.user.manager.GroupPushTaskManager;
import com.mall4j.cloud.user.mapper.CrmUserTagRelationMapper;
import com.mall4j.cloud.user.mapper.UserStaffCpRelationMapper;
import com.mall4j.cloud.user.model.*;
import com.mall4j.cloud.user.service.GroupPushTaskService;
import com.mall4j.cloud.user.service.GroupSonTaskSendRecordService;
import com.mall4j.cloud.user.service.StaffBatchSendCpMsgService;
import com.mall4j.cloud.user.service.UserService;
import com.mall4j.cloud.user.service.UserStaffCpRelationService;
import com.mall4j.cloud.user.vo.MediaJsonVO;
import com.mall4j.cloud.user.vo.TaskSonItemVO;
import com.mall4j.cloud.user.vo.TaskSonMediaVO;
import com.mall4j.cloud.user.vo.TaskSonUserVO;
import com.mall4j.cloud.user.vo.TaskSonVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.AddGroupSonTaskSendRecordBO;
import com.mall4j.cloud.user.bo.AddStaffBatchSendCpMsgBO;
import com.mall4j.cloud.user.dto.StaffBatchSendCpMsgSaveDTO;
import com.mall4j.cloud.user.manager.GroupPushTaskVipRelationManager;
import com.mall4j.cloud.user.manager.GroupSonTaskSendRecordManager;
import com.mall4j.cloud.user.manager.GroupPushSonTaskManager;
import com.mall4j.cloud.user.manager.StaffBatchSendCpMsgManager;
import com.mall4j.cloud.user.mapper.GroupPushSonTaskMapper;
import com.mall4j.cloud.user.service.GroupPushSonTaskMediaService;
import com.mall4j.cloud.user.service.GroupPushSonTaskService;
import com.mall4j.cloud.user.service.TagService;
import com.mall4j.cloud.user.vo.GroupPushSonTaskVO;
import com.mall4j.cloud.api.user.vo.GroupPushTaskVipRelationVO;
import com.mall4j.cloud.user.vo.GroupSonTaskSendRecordVO;
import com.mall4j.cloud.user.vo.StaffBatchSendCpMsgVO;
import io.swagger.annotations.ApiModelProperty;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.external.WxCpMsgTemplateAddResult;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 群发任务子任务表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Slf4j
@Service
public class GroupPushSonTaskServiceImpl extends ServiceImpl<GroupPushSonTaskMapper, GroupPushSonTask> implements GroupPushSonTaskService {

    @Resource
    private GroupPushSonTaskMapper groupPushSonTaskMapper;
    @Resource
    private GroupPushTaskManager groupPushTaskManager;
    @Resource
    private GroupPushTaskService groupPushTaskService;
    @Resource
    private StaffBatchSendCpMsgManager staffBatchSendCpMsgManager;
    @Autowired
    private GroupSonTaskSendRecordService groupSonTaskSendRecordService;
    @Autowired
    private GroupPushSonTaskMediaService groupPushSonTaskMediaService;
    @Autowired
    private GroupPushSonTaskManager groupPushSonTaskManager;
    @Autowired
    private StaffBatchSendCpMsgService staffBatchSendCpMsgService;
    @Autowired
    private GroupPushTaskVipRelationManager groupPushTaskVipRelationManager;
    @Autowired
    private WxCpGroupPushTaskClient wxCpGroupPushTaskClient;
    @Resource
    private CrmUserTagRelationMapper crmUserTagRelationMapper;
    @Autowired
    private UserStaffCpRelationMapper userStaffCpRelationMapper;
    @Autowired
    private CpCustGroupClient cpCustGroupClient;
    @Autowired
    private UserStaffCpRelationService userStaffCpRelationService;

    /**
     * 导购端获取代办任务列表
     * @param staffId 导购ID
     * @param pageDTO 分页数据
     * @return 代办任务列表
     */
    @Override
    public ServerResponseEntity<PageVO<TaskSonVO>> getSonTaskPage(Long staffId, Integer taskMode, PageDTO pageDTO) {
        PageVO<TaskSonVO> taskSonPageVO = PageUtil.doPage(pageDTO, () -> groupPushSonTaskMapper.taskList(staffId, taskMode));
        List<TaskSonVO> list = taskSonPageVO.getList();
        log.info("进入导购端获取代办任务列表方法 当前执行该方法的导购是:{} 分页查询出的数据为 {}", staffId, list);

        if(CollectionUtil.isNotEmpty(list)){
            List<GroupPushSonTaskMedia> groupPushSonTaskMediaList = groupPushSonTaskMediaService
                .list(new LambdaUpdateWrapper<GroupPushSonTaskMedia>()
                    .in(GroupPushSonTaskMedia::getGroupPushSonTaskId,
                        list.stream().map(TaskSonVO::getSonTaskId).collect(Collectors.toList())));
            if (CollectionUtil.isNotEmpty(groupPushSonTaskMediaList)) {
                Map<Long, List<GroupPushSonTaskMedia>> mediaMap = groupPushSonTaskMediaList.stream()
                    .collect(Collectors.groupingBy(GroupPushSonTaskMedia::getGroupPushSonTaskId));

                Map<Long, List<TaskSonMediaVO>> taskSonMediaVoMap = new HashMap<>();
                for (Long sonTaskId : mediaMap.keySet()) {
                    List<GroupPushSonTaskMedia> sonTaskMedia = mediaMap.get(sonTaskId);
                    List<TaskSonMediaVO> taskSonMediaVOList = new ArrayList<>();
                    for (GroupPushSonTaskMedia groupPushSonTaskMedia : sonTaskMedia) {
                        TaskSonMediaVO taskSonMediaVO = new TaskSonMediaVO();
                        taskSonMediaVO.setSonTaskId(groupPushSonTaskMedia.getGroupPushSonTaskId());
                        taskSonMediaVO.setMediaId(groupPushSonTaskMedia.getSonTaskMediaId());
                        taskSonMediaVO.setType(groupPushSonTaskMedia.getType());
                        taskSonMediaVO.setMedia(groupPushSonTaskMedia.getMedia());
                        MediaJsonVO mediaJsonVO = JSONObject.parseObject(taskSonMediaVO.getMedia(), MediaJsonVO.class);
                        if(Objects.nonNull(mediaJsonVO.getMaterialId())){
                            Attachment attachment=groupPushTaskService.getMaterialAttachment(mediaJsonVO.getMaterialId(),staffId);
                            if(Objects.nonNull(attachment)){
                                mediaJsonVO.setMaterialJson(JSON.toJSONString(attachment));
                            }
                        }
                        taskSonMediaVO.setMediaJsonVO(mediaJsonVO);
                        taskSonMediaVOList.add(taskSonMediaVO);
                    }
                    taskSonMediaVoMap.put(sonTaskId, taskSonMediaVOList);
                }
                list.forEach(taskSonVO -> taskSonVO
                    .setSonTaskMedia(taskSonMediaVoMap.get(taskSonVO.getSonTaskId())));
            }
        }
        return ServerResponseEntity.success(taskSonPageVO);
    }

    @Override
    public ServerResponseEntity<TaskSonItemVO> getSonTaskDetailBySonTaskId(Long staffId, Long sonTaskId) {

        TaskSonItemVO taskSonItemVO = groupPushSonTaskMapper.getSonTaskDetailBySonTaskId(sonTaskId);
        log.info("进入导购端获取代办任务列表方法，任务内容为:{}", taskSonItemVO);

        List<StaffBatchSendCpMsg> staffBatchSendCpMsgs = staffBatchSendCpMsgService
                .list(new LambdaUpdateWrapper<StaffBatchSendCpMsg>().in(StaffBatchSendCpMsg::getPushSonTaskId, sonTaskId));

        if(CollUtil.isNotEmpty(staffBatchSendCpMsgs)){
            // 获取该任务应该触达数量
            int headCount = staffBatchSendCpMsgs.stream().mapToInt(StaffBatchSendCpMsg::getHeadCount).sum();
            // 子任务成功发送数量
            int reachCount = staffBatchSendCpMsgs.stream().mapToInt(StaffBatchSendCpMsg::getReachCount).sum();

            //StaffBatchSendCpMsgVO bySonTaskIdAndStaffId = staffBatchSendCpMsgManager.getBySonTaskIdAndStaffId(sonTaskId, staffId);

            //List<GroupSonTaskSendRecordVO> bySonTaskIdAndStaffId = groupSonTaskSendRecordManager.getBySonTaskIdAndStaffId(sonTaskId, staffId);

            if(Objects.nonNull(staffBatchSendCpMsgs.get(0).getMsgId())){
                taskSonItemVO.setIsToGroupPush(GroupPushTaskSendModelEnum.TO_GROUP_PUSH.getValue());
            }
            taskSonItemVO.setHeadCount(BigDecimal.valueOf(headCount));
            taskSonItemVO.setReachCount(BigDecimal.valueOf(reachCount));
            //taskSonItemVO.setInvitedCrowd(tagVO.getTagName());
        }
        taskSonItemVO.setSonTaskMedia(groupPushSonTaskMediaService.sonTaskMediaList(staffId,taskSonItemVO.getSonTaskId()));

        log.info("导购端获取代办任务列表方法结束，当前方法出参为:{}", taskSonItemVO);
        return ServerResponseEntity.success(taskSonItemVO);
    }

    @Override
    public ServerResponseEntity<List<GroupPushSonTaskVO>> getTheSonTaskByPushTaskId(Long pushTaskId) {

        List<GroupPushSonTaskVO> theSonTaskListByTaskId = groupPushSonTaskManager.getTheSonTaskListByTaskId(pushTaskId);

        return ServerResponseEntity.success(theSonTaskListByTaskId);
    }

    /**
     * 新增推送完成记录表数据
     * @param staffSaveSonTaskSendRecordDTO 新增推送完成记录表数据参数
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity staffSaveSonTaskSendRecord(StaffSaveSonTaskSendRecordDTO staffSaveSonTaskSendRecordDTO) {
      /*  log.info("进入群发任务推送小程序-新增推送完成记录表数据方法，当前入参为{}", JSONObject.toJSONString(staffSaveSonTaskSendRecordDTO));

        // 根据传过来的子任务ID查询出子任务信息
        GroupPushSonTaskVO sonTaskVO = groupPushSonTaskManager.getById(staffSaveSonTaskSendRecordDTO.getSonTaskId());

        // 根据任务ID、导购ID、用户ID查询出用户关联任务信息
        List<GroupPushTaskVipRelationVO> vipList = groupPushTaskVipRelationManager
                .getTheVipByTaskIdAndStaffId(sonTaskVO.getGroupPushTaskId(), staffSaveSonTaskSendRecordDTO.getStaffId(), staffSaveSonTaskSendRecordDTO.getUserId());

        if(CollectionUtil.isEmpty(vipList)){
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION, "您与该会员未找到关联信息");
        }
        //获取导购企微ID
        String staffCpUserId = vipList.get(0).getStaffCpUserId();

        // 为避免其他接口直接调用该方法，在最开始先校验导购与触达会员【是否是好友关系】。如果不是就直接返回
        if(StaffCpFriendStateEnum.NO.getFriendState().equals(vipList.get(0).getFriendState())){
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION, "您与该会员还不是好友关系，任务触达失败！");
        }

        StaffBatchSendCpMsgVO sendCpMsgVO = staffBatchSendCpMsgManager.getBySonTaskIdAndStaffId(staffSaveSonTaskSendRecordDTO.getSonTaskId(), staffSaveSonTaskSendRecordDTO.getStaffId());
        log.info("完成记录表数据为：{}", sendCpMsgVO);
        GroupSonTaskSendRecordVO sendRecordVO = groupSonTaskSendRecordManager.getBySonTaskIdAndStaffIdAndUserId(staffSaveSonTaskSendRecordDTO.getSonTaskId(), staffSaveSonTaskSendRecordDTO.getStaffId(), staffSaveSonTaskSendRecordDTO.getUserId());
        log.info("推送记录表数据为：{}", sendRecordVO);

        if(Objects.nonNull(sendRecordVO)){
            // 校验该子任务导购与会员是否已经触达过，如果已经触达过就直接进行返回。不做数据修改更新
            if(!sendRecordVO.getSendStatus().equals(GroupPushTaskSendStatusEnum.SUCCESS.getSendStatus())){
                return ServerResponseEntity.success();
            }
            // 修改 t_group_son_task_send_record 表中 send_status 字段
            groupSonTaskSendRecordManager.updateSendStatusByUserId(sendRecordVO.getPushSonTaskId(), sendRecordVO.getStaffId(), GroupPushTaskSendModelEnum.TO_ONCE_PUSH.value(), GroupPushTaskSendStatusEnum.SUCCESS.getSendStatus(), staffSaveSonTaskSendRecordDTO.getUserId());
        }else {
            // 进行组装批量新增参数
            List<AddGroupSonTaskSendRecordBO> addGroupSonTaskSendRecordBOS = vipList.stream().map(vip -> {
                AddGroupSonTaskSendRecordBO addGroupSonTaskSendRecordBO = new AddGroupSonTaskSendRecordBO();
                addGroupSonTaskSendRecordBO.setPushSonTaskId(sonTaskVO.getGroupPushSonTaskId());
                addGroupSonTaskSendRecordBO.setPushTaskId(sonTaskVO.getGroupPushTaskId());
                addGroupSonTaskSendRecordBO.setSendModel(GroupPushTaskSendModelEnum.TO_ONCE_PUSH.value());
                addGroupSonTaskSendRecordBO.setSendStatus(GroupPushTaskSendStatusEnum.SUCCESS.getSendStatus());
                addGroupSonTaskSendRecordBO.setStaffId(vip.getStaffId());
                addGroupSonTaskSendRecordBO.setVipUserId(vip.getVipUserId());
                addGroupSonTaskSendRecordBO.setVipCpUserId(vip.getVipCpUserId());
                addGroupSonTaskSendRecordBO.setFinishTime(LocalDateTime.now());
                return addGroupSonTaskSendRecordBO;
            }).filter(sendRecord -> sendRecord.getVipUserId().equals(staffSaveSonTaskSendRecordDTO.getUserId()))
            .collect(Collectors.toList());
            //新增发送记录关系
            groupSonTaskSendRecordManager.addBatch(addGroupSonTaskSendRecordBOS);
        }

        if(ObjectUtil.isNotEmpty(sendCpMsgVO)){
            StaffBatchSendCpMsgSaveDTO staffBatchSendCpMsgSaveDTO = new StaffBatchSendCpMsgSaveDTO();
            BeanUtils.copyProperties(sendCpMsgVO, staffBatchSendCpMsgSaveDTO);
            staffBatchSendCpMsgSaveDTO.setReachCount(staffBatchSendCpMsgSaveDTO.getReachCount() + 1);
            if(staffBatchSendCpMsgSaveDTO.getHeadCount().equals(staffBatchSendCpMsgSaveDTO.getReachCount())){
                staffBatchSendCpMsgSaveDTO.setFinishState(1);
                staffBatchSendCpMsgSaveDTO.setFinishTime(LocalDateTime.now());
            }
            // 为避免特殊情况，触达人群数已经全部触达后仍然能够调用该接口导致触达数量大于任务应触达数
            if(staffBatchSendCpMsgSaveDTO.getReachCount() > staffBatchSendCpMsgSaveDTO.getHeadCount()){
                // 当触达数量大于任务应触达数直接进行返回
                return ServerResponseEntity.success();
            }
            // 调用方法修改发送记录表信息
            staffBatchSendCpMsgManager.update(staffBatchSendCpMsgSaveDTO);
        }else {
            //不存在，则新增相关记录，并记录完成情况
            Integer headCount = groupPushTaskVipRelationManager.getTheCountByPushTaskId(sonTaskVO.getGroupPushTaskId(), staffSaveSonTaskSendRecordDTO.getStaffId());
            AddStaffBatchSendCpMsgBO addStaffBatchSendCpMsgBO = new AddStaffBatchSendCpMsgBO();
            addStaffBatchSendCpMsgBO.setStaffId(staffSaveSonTaskSendRecordDTO.getStaffId());
            addStaffBatchSendCpMsgBO.setStaffCpUserId(staffCpUserId);
            addStaffBatchSendCpMsgBO.setPushTaskId(sonTaskVO.getGroupPushTaskId());
            addStaffBatchSendCpMsgBO.setPushSonTaskId(sonTaskVO.getGroupPushSonTaskId());
            addStaffBatchSendCpMsgBO.setHeadCount(headCount);
            addStaffBatchSendCpMsgBO.setReachCount(1);
            addStaffBatchSendCpMsgBO.setCreateTime(LocalDateTime.now());
            if(headCount == 1){
                addStaffBatchSendCpMsgBO.setFinishState(1);
                addStaffBatchSendCpMsgBO.setFinishTime(LocalDateTime.now());
            }
            staffBatchSendCpMsgManager.add(addStaffBatchSendCpMsgBO);
        }*/

        return ServerResponseEntity.success();
    }


    /**
     * 根据任务ID和导购ID获取任务相关信息
     * @param taskId 主任务ID
     * @param staffId 导购ID
     * @return
     */
    @Override
    public ServerResponseEntity<List<GroupPushTaskVipRelationVO>> getTheVipListByTaskIdAndStaffId(Long taskId, Long staffId) {
        List<GroupPushTaskVipRelationVO> vipList = groupPushTaskVipRelationManager.getTheVipListByTaskIdAndStaffId(taskId, staffId);
        return ServerResponseEntity.success(vipList);
    }

    /**
     * 修改任务与用户关联表将好友状态改为【未加好友】
     * @param taskId 任务ID
     * @param staffId 导购ID
     * @param userId 用户ID
     * @return
     */
    @Override
    public ServerResponseEntity updateStaffAndUserTaskRelation(Long taskId, Long staffId, Long userId) {
        groupPushTaskVipRelationManager.updateStaffAndUserTaskRelation(taskId, staffId, userId);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity syncGroupMessageSendResult() {
        Integer limit = 1000;
        // 远程调用获取所有未发送的群发任务信息
        List<StaffBatchSendCpMsg> staffBatchSendCpMsgs = staffBatchSendCpMsgService.list(
            new LambdaUpdateWrapper<StaffBatchSendCpMsg>().eq(StaffBatchSendCpMsg::getFinishState, 0)
                .isNotNull(StaffBatchSendCpMsg::getMsgId));
        // 调用企业微信获取群发成员发送任务列表接口
        if(CollectionUtil.isNotEmpty(staffBatchSendCpMsgs)){
            List<GroupSonTaskSendRecord> sendRecordUpdateList = new ArrayList<>();
            for (StaffBatchSendCpMsg staffBatchSendCpMsg: staffBatchSendCpMsgs){
                boolean isFinish = true;
                int reachCount = 0;
                // 完成时间 取最后1条的发送完成时间
                Date finishTime = new Date();
                String taskCursor = "first";
                // 发送记录明细
                List<GroupSonTaskSendRecord> sendRecordList = groupSonTaskSendRecordService
                    .list(new LambdaUpdateWrapper<GroupSonTaskSendRecord>()
                        .eq(GroupSonTaskSendRecord::getPushSonTaskId, staffBatchSendCpMsg.getPushSonTaskId())
                        .eq(GroupSonTaskSendRecord::getStaffId, staffBatchSendCpMsg.getStaffId()));
                if (CollectionUtils.isEmpty(sendRecordList)) {
                    log.info("当前销售发送记录下无发送明细 {}", JSONObject.toJSONString(staffBatchSendCpMsg));
                    continue;
                }
                Map<String, GroupSonTaskSendRecord> sendRecordMap = sendRecordList.stream()
                    .collect(Collectors.toMap(GroupSonTaskSendRecord::getVipCpUserId, sendRecord -> sendRecord));
                // 在循环中查询到群发任务列表，然后根据导购企业微信ID进行匹配。
                // 匹配上的数据再校验当前任务状态是否是已发送，如果是已发送那就将状态同步到成功记录表中发送状态改为已发送
                while (StringUtils.isNotEmpty(taskCursor)){
                    if (("first").equals(taskCursor)){
                        taskCursor = "";
                    }
                    // 调用企业微信获取群发成员发送任务列表接口 getGroupMessageTask
                    WxCpGroupMsgSendResultVO groupMessageTask = wxCpGroupPushTaskClient
                        .getGroupMessageSendResult(staffBatchSendCpMsg.getMsgId(), staffBatchSendCpMsg.getStaffCpUserId(),
                            limit, taskCursor).getData();
                    log.info("调用企业微信获取群发成员发送任务列表接口:{}", JSONObject.toJSONString(groupMessageTask));
                    if(Objects.isNull(groupMessageTask)){
                        break;
                    }
                    taskCursor = groupMessageTask.getNextCursor();
                    // 如果查询结果不为空，那么就判断当前查询的导购在之前查询的Msg数据中是否存在，如果存在的话就判断当前查询结果是否是已发送。
                    // 如果是已发送就将之前查询出来的Msg中发送状态改为已发送
                    List<WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo> taskSelectList = groupMessageTask.getSendList();
                    if (CollectionUtil.isNotEmpty(taskSelectList)){

                        if(staffBatchSendCpMsg.getTaskMode()==0){
                            Map<String,WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo> groupMsgTaskInfoMap=taskSelectList.stream().
                                    collect(Collectors.toMap(WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo::getExternalUserId,
                                            s->s, (v1, v2)->v2));
                            for (GroupSonTaskSendRecord sonTaskSendRecord : sendRecordList) {//删除好友关系
                                if(!groupMsgTaskInfoMap.containsKey(sonTaskSendRecord.getVipCpUserId())){
                                    log.info("调用企业微信获取企业群发成员未匹配到任务客户，更改为操作失败");
                                    sonTaskSendRecord.setSendStatus(2);
                                    sendRecordUpdateList.add(sonTaskSendRecord);
                                }
                            }
                        }

                        for (WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo externalContactGroupMsgTaskInfo : taskSelectList) {
                            String qiWeiUserId = externalContactGroupMsgTaskInfo.getExternalUserId();
                            String chatId = externalContactGroupMsgTaskInfo.getChatId();
                            // 0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
                            Integer status = externalContactGroupMsgTaskInfo.getStatus();
                            if (status == 0) {
                                isFinish = false;
                                continue;
                            }
                            GroupSonTaskSendRecord sendRecord = null;
                            //任务模式：0-客户群发 1-群群发
                            if(staffBatchSendCpMsg.getTaskMode()==0){
                                sendRecord = sendRecordMap.get(qiWeiUserId);
                            }else if(staffBatchSendCpMsg.getTaskMode()==1){
                                sendRecord = sendRecordMap.get(chatId);
                            }
                            if(Objects.isNull(sendRecord)){
                                log.error("微信返回结果中未找到对应的发送记录 {} {}", staffBatchSendCpMsg.getStaffBatchSendCpMsgId(),qiWeiUserId);
                                continue;
                            }
                            if (status == 1) {
                                reachCount++;
                                finishTime = new Date(externalContactGroupMsgTaskInfo.getSendTime() * 1000L);
                                sendRecord.setFinishTime(finishTime);
                            }
                            sendRecord.setSendStatus(status);
                            sendRecordUpdateList.add(sendRecord);
                        }
                    }
                }
                if (isFinish) {
                    //staffBatchSendCpMsg.setSendStatus(1);
                    staffBatchSendCpMsg.setFinishTime(finishTime);
                    staffBatchSendCpMsg.setFinishState(1);
                    staffBatchSendCpMsg.setReachCount(reachCount);
                    log.info("销售发送记录已完成 {}", JSONObject.toJSONString(staffBatchSendCpMsg));
                }
            }
            // 修改销售发送记录 和 发送记录明细
            staffBatchSendCpMsgService.updateBatchById(staffBatchSendCpMsgs);
            if (!CollectionUtils.isEmpty(sendRecordUpdateList)) {
                groupSonTaskSendRecordService.updateBatchById(sendRecordUpdateList);
            }
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<PageVO<TaskSonUserVO>> getSonTaskUserDetailBySonTaskId(Long staffId,
        QueryChuDaRenQunDetailDTO params, PageDTO pageDTO) {
        LambdaUpdateWrapper<GroupSonTaskSendRecord> wrapper = new LambdaUpdateWrapper<GroupSonTaskSendRecord>()
            .eq(GroupSonTaskSendRecord::getStaffId, staffId)
            .eq(GroupSonTaskSendRecord::getPushSonTaskId, params.getSonTaskId());
        if (StringUtils.isNotBlank(params.getSearchKey())) {
            wrapper.and(and -> and.like(GroupSonTaskSendRecord::getQiWeiNickName, params.getSearchKey()).or()
                .like(GroupSonTaskSendRecord::getCpRemarkMobiles, params.getSearchKey()));
        }
        if (!CollectionUtils.isEmpty(params.getTagIdList())) {
            List<String> unionIdByTagId = crmUserTagRelationMapper.listUnionIdByTagId(params.getTagIdList());
            if (!CollectionUtils.isEmpty(params.getTagIdList())) {
                wrapper.in(GroupSonTaskSendRecord::getUnionId, unionIdByTagId);
            }
        }
        if (!CollectionUtils.isEmpty(params.getSendStatus())) {
            wrapper.in(GroupSonTaskSendRecord::getSendStatus, params.getSendStatus());
        }
        Page<GroupSonTaskSendRecord> pageList = groupSonTaskSendRecordService
            .page(new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize()), wrapper);
        List<GroupSonTaskSendRecord> records = pageList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return ServerResponseEntity.success();
        }
        List<String> userIds = records.stream().map(GroupSonTaskSendRecord::getVipCpUserId).collect(Collectors.toList());
        List<UserStaffCpRelation> userList = userStaffCpRelationMapper
            .selectList(new LambdaUpdateWrapper<UserStaffCpRelation>().in(UserStaffCpRelation::getQiWeiUserId, userIds)
                .groupBy(UserStaffCpRelation::getQiWeiUserId));
        Map<String, UserStaffCpRelation> userMap = userList.stream()
            .collect(Collectors.toMap(UserStaffCpRelation::getQiWeiUserId, o -> o));

        List<TaskSonUserVO> sendList = new ArrayList<>();
        for (GroupSonTaskSendRecord record : records) {
            TaskSonUserVO taskSonUserVO = new TaskSonUserVO();
            taskSonUserVO.setSendStatus(record.getSendStatus());
            taskSonUserVO.setTaskFinishRecordId(record.getTaskFinishRecordId());
            taskSonUserVO.setQiWeiUserId(record.getVipCpUserId());
            UserStaffCpRelation userStaffCpRelation = userMap.get(record.getVipCpUserId());
            if (userStaffCpRelation != null) {
                taskSonUserVO.setNickName(userStaffCpRelation.getQiWeiNickName());
                taskSonUserVO.setName(userStaffCpRelation.getCpRemark());
                taskSonUserVO.setPic(userStaffCpRelation.getAvatar());
                taskSonUserVO.setPhone(userStaffCpRelation.getCpRemarkMobiles());
//                if(record.getSendStatus()<=0 && userStaffCpRelation.getStatus()>1){
//                    taskSonUserVO.setSendStatus(2);
//                }
            }


            sendList.add(taskSonUserVO);
        }
        PageVO<TaskSonUserVO> result = new PageVO<>();
        result.setTotal(pageList.getTotal());
        result.setPages((int) pageList.getPages());
        result.setList(sendList);
        return ServerResponseEntity.success(result);
    }

    @Override
    public WaitMatterCountVO getStaffSendTaskCount() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        Date now = new Date();
        WaitMatterCountVO waitMatterCountVO = new WaitMatterCountVO();
        List<GroupPushSonTask> sonTaskList = list(new LambdaQueryWrapper<GroupPushSonTask>().le(GroupPushSonTask::getStartTime, now)
            .ge(GroupPushSonTask::getEndTime, now).eq(GroupPushSonTask::getDeleteFlag, 0));
        log.info("首页待办统计-群发&客户群发统计:{}",sonTaskList.size());
        if (CollectionUtils.isEmpty(sonTaskList)) {
            waitMatterCountVO.setGroupSendCount(0);
            waitMatterCountVO.setCustomSendCount(0);
            return waitMatterCountVO;
        }
        List<StaffBatchSendCpMsg> staffBatchSendCpMsgList = staffBatchSendCpMsgService.list(
            new LambdaQueryWrapper<StaffBatchSendCpMsg>().eq(StaffBatchSendCpMsg::getStaffId, userInfoInTokenBO.getUserId())
                .isNull(StaffBatchSendCpMsg::getMsgId).in(StaffBatchSendCpMsg::getPushSonTaskId,
                sonTaskList.stream().map(GroupPushSonTask::getGroupPushSonTaskId).collect(
                    Collectors.toList())));
        if (CollectionUtils.isEmpty(staffBatchSendCpMsgList)) {
            waitMatterCountVO.setGroupSendCount(0);
            waitMatterCountVO.setCustomSendCount(0);
            return waitMatterCountVO;
        }
        int  customSendCount = 0;
        int groupSendCount = 0;
        for (StaffBatchSendCpMsg staffBatchSendCpMsg : staffBatchSendCpMsgList) {
            if (staffBatchSendCpMsg.getTaskMode() == 0) {
                customSendCount++;
            } else {
                groupSendCount++;
            }
        }
        waitMatterCountVO.setGroupSendCount(groupSendCount);
        waitMatterCountVO.setCustomSendCount(customSendCount);
        return waitMatterCountVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pushTask(StaffBatchSendCpMsg staffBatchSendCpMsg, GroupPushSonTask sonTask, GroupPushTask mainTask) {
        Long pushSonTaskId = staffBatchSendCpMsg.getPushSonTaskId();
        // 客户发送列表
        List<GroupSonTaskSendRecord> sendRecordList = groupSonTaskSendRecordService
            .list(new LambdaUpdateWrapper<GroupSonTaskSendRecord>()
                .eq(GroupSonTaskSendRecord::getPushSonTaskId, pushSonTaskId)
                .eq(GroupSonTaskSendRecord::getStaffId, staffBatchSendCpMsg.getStaffId()));

        if (CollectionUtils.isEmpty(sendRecordList)) {
            log.info("1-执行任务推送任务ID:【{}】 字任务ID:【{}】，根据员工:【{}】未获取到客户/群列表",
                    mainTask.getGroupPushTaskId(),sonTask.getGroupPushSonTaskId(),staffBatchSendCpMsg.getStaffId());
            return;
        }
        Integer headCount=null;
        //设置群发参数
        ExtendWxCpMsgTemplateDTO extendWxCpMsgTemplateDTO = new ExtendWxCpMsgTemplateDTO();
        //获取导购企微ID
        String staffCpUserId = staffBatchSendCpMsg.getStaffCpUserId();
        //获取已加好友的客户的企微ID
        List<String> friendList = sendRecordList.stream()
            .map(GroupSonTaskSendRecord::getVipCpUserId)
            .collect(Collectors.toList());

        if (mainTask.getTaskMode() == 0) {
            //TODO 校验员工与好友关系是否正常
//            UserStaffCpRelationSearchDTO staffCpRelationSearchDTO=new UserStaffCpRelationSearchDTO();
//            staffCpRelationSearchDTO.setQiWeiUserIds(friendList);
//            staffCpRelationSearchDTO.setPageSize(10);
//            staffCpRelationSearchDTO.setPageNum(1);
//            staffCpRelationSearchDTO.setStatus(1);
//            friendList=userStaffCpRelationService.getUserStaffRelBy(staffCpRelationSearchDTO)
//                    .stream().filter(item-> StrUtil.isNotEmpty(item.getQiWeiUserId()))
//                    .map(UserStaffCpRelationListVO::getQiWeiUserId).collect(Collectors.toList());
//            if (CollectionUtils.isEmpty(friendList)) {
//                log.info("2-执行任务推送任务ID:【{}】 字任务ID:【{}】，根据员工:【{}】未获取到客户",
//                        mainTask.getGroupPushTaskId(),sonTask.getGroupPushSonTaskId(),staffBatchSendCpMsg.getStaffId());
//                return;
//            }
//            headCount=friendList.size();
            extendWxCpMsgTemplateDTO.setChat_type("single");
            boolean allowSelect=Objects.nonNull(mainTask.getAllowSelect())&&mainTask.getAllowSelect()==1?true:false;
            extendWxCpMsgTemplateDTO.setAllow_select(allowSelect);//是否允许成员在待发送客户列表中重新进行选择，默认为false，仅支持客户群发场景
            extendWxCpMsgTemplateDTO.setExternal_userid(friendList);
            extendWxCpMsgTemplateDTO.setSender(staffCpUserId);
        } else {
            //TODO 校验员工关联群关系是否正常
//            ServerResponseEntity<List<CustGroupVO>> groupResponse = cpCustGroupClient.getGroupByStaffIdList(Arrays.asList(staffBatchSendCpMsg.getStaffId()));
//            if(groupResponse.isFail()){
//                log.info("2-执行任务推送任务ID:【{}】 字任务ID:【{}】，根据员工:【{}】未获取到客群列表失败：{}",
//                        mainTask.getGroupPushTaskId(),sonTask.getGroupPushSonTaskId(),groupResponse.toString());
//            }
//            friendList=groupResponse.getData().stream().map(CustGroupVO::getId).collect(Collectors.toList());;
//            headCount=friendList.size();
            extendWxCpMsgTemplateDTO.setChat_type("group");
            extendWxCpMsgTemplateDTO.setChat_id_list(friendList);
//            extendWxCpMsgTemplateDTO.setSender(staffCpUserId);
        }
        log.info("发送客户企微id集合 {}", JSONObject.toJSONString(friendList));

        //设置一个缓存临时素材的redisKey
        String redisKey = UserCacheNames.CP_EXTERN_CONTACT_ATTACHMENT_LIST + pushSonTaskId;
        List<Attachment> attachmentList = groupPushTaskService.getAttachments(pushSonTaskId, redisKey, staffBatchSendCpMsg.getStaffId(),false);
        extendWxCpMsgTemplateDTO.setAttachments(attachmentList);
        Text text = new Text();
        //log.info("SON TASK VO IS:{}", JSONObject.toJSONString(sonTaskVO));
        text.setContent(sonTask.getPushContent());
        extendWxCpMsgTemplateDTO.setText(text);

        // 调用企微接口
        WxCpMsgTemplateAddResult wxCpMsgTemplateAddResult;
        try {
            log.info("执行企微发送群发任务入参：{}",extendWxCpMsgTemplateDTO.toJson());
            wxCpMsgTemplateAddResult = groupPushTaskManager
                .addExternalContactMsgTemplate(extendWxCpMsgTemplateDTO.toJson());
            log.info("执行企微发送群发任务结果：{}",wxCpMsgTemplateAddResult.toString());
        } catch (Exception e) {
            log.error("调用企微群发接口报错 params = {}", extendWxCpMsgTemplateDTO.toJson());
            log.error("", e);
            return;
        }


        // 修改发送状态
        //int failCount = 0;
        List<String> failList = wxCpMsgTemplateAddResult.getFailList();
        LambdaUpdateWrapper<GroupSonTaskSendRecord> updateWrapper = new LambdaUpdateWrapper<GroupSonTaskSendRecord>()
            .eq(GroupSonTaskSendRecord::getPushSonTaskId, pushSonTaskId)
            .eq(GroupSonTaskSendRecord::getStaffId, staffBatchSendCpMsg.getStaffId())
            .set(GroupSonTaskSendRecord::getSendStatus, 0);
        groupSonTaskSendRecordService.update(updateWrapper);
        if (!CollectionUtils.isEmpty(failList)) {
            //failCount = failList.size();
            groupSonTaskSendRecordService.update(new LambdaUpdateWrapper<GroupSonTaskSendRecord>()
                .eq(GroupSonTaskSendRecord::getPushSonTaskId, pushSonTaskId)
                .eq(GroupSonTaskSendRecord::getStaffId, staffBatchSendCpMsg.getStaffId())
                .in(GroupSonTaskSendRecord::getVipCpUserId, failList)
                .set(GroupSonTaskSendRecord::getSendStatus, 2));
        }
        staffBatchSendCpMsgManager
            .updateMsgIdById(staffBatchSendCpMsg.getStaffBatchSendCpMsgId(), wxCpMsgTemplateAddResult.getMsgId(),headCount);

        mainTask.setExecuteStatus(2);
        mainTask.setUpdater("定时任务执行发送任务");
        mainTask.setUpdateTime(new Date());
        groupPushTaskService.updateById(mainTask);
    }

    @Override
    public ServerResponseEntity<PageVO<TaskSonGroupVO>> getSonTaskGroupDetailBySonTaskId(Long staffId, Long sonTaskId,String searchKey, PageDTO pageDTO) {
        LambdaUpdateWrapper<GroupSonTaskSendRecord> wrapper = new LambdaUpdateWrapper<GroupSonTaskSendRecord>()
            .eq(GroupSonTaskSendRecord::getStaffId, staffId).eq(GroupSonTaskSendRecord::getPushSonTaskId, sonTaskId);
        if (StringUtils.isNotBlank(searchKey)) {
            wrapper.like(GroupSonTaskSendRecord::getQiWeiNickName, searchKey);
        }

        Page<GroupSonTaskSendRecord> pageList = groupSonTaskSendRecordService
            .page(new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize()),
                wrapper);
        List<GroupSonTaskSendRecord> records = pageList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return ServerResponseEntity.success();
        }
        StaffBatchSendCpMsg one = staffBatchSendCpMsgService.getOne(
            new LambdaUpdateWrapper<StaffBatchSendCpMsg>().eq(StaffBatchSendCpMsg::getStaffId, staffId).last("limit 1"));

        List<String> userIds = records.stream().map(GroupSonTaskSendRecord::getVipCpUserId).collect(Collectors.toList());
        ServerResponseEntity<List<TaskSonGroupVO>> custGroupByIds = cpCustGroupClient.findCustGroupByIds(userIds);
        List<TaskSonGroupVO> groupVOS = custGroupByIds.getData();
        if (CollectionUtils.isEmpty(groupVOS)) {
            return ServerResponseEntity.success();
        }
        Map<String, TaskSonGroupVO> groupMap = groupVOS.stream().collect(Collectors.toMap(TaskSonGroupVO::getGroupId, o -> o));

        List<TaskSonGroupVO> sendList = new ArrayList<>();
        for (GroupSonTaskSendRecord record : records) {
            TaskSonGroupVO taskSonUserVO = groupMap.get(record.getVipCpUserId());
            if (taskSonUserVO != null) {
                taskSonUserVO.setSendStatus(record.getSendStatus());
                taskSonUserVO.setOwnerName(one.getStaffName());
            }
            sendList.add(taskSonUserVO);
        }
        PageVO<TaskSonGroupVO> result = new PageVO<>();
        result.setTotal(pageList.getTotal());
        result.setPages((int)pageList.getPages());
        result.setList(sendList);
        return ServerResponseEntity.success(result);
    }

    /**
     * 导购获取自己群发任务数量
     * @param staffId 导购ID
     * @return
     */
    @Override
    public ServerResponseEntity<Integer> staffGetGroupPushTaskCount(Long staffId) {
        return ServerResponseEntity.success(groupPushSonTaskMapper.staffGetGroupPushTaskCount(staffId));
    }

}
