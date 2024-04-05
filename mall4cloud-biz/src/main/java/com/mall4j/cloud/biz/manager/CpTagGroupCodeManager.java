package com.mall4j.cloud.biz.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.constant.cp.TaskUserSendStatusEnum;
import com.mall4j.cloud.api.biz.feign.WxCpGroupPushTaskClient;
import com.mall4j.cloud.api.biz.vo.WxCpGroupMsgSendResultVO;
import com.mall4j.cloud.api.biz.vo.WxCpGroupMsgTaskVO;
import com.mall4j.cloud.biz.mapper.cp.TaskStaffRefMapper;
import com.mall4j.cloud.biz.model.cp.CpGroupCreateTask;
import com.mall4j.cloud.biz.model.cp.CpTaskStaffRef;
import com.mall4j.cloud.biz.model.cp.CpTaskUserRef;
import com.mall4j.cloud.biz.service.cp.CpTaskUserRefService;
import com.mall4j.cloud.biz.service.cp.GroupCreateTaskService;
import com.mall4j.cloud.biz.service.cp.CpTaskStaffRefService;
import com.mall4j.cloud.biz.vo.cp.CpTaskStaffRefVO;
import com.mall4j.cloud.biz.wx.cp.constant.SendStatus;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 标签建群-员工&客户执行状态更新
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CpTagGroupCodeManager {

    private final GroupCreateTaskService groupCreateTaskService;
    private final CpTaskStaffRefService taskStaffRefService;
    private final CpTaskUserRefService taskUserRefService;
    private final WxCpGroupPushTaskClient wxCpGroupPushTaskClient;
    private final TaskStaffRefMapper taskStaffRefMapper;

    public void refeshStaffAndExternalStatus(){
        /**
         * 状态依据企微接口：https://developer.work.weixin.qq.com/document/path/93338
         * 1.获取未执行完成的任务
         * 2.根据任务msgId获取：获取群发成员发送任务列表  -> https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_groupmsg_task
         * 3.根据任务msgId获取：获取企业群发成员执行结果  -> https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_groupmsg_send_result
         * 4.处理任务邀请人总数
         * 5.处理任务员工发送完成总数
         *
         *
         */
        List<CpGroupCreateTask> createTasks=getTasks();
        if(CollUtil.isEmpty(createTasks)){
            log.info("标签建群-员工&客户执行状态更新失败，无任务数据需要处理");
            return;
        }
        Map<Long,CpGroupCreateTask> createTaskMap=LambdaUtils.toMap(createTasks,CpGroupCreateTask::getId);

        List<CpTaskStaffRefVO> staffRefVOS=getTaskStaff().stream().filter(item->StrUtil.isNotEmpty(item.getMsgId())).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(staffRefVOS)){
            Integer limit = 1000;


            Map<Long,CpGroupCreateTask> updateCreateTaskMap=new HashMap<>();
            /****************************处理群发成员发送任务列表 start*********************************************/
            List<CpTaskStaffRef> updateStaffRef=new ArrayList<>();
            for (CpTaskStaffRefVO staffRefVO : staffRefVOS) {
                String msgId=staffRefVO.getMsgId();
                String taskCursor = "first";
                // 在循环中查询到群发任务列表，然后根据导购企业微信ID进行匹配。
                // 匹配上的数据再校验当前任务状态是否是已发送，如果是已发送那就将状态同步到成功记录表中发送状态改为已发送
                while (StringUtils.isNotEmpty(taskCursor)) {
                    if (("first").equals(taskCursor)) {
                        taskCursor = "";
                    }
                    // 调用企业微信获取群发成员发送任务列表接口 getGroupMessageTask
                    WxCpGroupMsgTaskVO groupMessageTask = wxCpGroupPushTaskClient.getGroupMessageTask(msgId, limit, taskCursor).getData();
                    log.info("调用企业微信获取群发成员发送任务列表接口:{}", JSONObject.toJSONString(groupMessageTask));
                    if(Objects.isNull(groupMessageTask)){
                        break;
                    }
                    taskCursor = groupMessageTask.getNextCursor();
                    // 如果查询结果不为空，那么就判断当前查询的导购在之前查询的Msg数据中是否存在，如果存在的话就判断当前查询结果是否是已发送。
                    // 如果是已发送就将之前查询出来的Msg中发送状态改为已发送
                    if (CollectionUtil.isNotEmpty(groupMessageTask.getTaskList())){
                        //只处理发送成功的：0-未发送 2-已发送
                        List<WxCpGroupMsgTaskVO.ExternalContactGroupMsgTaskInfo> taskList=groupMessageTask.getTaskList().stream().filter(item->item.getStatus()==2).collect(Collectors.toList());
                        if(CollUtil.isEmpty(taskList)){
                            log.info("只处理发送成功的无数据，继续下一个");
                            continue;
                        }
                        Map<String, WxCpGroupMsgTaskVO.ExternalContactGroupMsgTaskInfo> taskMap = taskList.stream()
                                .collect(Collectors.toMap(WxCpGroupMsgTaskVO.ExternalContactGroupMsgTaskInfo::getUserId, task -> task));
                        //0-未发送 2-已发送
                        if(taskMap.containsKey(staffRefVO.getUserId()) && taskMap.get(staffRefVO.getUserId()).getStatus() == 2){
                            CpTaskStaffRef cpTaskStaffRef=new CpTaskStaffRef();
                            cpTaskStaffRef.setId(staffRefVO.getId());
                            cpTaskStaffRef.setUpdateBy("定时任务刷新执行状态");
                            cpTaskStaffRef.setUpdateTime(new Date());
                            cpTaskStaffRef.setStatus(1);
                            updateStaffRef.add(cpTaskStaffRef);

                            //增加任务完成发送员工数
                            if(createTaskMap.containsKey(staffRefVO.getTaskId())){
                                Long taskId=createTaskMap.get(staffRefVO.getTaskId()).getId();
                                CpGroupCreateTask cpGroupCreateTask=updateCreateTaskMap.get(taskId);
                                Integer sendStaffCount=createTaskMap.get(taskId).getSendStaffCount();
                                if(Objects.isNull(sendStaffCount) || sendStaffCount<=0){
                                    sendStaffCount=1;
                                } else{
                                    sendStaffCount=sendStaffCount+1;
                                }
                                createTaskMap.get(taskId).setSendStaffCount(sendStaffCount);
                                if(Objects.isNull(cpGroupCreateTask)){
                                    cpGroupCreateTask=new CpGroupCreateTask();
                                    cpGroupCreateTask.setId(taskId);
                                    cpGroupCreateTask.setSendStaffCount(sendStaffCount);
                                    cpGroupCreateTask.setSendStatus(1);
                                    cpGroupCreateTask.setUpdateBy("定时任务刷新执行状态");
                                    cpGroupCreateTask.setUpdateTime(new Date());
                                    updateCreateTaskMap.put(taskId,cpGroupCreateTask);
                                }else{
                                    updateCreateTaskMap.get(taskId).setSendStaffCount(sendStaffCount);
                                }
                            }
                        }
                    }
                }
            }
            if(CollUtil.isNotEmpty(updateStaffRef)){
                taskStaffRefService.updateBatchById(updateStaffRef);
                updateStaffRef.clear();
            }
            /**************************处理群发成员发送任务列表 end*********************************************/


            /**************************处理企业群发成员执行结果 start*********************************************/
            List<CpTaskUserRef> updateTaskUserRefs=new ArrayList<>();
            for (CpTaskStaffRefVO staffRefVO : staffRefVOS) {
                //获取客户数据
                List<CpTaskUserRef> taskUserRefs=getTaskUser(staffRefVO.getTaskId(),staffRefVO.getStaffId());
                Map<String,CpTaskUserRef> taskUserRefMap=LambdaUtils.toMap(taskUserRefs,CpTaskUserRef::getQiWeiUserId);
                String sendCursor = "first";
                while (StringUtils.isNotEmpty(sendCursor)) {
                    if (("first").equals(sendCursor)) {
                        sendCursor = "";
                    }
                    /**
                     * 1.调用Biz企业微信接口获取群发任务结果
                     * 发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
                     */
                    WxCpGroupMsgSendResultVO groupMessageSendResult = wxCpGroupPushTaskClient.getGroupMessageSendResult(staffRefVO.getMsgId(), staffRefVO.getUserId(), limit, sendCursor).getData();
                    log.info("调用企业微信获取企业群发成员执行结果接口:{}", JSONObject.toJSONString(groupMessageSendResult));
                    if(Objects.isNull(groupMessageSendResult)){
                        break;
                    }
                    //TODO 固定数据仅限调使用
//                    String json="[{\"externalUserId\":\"wmRqXCCgAApPIwgJBoKGE-V9f8rvqA0w\",\"status\":1,\"userId\":\"eury\"},{\"externalUserId\":\"wmRqXCCgAALYQi6VcjFFOh4fExi1MUlg\",\"status\":1,\"userId\":\"eury\"},{\"externalUserId\":\"wmRqXCCgAAEr_HjzEWpiTS6ADR9xNVOQ\",\"status\":1,\"userId\":\"eury\"},{\"externalUserId\":\"wmRqXCCgAAR6A5V5U-JkrHb4J-6iS7rw\",\"status\":3,\"userId\":\"eury\"}]";
//                    List<WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo> sendLis=JSONArray.parseArray(json,WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo.class);
//                    groupMessageSendResult.setSendList(sendLis);
                    sendCursor = groupMessageSendResult.getNextCursor();
                    log.info("调用企业微信获取企业群发成员执行结果接口 getSendList:{}", JSONObject.toJSONString(groupMessageSendResult.getSendList()));
                    if (CollectionUtil.isNotEmpty(groupMessageSendResult.getSendList())){

                        Map<String,WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo> groupMsgTaskInfoMap=groupMessageSendResult.getSendList().stream().
                                collect(Collectors.toMap(WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo::getExternalUserId,
                                        s->s, (v1, v2)->v2));
                        for (CpTaskUserRef taskUserRef : taskUserRefs) {//删除好友关系
                            if(!groupMsgTaskInfoMap.containsKey(taskUserRef.getQiWeiUserId())){
                                log.info("调用企业微信获取企业群发成员未匹配到任务客户，更改为操作失败");
                                taskUserRef.setStatus(1);//执行完成
                                taskUserRef.setReachStatus(0);
                                taskUserRef.setCpSendStatus(2);
                                taskUserRef.setUpdateBy("定时任务刷新执行状态");
                                taskUserRef.setUpdateTime(new Date());
                                updateTaskUserRefs.add(taskUserRef);
                            }
                        }
                        for (WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo sendInfo : groupMessageSendResult.getSendList()) {
                            if(!taskUserRefMap.containsKey(sendInfo.getExternalUserId())){
                                log.info("调用企业微信获取企业群发成员未匹配到任务客户，执行下一个");
                                continue;
                            }
                            CpTaskUserRef cpTaskUserRef=new CpTaskUserRef();
                            cpTaskUserRef.setId(taskUserRefMap.get(sendInfo.getExternalUserId()).getId());
                            if(sendInfo.getStatus()== TaskUserSendStatusEnum.SUCCESS.getValue()){
                                cpTaskUserRef.setStatus(1);//执行完成
                            }
                            if(StrUtil.isNotBlank(sendInfo.getChatId())){
                                cpTaskUserRef.setChatId(sendInfo.getChatId());
                            }
                            cpTaskUserRef.setReachStatus(1);
                            cpTaskUserRef.setCpSendStatus(sendInfo.getStatus());
                            cpTaskUserRef.setQiWeiUserId(sendInfo.getExternalUserId());
                            cpTaskUserRef.setUpdateBy("定时任务刷新执行状态");
                            cpTaskUserRef.setUpdateTime(new Date());
                            if(Objects.nonNull(sendInfo.getSendTime())){
                                cpTaskUserRef.setSendTime(WechatUtils.formatDate(sendInfo.getSendTime().toString()));
                            }
                            updateTaskUserRefs.add(cpTaskUserRef);

                            //员工客户数据统计
                            CpTaskStaffRef cpTaskStaffRef=new CpTaskStaffRef();
                            cpTaskStaffRef.setId(staffRefVO.getId());
                            Integer sendUserCount=staffRefVO.getSendUserCount();
                            if(Objects.isNull(sendUserCount) || sendUserCount<=0){
                                sendUserCount=1;
                            } else{
                                sendUserCount=sendUserCount+1;
                            }
                            staffRefVO.setSendUserCount(sendUserCount);
                            cpTaskStaffRef.setSendUserCount(sendUserCount);//发送客户总数

                            if(sendInfo.getStatus()==TaskUserSendStatusEnum.SUCCESS.getValue()){//发送成功
                                Integer inviteUserCount=staffRefVO.getInviteUserCount();
                                if(Objects.isNull(inviteUserCount) || inviteUserCount<=0){
                                    inviteUserCount=1;
                                } else{
                                    inviteUserCount=inviteUserCount+1;
                                }
                                staffRefVO.setInviteUserCount(inviteUserCount);
                                cpTaskStaffRef.setInviteUserCount(inviteUserCount);//邀请客户总数

                                //增加任务邀请人数
                                if(createTaskMap.containsKey(staffRefVO.getTaskId())){
                                    Long taskId=createTaskMap.get(staffRefVO.getTaskId()).getId();
                                    CpGroupCreateTask cpGroupCreateTask=updateCreateTaskMap.get(taskId);
                                    Integer InviteCount=createTaskMap.get(taskId).getInviteCount();
                                    if(Objects.isNull(InviteCount) || InviteCount<=0) {
                                        InviteCount=1;
                                    }else{
                                        InviteCount=InviteCount+1;
                                    }
                                    createTaskMap.get(taskId).setInviteCount(InviteCount);
                                    if(Objects.isNull(cpGroupCreateTask)){
                                        cpGroupCreateTask=new CpGroupCreateTask();
                                        cpGroupCreateTask.setId(taskId);
                                        cpGroupCreateTask.setInviteCount(InviteCount);
                                        cpGroupCreateTask.setUpdateBy("定时任务刷新执行状态");
                                        cpGroupCreateTask.setUpdateTime(new Date());
                                        updateCreateTaskMap.put(taskId,cpGroupCreateTask);
                                    }else{
                                        updateCreateTaskMap.get(taskId).setInviteCount(InviteCount);
                                    }
                                }
                            }

                            updateStaffRef.add(cpTaskStaffRef);
                        }
                    }

                }
            }
            if(CollUtil.isNotEmpty(updateTaskUserRefs)){
                log.info("updateTaskUserRefs-->{}", JSON.toJSONString(updateTaskUserRefs));
                taskUserRefService.updateBatchById(updateTaskUserRefs);
            }
            if(CollUtil.isNotEmpty(updateCreateTaskMap)){
                List<CpGroupCreateTask> updateCreateTask=new ArrayList<>(updateCreateTaskMap.values());
                log.info("updateCreateTask-->{}", JSON.toJSONString(updateCreateTask));
                groupCreateTaskService.updateBatchById(updateCreateTask);
            }
            if(CollUtil.isNotEmpty(updateStaffRef)){
                taskStaffRefService.updateBatchById(updateStaffRef);
            }

            /**************************处理企业群发成员执行结果 end*********************************************/
        }

    }

