package com.mall4j.cloud.biz.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.biz.dto.cp.CpChatBusinessDTO;
import com.mall4j.cloud.biz.mapper.cp.CpAutoGroupCodeUserMapper;
import com.mall4j.cloud.biz.mapper.cp.CpGroupCodeToolMapper;
import com.mall4j.cloud.biz.mapper.cp.CpTaskUserRefMapper;
import com.mall4j.cloud.biz.mapper.cp.GroupCustInfoMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CpChatBusinessManager {

    private final CpTaskUserRefMapper cpTaskUserRefMapper;
    private final CpAutoGroupCodeUserMapper cpAutoGroupCodeUserMapper;
    private final CpTaskUserRefService cpTaskUserRefService;
    private final CpAutoGroupCodeUserService autoGroupCodeUserService;
//    private final GroupCustInfoMapper groupCustInfoMapper;
    private final GroupCustInfoService custInfoService;
    private final GroupCreateTaskService groupCreateTaskService;
    private final GroupCodeRefService groupCodeRefService;
    private final GroupCreateTaskService getGroupCreateTaskService;

    /**
     * 入群
     * 1.处理渠道源信息: 自动拉群、标签建群
     * @param dto
     */
    @Async
    public void businessInChat(CpChatBusinessDTO dto){

        dto.setGroupCodeUsers(new ArrayList<>());
        dto.setTaskUserRefs(new ArrayList<>());
        if(CollUtil.isEmpty(dto.getCpCodeChannelMaps())){
            log.info("客群信息变更失败，未获取到渠道源信息");
            return;
        }
        List<CpGroupCreateTask> cpGroupCreateTasks=new ArrayList<>();
        for (WxCpUserExternalGroupChatInfo.GroupMember item : dto.getGroupChat().getMemberList()) {
            //根据state获取群渠道信息
            CpCodeChannel cpCodeChannel= StrUtil.isNotEmpty(item.getState())?dto.getCpCodeChannelMaps().get(item.getState()):null;
            log.info("企微群--根据state获取群渠道信息 {}", JSON.toJSONString(cpCodeChannel));
            if(Objects.isNull(cpCodeChannel)){
                continue;
            }
            //TODO 处理标签建群发送好友
            if(cpCodeChannel.getSourceFrom()== CodeChannelEnum.TAG_GROUP_CODE.getValue()){
                CpTaskUserRef cpTaskUserRef=cpTaskUserRefMapper.getByUserId(Long.parseLong(cpCodeChannel.getBaseId()),item.getUserId());
                log.info("企微群--处理标签建群发送好友: {} 渠道信息：{}", JSON.toJSONString(cpTaskUserRef),JSON.toJSONString(cpCodeChannel));
                if(Objects.nonNull(cpTaskUserRef) && cpTaskUserRef.getJoinGroup()==0){
                    cpTaskUserRef.setJoinGroup(1);
                    cpTaskUserRef.setState(item.getState());
                    cpTaskUserRef.setGroupName(item.getGroupNickname());
                    cpTaskUserRef.setChatId(dto.getCustGroup().getId());
                    cpTaskUserRef.setUpdateBy("客户入群触发");
                    cpTaskUserRef.setUpdateTime(new Date());
                    dto.getTaskUserRefs().add(cpTaskUserRef);
                    //处理任务入群总人数
                    CpGroupCreateTask cpGroupCreateTask=groupCreateTaskService.getById(cpCodeChannel.getBaseId());
                    if(Objects.nonNull(cpGroupCreateTask)){
                        Integer joinGroupCount=cpGroupCreateTask.getJoinGroupCount();
                        if(Objects.isNull(joinGroupCount) || joinGroupCount<=0) {
                            joinGroupCount=1;
                        }else{
                            joinGroupCount=joinGroupCount+1;
                        }
                        cpGroupCreateTask.setJoinGroupCount(joinGroupCount);
                        cpGroupCreateTask.setUpdateBy("客户入群触发");
                        cpGroupCreateTask.setUpdateTime(new Date());
                        cpGroupCreateTasks.add(cpGroupCreateTask);
                    }
                }
            }
            //TODO 处理自动拉群好友关系表数据
            if(cpCodeChannel.getSourceFrom()== CodeChannelEnum.AUTO_GROUP_CODE.getValue()){
                CpAutoGroupCodeUser cpAutoGroupCodeUser=cpAutoGroupCodeUserMapper.getByUserId(item.getUserId(),Long.parseLong(cpCodeChannel.getBaseId()));
                log.info("企微群--处理自动拉群好友关系表数据: {} 渠道信息：{}", JSON.toJSONString(cpAutoGroupCodeUser),JSON.toJSONString(cpCodeChannel));
                if(Objects.nonNull(cpAutoGroupCodeUser) && cpAutoGroupCodeUser.getJoinGroup()==0){
                    cpAutoGroupCodeUser.setJoinGroup(1);
                    cpAutoGroupCodeUser.setState(item.getState());
                    cpAutoGroupCodeUser.setChatId(dto.getCustGroup().getId());
                    cpAutoGroupCodeUser.setUpdateBy("客户入群触发");
                    cpAutoGroupCodeUser.setUpdateTime(new Date());
                    cpAutoGroupCodeUser.setJoinGroupTime(WechatUtils.formatDate(item.getJoinTime().toString()));
                    dto.getGroupCodeUsers().add(cpAutoGroupCodeUser);
                }
            }
        }

        if(CollUtil.isNotEmpty(dto.getTaskUserRefs())){//处理标签建群发送好友
            cpTaskUserRefService.updateBatchById(dto.getTaskUserRefs());
        }
        if(CollUtil.isNotEmpty(dto.getGroupCodeUsers())){//处理自动拉群好友关系表数据
            autoGroupCodeUserService.updateBatchById(dto.getGroupCodeUsers());
        }
        if(CollUtil.isNotEmpty(cpGroupCreateTasks)){//处理任务入群总人数
            groupCreateTaskService.updateBatchById(cpGroupCreateTasks);
        }

        //更新入群人数
        addScanCount(dto.getGroupChat().getMemberList().stream().filter(item->StrUtil.isNotEmpty(item.getState())).collect(Collectors.toList()));
    }

    public static void main(String[] strings){
        int joinGroupCount=1;
        System.out.println(joinGroupCount++);
        joinGroupCount=joinGroupCount++;
        System.out.println(joinGroupCount);

    }

    /**
     * 退群
     * 1. 删除群成员
     * 2. TODO 是否需要处理：自动拉群/群活码/标签建群
     * @param dto
     */
    public void businessOutChat(CpChatBusinessDTO dto){
        String quitScene=dto.getQuitScene();
        //匹配不到的成员为已删除
        JSONObject MemChangeList=JSONObject.parseObject(JSON.toJSONString(dto.getWxMessage().getAllFieldsMap().get("MemChangeList")),JSONObject.class);
        List<String> ChangeList=JSONArray.parseArray(MemChangeList.getString("Item"),String.class);
        log.info("删除群成员--> MemChangeList:{}",JSON.toJSONString(dto.getWxMessage().getAllFieldsMap().get("MemChangeList")));
        log.info("删除群成员--> ChangeList:{} ",JSON.toJSONString(ChangeList));

        List<CpGroupCustInfo>  custInfosAll=custInfoService.list(new LambdaQueryWrapper<CpGroupCustInfo>()
                .eq(CpGroupCustInfo::getGroupId,dto.getCustGroup().getId())
                .eq(CpGroupCustInfo::getFlag,0));
        if(Objects.isNull(custInfosAll)){
            log.info("删除群成员失败--> custInfosAll is null");
            return;
        }
        //处理退群客户
        Map<String,WxCpUserExternalGroupChatInfo.GroupMember> groupMemberMap=LambdaUtils.toMap(dto.getGroupChat().getMemberList(),
                WxCpUserExternalGroupChatInfo.GroupMember::getUserId);
        List<CpGroupCustInfo> delCustInfos=new ArrayList<>();
        for (CpGroupCustInfo custInfo : custInfosAll) {
            if(!groupMemberMap.containsKey(custInfo.getUserId())){
                //匹配到需要退群客户
                CpGroupCustInfo updateInfo=new CpGroupCustInfo();
                updateInfo.setId(custInfo.getId());
                updateInfo.setFlag(1);
                if(StrUtil.isNotEmpty(quitScene)){
                    updateInfo.setQuitScene(Integer.parseInt(quitScene));
                }
                updateInfo.setName(custInfo.getName());
                updateInfo.setGroupNickname(custInfo.getGroupNickname());
                updateInfo.setGroupId(custInfo.getGroupId());
                updateInfo.setUserId(custInfo.getUserId());
                updateInfo.setUpdateBy("客户退群触发");
                updateInfo.setUpdateTime(new Date());
                delCustInfos.add(updateInfo);
            }
        }
        if(Objects.isNull(delCustInfos)){
            log.info("删除群成员失败--> 为匹配到退群客户->【{}】");
            return;
        }
        log.info("客户退群触发:{}",JSON.toJSONString(delCustInfos));
        custInfoService.updateBatchById(delCustInfos);

        //更新入群人数
        delScanCount(delCustInfos.stream().filter(item->StrUtil.isNotEmpty(item.getState())).collect(Collectors.toList()), ChangeList);

//        List<CpGroupCustInfo>  custInfos=custInfoService.list(new LambdaQueryWrapper<CpGroupCustInfo>()
//                .eq(CpGroupCustInfo::getGroupId,dto.getCustGroup().getId())
//                .eq(CpGroupCustInfo::getFlag,0)
//                .in(CpGroupCustInfo::getUserId,ChangeList));

//        if(Objects.isNull(custInfos)){
//            log.info("删除群成员失败--> 根据入参MemChangeList获取到对应成员：MemChangeList->【{}】",JSON.toJSONString(ChangeList));
//            return;
//        }
//
//        LambdaUpdateWrapper<CpGroupCustInfo> updateWrapper=new LambdaUpdateWrapper<CpGroupCustInfo>();
//        updateWrapper.in(CpGroupCustInfo::getUserId,ChangeList);
//        updateWrapper.eq(CpGroupCustInfo::getGroupId,dto.getCustGroup().getId());
//        updateWrapper.set(CpGroupCustInfo::getFlag,1);
//        if(StrUtil.isNotEmpty(quitScene)){
//            updateWrapper.set(CpGroupCustInfo::getQuitScene,Integer.parseInt(quitScene));
//        }
//        custInfoService.update(updateWrapper);
//        //更新入群人数
//        delScanCount(custInfos.stream().filter(item->StrUtil.isNotEmpty(item.getState())).collect(Collectors.toList()), ChangeList);
    }

    /**
     * 更新群活码扫码入群人数【渠道入群】
     */
    private void addScanCount(List<WxCpUserExternalGroupChatInfo.GroupMember> memberList){
        log.info("更新群活码扫码入群人数【渠道入群】 -> {}",JSON.toJSONString(memberList));
        if(CollUtil.isEmpty(memberList)){
            return;
        }
        List<String> states=memberList.stream().filter(item->StrUtil.isNotEmpty(item.getState())).map(item->item.getState()).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<CpGroupCodeRef> lambdaQueryWrapper=new LambdaQueryWrapper<CpGroupCodeRef>();
        lambdaQueryWrapper.eq(CpGroupCodeRef::getIsDelete,0);
        lambdaQueryWrapper.in(CpGroupCodeRef::getState,states);
        List<CpGroupCodeRef> cpGroupCodeRefs=groupCodeRefService.list(lambdaQueryWrapper);
        if(CollUtil.isEmpty(cpGroupCodeRefs)){
            return;
        }
        Map<String,List<WxCpUserExternalGroupChatInfo.GroupMember>> groupMemberMap=LambdaUtils.groupList(memberList, WxCpUserExternalGroupChatInfo.GroupMember::getState);
        List<CpGroupCodeRef> updateCpGroupCodeRef=new ArrayList<>();
        for (CpGroupCodeRef cpGroupCodeRef : cpGroupCodeRefs) {
            if(groupMemberMap.containsKey(cpGroupCodeRef.getState())){
                CpGroupCodeRef update=new CpGroupCodeRef();
                update.setId(cpGroupCodeRef.getId());
                int scanCount=groupMemberMap.get(cpGroupCodeRef.getState()).size();
                if(scanCount>cpGroupCodeRef.getTotal()){
                    scanCount=cpGroupCodeRef.getTotal();
                }
                update.setScanCount(scanCount);
                update.setUpdateBy("客户入群触发");
                update.setUpdateTime(new Date());
                updateCpGroupCodeRef.add(update);
            }
        }
        if(CollUtil.isNotEmpty(updateCpGroupCodeRef)){
            groupCodeRefService.updateBatchById(updateCpGroupCodeRef);
        }
    }

    /**
     * 更新群活码扫码入群人数【渠道退群】
     */
    private void delScanCount(List<CpGroupCustInfo> cpGroupCustInfos,List<String> ChangeList){
        if(CollUtil.isEmpty(cpGroupCustInfos)){
            return;
        }
        List<String> states=cpGroupCustInfos.stream().filter(item->StrUtil.isNotEmpty(item.getState())).map(item->item.getState()).distinct().collect(Collectors.toList());
        log.info("更新群活码扫码入群人数【渠道退群】 -> {}",JSON.toJSONString(states));
        if(CollUtil.isEmpty(states)){
            return;
        }
        LambdaQueryWrapper<CpGroupCodeRef> lambdaQueryWrapper=new LambdaQueryWrapper<CpGroupCodeRef>();
        lambdaQueryWrapper.eq(CpGroupCodeRef::getIsDelete,0);
        lambdaQueryWrapper.in(CpGroupCodeRef::getState,states);
        List<CpGroupCodeRef> cpGroupCodeRefs=groupCodeRefService.list(lambdaQueryWrapper);
        if(CollUtil.isNotEmpty(cpGroupCodeRefs)){
            Map<String,List<CpGroupCustInfo>> groupMemberMap=LambdaUtils.groupList(cpGroupCustInfos, CpGroupCustInfo::getState);
            log.info("渠道退群 : cpGroupCodeRefs->{} groupMemberMap->{}",JSON.toJSONString(cpGroupCodeRefs),JSON.toJSONString(groupMemberMap));
            List<CpGroupCodeRef> updateCpGroupCodeRef=new ArrayList<>();
            for (CpGroupCodeRef cpGroupCodeRef : cpGroupCodeRefs) {
                if(groupMemberMap.containsKey(cpGroupCodeRef.getState())){
                    CpGroupCodeRef update=new CpGroupCodeRef();
                    update.setId(cpGroupCodeRef.getId());
                    int delCount=groupMemberMap.get(cpGroupCodeRef.getState()).size();
                    int scanCount= cpGroupCodeRef.getScanCount()>delCount?cpGroupCodeRef.getScanCount()-delCount:0;
                    scanCount=scanCount<0?0:scanCount;
                    update.setScanCount(scanCount);
                    update.setUpdateBy("客户退群触发");
                    update.setUpdateTime(new Date());
                    updateCpGroupCodeRef.add(update);
                }
            }
            if(CollUtil.isNotEmpty(updateCpGroupCodeRef)){
                groupCodeRefService.updateBatchById(updateCpGroupCodeRef);
            }
        }

        //处理自动拉群
        LambdaQueryWrapper<CpAutoGroupCodeUser> codeUserLambdaQueryWrapper=new LambdaQueryWrapper<CpAutoGroupCodeUser>();
        codeUserLambdaQueryWrapper.eq(CpAutoGroupCodeUser::getIsDelete,0);
        codeUserLambdaQueryWrapper.in(CpAutoGroupCodeUser::getState,states);
        codeUserLambdaQueryWrapper.in(CpAutoGroupCodeUser::getQiWeiUserId,ChangeList);
        List<CpAutoGroupCodeUser> autoGroupCodeUsers=autoGroupCodeUserService.list(codeUserLambdaQueryWrapper);
        if(CollUtil.isNotEmpty(autoGroupCodeUsers)){
            for (CpAutoGroupCodeUser autoGroupCodeUser : autoGroupCodeUsers) {
                autoGroupCodeUser.setJoinGroup(0);
                autoGroupCodeUser.setUpdateBy("客户退群触发");
                autoGroupCodeUser.setUpdateTime(new Date());
            }
            autoGroupCodeUserService.updateBatchById(autoGroupCodeUsers);
        }

        //处理标签建群
        LambdaQueryWrapper<CpTaskUserRef> taskUserRefLambdaQueryWrapper=new LambdaQueryWrapper<CpTaskUserRef>();
        taskUserRefLambdaQueryWrapper.eq(CpTaskUserRef::getIsDelete,0);
        taskUserRefLambdaQueryWrapper.in(CpTaskUserRef::getState,states);
        taskUserRefLambdaQueryWrapper.in(CpTaskUserRef::getQiWeiUserId,ChangeList);
        List<CpTaskUserRef> taskUserRefs=cpTaskUserRefService.list(taskUserRefLambdaQueryWrapper);
        if(CollUtil.isNotEmpty(taskUserRefs)){
            for (CpTaskUserRef taskUserRef : taskUserRefs) {
                taskUserRef.setJoinGroup(0);
                taskUserRef.setChatId("");
                taskUserRef.setUpdateBy("客户退群触发");
                taskUserRef.setUpdateTime(new Date());
            }
            cpTaskUserRefService.updateBatchById(taskUserRefs);
            //处理标签建群：入群人总数
            List<Long> taskIds=taskUserRefs.stream().map(item->item.getTaskId()).collect(Collectors.toList());
            LambdaQueryWrapper<CpGroupCreateTask> GroupCreateTaskLambdaQueryWrapper=new LambdaQueryWrapper<CpGroupCreateTask>();
            GroupCreateTaskLambdaQueryWrapper.in(CpGroupCreateTask::getId,taskIds);
            List<CpGroupCreateTask> groupCreateTasks=groupCreateTaskService.list(GroupCreateTaskLambdaQueryWrapper);
            if(CollUtil.isNotEmpty(groupCreateTasks)){
                Map<Long,List<CpTaskUserRef>> taskUserRefMap=LambdaUtils.groupList(taskUserRefs,CpTaskUserRef::getTaskId);
                List<CpGroupCreateTask> updateCpGroupCreateTask=new ArrayList<>();
                for (CpGroupCreateTask groupCreateTask : groupCreateTasks) {
                    if(taskUserRefMap.containsKey(groupCreateTask.getId())){
                        int delJoinCount=taskUserRefMap.get(groupCreateTask.getId()).size();
                        int joinGroupCount=groupCreateTask.getJoinGroupCount()-delJoinCount;
                        if(joinGroupCount<0){
                            joinGroupCount=0;
                        }
                        CpGroupCreateTask updateGroupCreateTask=new CpGroupCreateTask();
                        updateGroupCreateTask.setJoinGroupCount(joinGroupCount);
                        updateGroupCreateTask.setId(groupCreateTask.getId());
                        updateGroupCreateTask.setUpdateBy("客户退群触发");
                        updateGroupCreateTask.setUpdateTime(new Date());
                        updateCpGroupCreateTask.add(updateGroupCreateTask);
                    }
                }
                if(CollUtil.isNotEmpty(updateCpGroupCreateTask)){
                    groupCreateTaskService.updateBatchById(updateCpGroupCreateTask);
                }
            }
        }
    }



}