//    public void refeshStaffAndExternalStatus(){
//        /**
//         * 状态依据企微接口：https://developer.work.weixin.qq.com/document/path/93338
//         * 1.获取未执行完成的任务
//         * 2.根据任务msgId获取：获取群发成员发送任务列表  -> https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_groupmsg_task
//         * 3.根据任务msgId获取：获取企业群发成员执行结果  -> https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_groupmsg_send_result
//         * 4.处理任务邀请人总数
//         * 5.处理任务员工发送完成总数
//         *
//         *
//         */
//        List<CpGroupCreateTask> createTasks=getTasks();
//        if(CollUtil.isEmpty(createTasks)){
//            log.info("标签建群-员工&客户执行状态更新失败，无任务数据需要处理");
//            return;
//        }
//        Map<String,CpGroupCreateTask> createTaskMap=LambdaUtils.toMap(createTasks,CpGroupCreateTask::getMsgId);
//
//        List<CpTaskStaffRefVO> staffRefVOS=getTaskStaff();
//        if(CollUtil.isNotEmpty(staffRefVOS)){
//            Integer limit = 1000;
//
//
//            Map<Long,CpGroupCreateTask> updateCreateTaskMap=new HashMap<>();
//            /****************************处理群发成员发送任务列表 start*********************************************/
//            List<CpTaskStaffRef> updateStaffRef=new ArrayList<>();
//            Map<String,List<CpTaskStaffRefVO>> staffRefMap= LambdaUtils.groupList(staffRefVOS,CpTaskStaffRefVO::getMsgId);
//            for (Map.Entry<String,List<CpTaskStaffRefVO>> entry : staffRefMap.entrySet()) {
//                String msgId=entry.getKey();
//                String taskCursor = "first";
//                // 在循环中查询到群发任务列表，然后根据导购企业微信ID进行匹配。
//                // 匹配上的数据再校验当前任务状态是否是已发送，如果是已发送那就将状态同步到成功记录表中发送状态改为已发送
//                while (StringUtils.isNotEmpty(taskCursor)) {
//                    if (("first").equals(taskCursor)) {
//                        taskCursor = "";
//                    }
//                    // 调用企业微信获取群发成员发送任务列表接口 getGroupMessageTask
//                    WxCpGroupMsgTaskVO groupMessageTask = wxCpGroupPushTaskClient.getGroupMessageTask(msgId, limit, taskCursor).getData();
//                    log.info("调用企业微信获取群发成员发送任务列表接口:{}", JSONObject.toJSONString(groupMessageTask));
//                    if(Objects.isNull(groupMessageTask)){
//                        break;
//                    }
//                    taskCursor = groupMessageTask.getNextCursor();
//                    // 如果查询结果不为空，那么就判断当前查询的导购在之前查询的Msg数据中是否存在，如果存在的话就判断当前查询结果是否是已发送。
//                    // 如果是已发送就将之前查询出来的Msg中发送状态改为已发送
//                    if (CollectionUtil.isNotEmpty(groupMessageTask.getTaskList())){
//                        //只处理发送成功的：0-未发送 2-已发送
//                        List<WxCpGroupMsgTaskVO.ExternalContactGroupMsgTaskInfo> taskList=groupMessageTask.getTaskList().stream().filter(item->item.getStatus()==2).collect(Collectors.toList());
//                        if(CollUtil.isEmpty(taskList)){
//                            log.info("只处理发送成功的无数据，继续下一个");
//                            continue;
//                        }
//                        Map<String, WxCpGroupMsgTaskVO.ExternalContactGroupMsgTaskInfo> taskMap = taskList.stream()
//                                .collect(Collectors.toMap(WxCpGroupMsgTaskVO.ExternalContactGroupMsgTaskInfo::getUserId, task -> task));
//                        for (CpTaskStaffRefVO staffRefVO : entry.getValue()) {
//                            //0-未发送 2-已发送
//                            if(taskMap.containsKey(staffRefVO.getUserId()) && taskMap.get(staffRefVO.getUserId()).getStatus() == 2){
//                                CpTaskStaffRef cpTaskStaffRef=new CpTaskStaffRef();
//                                cpTaskStaffRef.setId(staffRefVO.getId());
//                                cpTaskStaffRef.setUpdateBy("定时任务刷新执行状态");
//                                cpTaskStaffRef.setUpdateTime(new Date());
//                                cpTaskStaffRef.setStatus(1);
//                                updateStaffRef.add(cpTaskStaffRef);
//
//                                //增加任务完成发送员工数
//                                if(createTaskMap.containsKey(staffRefVO.getMsgId())){
//                                    Long taskId=createTaskMap.get(staffRefVO.getMsgId()).getId();
//                                    CpGroupCreateTask cpGroupCreateTask=updateCreateTaskMap.get(taskId);
//                                    Integer sendStaffCount=createTaskMap.get(staffRefVO.getMsgId()).getSendStaffCount();
//                                    if(Objects.isNull(sendStaffCount) || sendStaffCount<=0){
//                                        sendStaffCount=1;
//                                    } else{
//                                        sendStaffCount=sendStaffCount+1;
//                                    }
//                                    createTaskMap.get(staffRefVO.getMsgId()).setSendStaffCount(sendStaffCount);
//                                    if(Objects.isNull(cpGroupCreateTask)){
//                                        cpGroupCreateTask=new CpGroupCreateTask();
//                                        cpGroupCreateTask.setId(taskId);
//                                        cpGroupCreateTask.setSendStaffCount(sendStaffCount);
//                                        cpGroupCreateTask.setSendStatus(1);
//                                        cpGroupCreateTask.setUpdateBy("定时任务刷新执行状态");
//                                        cpGroupCreateTask.setUpdateTime(new Date());
//                                        updateCreateTaskMap.put(taskId,cpGroupCreateTask);
//                                    }else{
//                                        updateCreateTaskMap.get(taskId).setSendStaffCount(sendStaffCount);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if(CollUtil.isNotEmpty(updateStaffRef)){
//                taskStaffRefService.updateBatchById(updateStaffRef);
//                updateStaffRef.clear();
//            }
//            /**************************处理群发成员发送任务列表 end*********************************************/
//
//
//            /**************************处理企业群发成员执行结果 start*********************************************/
//            List<CpTaskUserRef> updateTaskUserRefs=new ArrayList<>();
//            for (CpTaskStaffRefVO staffRefVO : staffRefVOS) {
//                //获取客户数据
//                List<CpTaskUserRef> taskUserRefs=getTaskUser(staffRefVO.getTaskId(),staffRefVO.getStaffId());
//                Map<String,CpTaskUserRef> taskUserRefMap=LambdaUtils.toMap(taskUserRefs,CpTaskUserRef::getQiWeiUserId);
//                String sendCursor = "first";
//                while (StringUtils.isNotEmpty(sendCursor)) {
//                    if (("first").equals(sendCursor)) {
//                        sendCursor = "";
//                    }
//                    /**
//                     * 1.调用Biz企业微信接口获取群发任务结果
//                     * 发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
//                     */
//                    WxCpGroupMsgSendResultVO groupMessageSendResult = wxCpGroupPushTaskClient.getGroupMessageSendResult(staffRefVO.getMsgId(), staffRefVO.getUserId(), limit, sendCursor).getData();
//                    log.info("调用企业微信获取企业群发成员执行结果接口:{}", JSONObject.toJSONString(groupMessageSendResult));
//                    if(Objects.isNull(groupMessageSendResult)){
//                        break;
//                    }
//                    //TODO 固定数据仅限调使用
////                    String json="[{\"externalUserId\":\"wmRqXCCgAApPIwgJBoKGE-V9f8rvqA0w\",\"status\":1,\"userId\":\"eury\"},{\"externalUserId\":\"wmRqXCCgAALYQi6VcjFFOh4fExi1MUlg\",\"status\":1,\"userId\":\"eury\"},{\"externalUserId\":\"wmRqXCCgAAEr_HjzEWpiTS6ADR9xNVOQ\",\"status\":1,\"userId\":\"eury\"},{\"externalUserId\":\"wmRqXCCgAAR6A5V5U-JkrHb4J-6iS7rw\",\"status\":3,\"userId\":\"eury\"}]";
////                    List<WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo> sendLis=JSONArray.parseArray(json,WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo.class);
////                    groupMessageSendResult.setSendList(sendLis);
//                    sendCursor = groupMessageSendResult.getNextCursor();
//                    log.info("调用企业微信获取企业群发成员执行结果接口 getSendList:{}", JSONObject.toJSONString(groupMessageSendResult.getSendList()));
//                    if (CollectionUtil.isNotEmpty(groupMessageSendResult.getSendList())){
//
//                        Map<String,WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo> groupMsgTaskInfoMap=groupMessageSendResult.getSendList().stream().
//                                collect(Collectors.toMap(WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo::getExternalUserId,
//                                        s->s, (v1, v2)->v2));
//                        for (CpTaskUserRef taskUserRef : taskUserRefs) {//删除好友关系
//                            if(!groupMsgTaskInfoMap.containsKey(taskUserRef.getQiWeiUserId())){
//                                log.info("调用企业微信获取企业群发成员未匹配到任务客户，更改为操作失败");
//                                taskUserRef.setStatus(1);//执行完成
//                                taskUserRef.setReachStatus(0);
//                                taskUserRef.setCpSendStatus(2);
//                                taskUserRef.setUpdateBy("定时任务刷新执行状态");
//                                taskUserRef.setUpdateTime(new Date());
//                                updateTaskUserRefs.add(taskUserRef);
//                            }
//                        }
//                        for (WxCpGroupMsgSendResultVO.ExternalContactGroupMsgTaskInfo sendInfo : groupMessageSendResult.getSendList()) {
//                            if(!taskUserRefMap.containsKey(sendInfo.getExternalUserId())){
//                                log.info("调用企业微信获取企业群发成员未匹配到任务客户，执行下一个");
//                                continue;
//                            }
//                            CpTaskUserRef cpTaskUserRef=new CpTaskUserRef();
//                            cpTaskUserRef.setId(taskUserRefMap.get(sendInfo.getExternalUserId()).getId());
//                            if(sendInfo.getStatus()== TaskUserSendStatusEnum.SUCCESS.getValue()){
//                                cpTaskUserRef.setStatus(1);//执行完成
//                            }
//                            if(StrUtil.isNotBlank(sendInfo.getChatId())){
//                                cpTaskUserRef.setChatId(sendInfo.getChatId());
//                            }
//                            cpTaskUserRef.setReachStatus(1);
//                            cpTaskUserRef.setCpSendStatus(sendInfo.getStatus());
//                            cpTaskUserRef.setQiWeiUserId(sendInfo.getExternalUserId());
//                            cpTaskUserRef.setUpdateBy("定时任务刷新执行状态");
//                            cpTaskUserRef.setUpdateTime(new Date());
//                            if(Objects.nonNull(sendInfo.getSendTime())){
//                                cpTaskUserRef.setSendTime(WechatUtils.formatDate(sendInfo.getSendTime().toString()));
//                            }
//                            updateTaskUserRefs.add(cpTaskUserRef);
//
//                            //员工客户数据统计
//                            CpTaskStaffRef cpTaskStaffRef=new CpTaskStaffRef();
//                            cpTaskStaffRef.setId(staffRefVO.getId());
//                            Integer sendUserCount=staffRefVO.getSendUserCount();
//                            if(Objects.isNull(sendUserCount) || sendUserCount<=0){
//                                sendUserCount=1;
//                            } else{
//                                sendUserCount=sendUserCount+1;
//                            }
//                            staffRefVO.setSendUserCount(sendUserCount);
//                            cpTaskStaffRef.setSendUserCount(sendUserCount);//发送客户总数
//
//                            if(sendInfo.getStatus()==TaskUserSendStatusEnum.SUCCESS.getValue()){//发送成功
//                                Integer inviteUserCount=staffRefVO.getInviteUserCount();
//                                if(Objects.isNull(inviteUserCount) || inviteUserCount<=0){
//                                    inviteUserCount=1;
//                                } else{
//                                    inviteUserCount=inviteUserCount+1;
//                                }
//                                staffRefVO.setInviteUserCount(inviteUserCount);
//                                cpTaskStaffRef.setInviteUserCount(inviteUserCount);//邀请客户总数
//
//                                //增加任务邀请人数
//                                if(createTaskMap.containsKey(staffRefVO.getMsgId())){
//                                    Long taskId=createTaskMap.get(staffRefVO.getMsgId()).getId();
//                                    CpGroupCreateTask cpGroupCreateTask=updateCreateTaskMap.get(taskId);
//                                    Integer InviteCount=createTaskMap.get(staffRefVO.getMsgId()).getInviteCount();
//                                    if(Objects.isNull(InviteCount) || InviteCount<=0) {
//                                        InviteCount=1;
//                                    }else{
//                                        InviteCount=InviteCount+1;
//                                    }
//                                    createTaskMap.get(staffRefVO.getMsgId()).setInviteCount(InviteCount);
//                                    if(Objects.isNull(cpGroupCreateTask)){
//                                        cpGroupCreateTask=new CpGroupCreateTask();
//                                        cpGroupCreateTask.setId(taskId);
//                                        cpGroupCreateTask.setInviteCount(InviteCount);
//                                        cpGroupCreateTask.setUpdateBy("定时任务刷新执行状态");
//                                        cpGroupCreateTask.setUpdateTime(new Date());
//                                        updateCreateTaskMap.put(taskId,cpGroupCreateTask);
//                                    }else{
//                                        updateCreateTaskMap.get(taskId).setInviteCount(InviteCount);
//                                    }
//                                }
//                            }
//
//                            updateStaffRef.add(cpTaskStaffRef);
//                        }
//                    }
//
//                }
//            }
//            if(CollUtil.isNotEmpty(updateTaskUserRefs)){
//                log.info("updateTaskUserRefs-->{}", JSON.toJSONString(updateTaskUserRefs));
//                taskUserRefService.updateBatchById(updateTaskUserRefs);
//            }
//            if(CollUtil.isNotEmpty(updateCreateTaskMap)){
//                List<CpGroupCreateTask> updateCreateTask=new ArrayList<>(updateCreateTaskMap.values());
//                log.info("updateCreateTask-->{}", JSON.toJSONString(updateCreateTask));
//                groupCreateTaskService.updateBatchById(updateCreateTask);
//            }
//            if(CollUtil.isNotEmpty(updateStaffRef)){
//                taskStaffRefService.updateBatchById(updateStaffRef);
//            }
//
//
//            /**************************处理企业群发成员执行结果 end*********************************************/
//        }
//
//    }


    /**
     * 获取任务
     * @return
     */
    private List<CpGroupCreateTask> getTasks(){
        LambdaQueryWrapper<CpGroupCreateTask> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CpGroupCreateTask::getIsDelete,0);
        lambdaQueryWrapper.eq(CpGroupCreateTask::getSendStatus, SendStatus.SUCCESS.getCode());
        lambdaQueryWrapper.eq(CpGroupCreateTask::getIsReplay,0);
        return groupCreateTaskService.list(lambdaQueryWrapper);
    }

    /**
     * 获取任务员工
     * @return
     */
    private List<CpTaskStaffRefVO> getTaskStaff(){
//        LambdaQueryWrapper<CpTaskStaffRef> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(CpTaskStaffRef::getIsDelete,0);
//        lambdaQueryWrapper.eq(CpTaskStaffRef::getStatus, 0);
//        lambdaQueryWrapper.eq(CpTaskStaffRef::getTaskId, taskId);
//        return taskStaffRefService.list(lambdaQueryWrapper);
        return taskStaffRefMapper.selectTaskRefeshList();
    }

    /**
     * 获取任务客户
     * @param taskId
     * @return
     */
    private List<CpTaskUserRef> getTaskUser(Long taskId,Long staffId){
        LambdaQueryWrapper<CpTaskUserRef> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CpTaskUserRef::getIsDelete,0);
        lambdaQueryWrapper.eq(CpTaskUserRef::getStatus, 0);
        lambdaQueryWrapper.eq(CpTaskUserRef::getTaskId, taskId);
        lambdaQueryWrapper.eq(CpTaskUserRef::getStaffId, staffId);
        return taskUserRefService.list(lambdaQueryWrapper);
    }



}
