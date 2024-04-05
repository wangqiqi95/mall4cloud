package com.mall4j.cloud.biz.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.constant.CrmPushMsgTypeEnum;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.mapper.cp.GroupCustInfoMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.service.cp.handler.WxCpExtConsts;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.LambdaUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatList;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 企微客群表跟进人状态更新
 * @author hwy
 */
@Component("CpCustGroupTask")
@RequiredArgsConstructor
@Slf4j
public class CpCustGroupTask {

    private final CustGroupService groupService;
    private final WeixinCpExternalManager weixinCpExternalManager;
    private final StaffFeignClient staffFeignClient;
    private final CpCodeChannelService cpCodeChannelService;
    private final GroupCustInfoMapper groupCustInfoMapper;
    private  final GroupCustInfoService groupCustInfoService;
//    private final CrmManager crmManager;

    /**
     * 刷新群主信息
     */
    @XxlJob("refreshCustGroupOwner")
    public void refreshCustGroupOwner(List<String> chatIds) throws WxErrorException{
        log.info("开始刷新刷新群主信息");
        long startTime=System.currentTimeMillis();
        //获取系统全部群聊
        LambdaQueryWrapper<CpCustGroup> lambdaQueryWrapper=new LambdaQueryWrapper<CpCustGroup>().eq(CpCustGroup::getFlag,0);
        if(CollUtil.isNotEmpty(chatIds)){
            lambdaQueryWrapper.in(CpCustGroup::getId,chatIds);
        }
        List<CpCustGroup>  cpCustGroups=groupService.list(lambdaQueryWrapper);
        if(CollUtil.isEmpty(cpCustGroups)){
            log.info("开始刷新刷新群主信息失败，未获取到群信息");
        }
        List<CpCustGroup> updateGroups=new ArrayList<>();
        List<WxCpUserExternalGroupChatInfo.GroupChat> updateGroupChats=new ArrayList<>();
        for (CpCustGroup custGroup : cpCustGroups) {
            //获取群详情
            WxCpUserExternalGroupChatInfo.GroupChat groupChat = weixinCpExternalManager.getChatDetail(custGroup.getId());
            if(groupChat==null){
                continue;
            }
            updateGroupChats.add(groupChat);

            //更新群主信息
            CpCustGroup updateGroup=new CpCustGroup();
            updateGroup.setId(custGroup.getId());
            setOwnerInfo(groupChat.getOwner(), updateGroup);
            updateGroup.setUpdateTime(new Date());
            updateGroups.add(updateGroup);
        }
        if(CollUtil.isNotEmpty(updateGroups)){
            groupService.updateBatchById(updateGroups);

            for (WxCpUserExternalGroupChatInfo.GroupChat groupChat : updateGroupChats) {
                //TODO 同步数云
                pushCrm(groupChat);
            }
        }
        log.info("结束执行刷新刷新群主信息，耗时：{}ms", System.currentTimeMillis() - startTime);
    }

    /**
     * 刷新群聊数据&群成员数据
     */
    @XxlJob("refreshCustGroup")
    public void refreshCustGroup() throws WxErrorException{
        log.info("开始刷新群聊数据&群成员数据");
        long startTime=System.currentTimeMillis();
        Integer limit=1000;
        Integer statusFilter=0;
        String nextCursor = "first";

        //获取系统全部群聊
        List<CpCustGroup>  cpCustGroups=groupService.list(new LambdaQueryWrapper<CpCustGroup>().eq(CpCustGroup::getFlag,0));
        Map<String,CpCustGroup> custGroupMap=LambdaUtils.toMap(cpCustGroups,CpCustGroup::getId);
        List<WxCpUserExternalGroupChatList.ChatStatus> chatStatuses=new ArrayList<>();
        do {
            if (("first").equals(nextCursor)) {
                nextCursor = "";
            }
            log.info("刷新群聊数据&群成员数据，循环拉取中 nextCursor: {}",nextCursor);
            WxCpUserExternalGroupChatList chatList= WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId())
                    .getExternalContactService().listGroupChat(limit,nextCursor,statusFilter,null);
            if(Objects.isNull(chatList)){
                break;
            }
            log.info("刷新群聊数据&群成员数据，循环拉取结果 nextCursor: {}",chatList.getNextCursor());
            if (CollUtil.isEmpty(chatList.getGroupChatList())) {
               break;
            }
            chatStatuses.addAll(chatList.getGroupChatList());

            for (WxCpUserExternalGroupChatList.ChatStatus chatStatus : chatList.getGroupChatList()) {
                //获取群详情
                WxCpUserExternalGroupChatInfo.GroupChat groupChat = weixinCpExternalManager.getChatDetail(chatStatus.getChatId());
                if(groupChat==null){
                    continue;
                }

                CpCustGroup custGroup = new CpCustGroup();
                custGroup.initData(custGroup,groupChat,chatStatus.getStatus());
                setOwnerInfo(groupChat.getOwner(), custGroup);
                if(custGroupMap.containsKey(groupChat.getChatId())){
                    //更新群信息
                    custGroup.setId(custGroupMap.get(groupChat.getChatId()).getId());
                    custGroup.setCreateTime(Objects.nonNull(groupChat.getCreateTime())?WechatUtils.formatDate(groupChat.getCreateTime().toString()):new Date());
                    custGroup.setUpdateTime(new Date());
                    groupService.updateById(custGroup);
                }else{
                    //添加群信息
                    try {
                        custGroup.setCreateTime(Objects.nonNull(groupChat.getCreateTime())?WechatUtils.formatDate(groupChat.getCreateTime().toString()):new Date());
                        groupService.save(custGroup);
                    }catch (Exception e){
                        log.info("refreshCustGroup 保存群聊失败：{} {}",e,e.getMessage());
                    }
                }
                //TODO群成员信息
                getGroupMemberList(custGroup,groupChat);

                //TODO 同步数云
                pushCrm(groupChat);

            }
            log.info("刷新群聊数据&群成员数据，循环拉取下一个 nextCursor: {}",nextCursor);
            nextCursor = chatList.getNextCursor();
            if(StrUtil.isEmpty(nextCursor)){
                break;
            }
        } while (StrUtil.isNotEmpty(nextCursor));

        //处理需要删除的群聊
        if(CollUtil.isNotEmpty(cpCustGroups) && CollUtil.isNotEmpty(chatStatuses)){
            List<CpCustGroup> delGroups=new ArrayList<>();
            Map<String,WxCpUserExternalGroupChatList.ChatStatus> custGroupMapDel=LambdaUtils.toMap(chatStatuses,WxCpUserExternalGroupChatList.ChatStatus::getChatId);
            for (CpCustGroup cpCustGroup : cpCustGroups) {
                if(!custGroupMapDel.containsKey(cpCustGroup.getId())){
                    CpCustGroup delGroup=new CpCustGroup();
                    delGroup.setId(cpCustGroup.getId());
                    delGroup.setFlag(1);
                    delGroup.setUpdateTime(new Date());
                    delGroups.add(delGroup);
                }
            }
            if(CollUtil.isNotEmpty(delGroups)){
                log.info("刷新群聊数据&群成员数据，需要删除的群聊：{}",JSON.toJSONString(delGroups));
                groupService.updateBatchById(delGroups);
                for (CpCustGroup delGroup : delGroups) {
                    groupCustInfoService.deleteByGroupId(delGroup.getId(),"同步群聊数据");
                }
            }
        }
        log.info("结束执行刷新群聊数据&群成员数据，耗时：{}ms", System.currentTimeMillis() - startTime);
    }

    private void pushCrm(WxCpUserExternalGroupChatInfo.GroupChat groupChat){
        try {
            PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
            dto.setChangetype(WxCpExtConsts.ExternalChatChangeType.ADD_CHAT);
            dto.setMsgType(CrmPushMsgTypeEnum.WX_GROUP.value());
            dto.setGroupChat(groupChat);
            //todo 待实现
//            crmManager.pushCDPCpMsgDate(dto);
        }catch (Exception e){
            log.info("刷新群聊数据&群成员数据,同步crm失败：{}",e);
        }

    }

    /**
     * 刷新群管理员身份
     * TODO 管理员需要在系统添加员工且与微信员工同步
     */
    @XxlJob("refreshCustGroupStatus")
    public void refreshCustGroupStatus()  {
        log.info("开始刷新群管理员身份");
        long startTime=System.currentTimeMillis();
        Integer limit=1000;
        Integer statusFilter=0;
        String cursor=null;
        //获取系统全部客群信息
        List<CpCustGroup> cpCustGroups=groupService.list(new LambdaQueryWrapper<CpCustGroup>().eq(CpCustGroup::getFlag,0));
        Map<String,CpCustGroup> cpCustGroupMap= LambdaUtils.toMap(cpCustGroups,CpCustGroup::getId);
        getChatList(limit,statusFilter,cursor,cpCustGroupMap);
        log.info("结束执行刷新群管理员身份，耗时：{}ms", System.currentTimeMillis() - startTime);
    }


    /**
     * 获取群管理员关联系统员工信息
     * @param ownerId
     * @param custGroup
     */
    private void setOwnerInfo(String ownerId, CpCustGroup custGroup) {
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffByQiWeiUserId(ownerId);
        log.info("----群ownerId---:【"+Json.toJsonString(staffResp)+"】");
        if (staffResp != null && staffResp.getData() != null) {
            StaffVO staffVO = staffResp.getData();
            custGroup.setOwnerId(staffVO.getId());
            custGroup.setOwnerName(staffVO.getStaffName());
            custGroup.setStoreId(staffVO.getStoreId());
            custGroup.setStoreName(staffVO.getStoreName());
        }
    }


    private void getChatList(Integer limit,Integer statusFilter,String cursor,Map<String,CpCustGroup> cpCustGroupMap){
        try {
            WxCpUserExternalGroupChatList chatList= WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId())
                    .getExternalContactService().listGroupChat(limit,cursor,statusFilter,null);

            //更新客群跟进人状态
            if(CollUtil.isNotEmpty(chatList.getGroupChatList())){
                List<CpCustGroup> cpCustGroups=new ArrayList<>();
                for (WxCpUserExternalGroupChatList.ChatStatus chatStatus : chatList.getGroupChatList()) {
                    if(cpCustGroupMap.containsKey(chatStatus.getChatId())
                    &&cpCustGroupMap.get(chatStatus.getChatId()).getAdminStatus()!=chatStatus.getStatus()){
                        CpCustGroup group=new CpCustGroup();
                        group.setId(chatStatus.getChatId());
                        group.setAdminStatus(chatStatus.getStatus());
                        cpCustGroups.add(group);
                    }
                }
                if(CollUtil.isNotEmpty(cpCustGroups)){
                    groupService.updateBatchById(cpCustGroups);
                }
            }
            if(StrUtil.isNotBlank(chatList.getNextCursor())){
                getChatList(limit,statusFilter,cursor,cpCustGroupMap);
            }
        }catch (WxErrorException e){
            log.info("企微-定时任务获取客户群列表失败 {}",e);
        }
    }

    private void getGroupMemberList(CpCustGroup custGroup, WxCpUserExternalGroupChatInfo.GroupChat groupChat){
        //群管理员处理
        Map<String, WxCpUserExternalGroupChatInfo.GroupAdmin> groupAdminMap=new HashMap<>();
        if(CollUtil.isNotEmpty(groupChat.getAdminList())){
            groupAdminMap= LambdaUtils.toMap(groupChat.getAdminList(),WxCpUserExternalGroupChatInfo.GroupAdmin::getUserId);
            log.info("客户群详情-群管理员列表: {}", JSON.toJSONString(groupAdminMap));
        }
        //群成员
        List<CpGroupCustInfo>  custInfos=groupCustInfoMapper.listByGroupId(custGroup.getId());
        Map<String,CpGroupCustInfo> custInfoMap=LambdaUtils.toMap(custInfos,CpGroupCustInfo::getUserId);
        //获取渠道信息
        List<String> sourceState=groupChat.getMemberList().stream().filter(item->StrUtil.isNotEmpty(item.getState())).map(item->item.getState()).collect(Collectors.toList());
        Map<String,CpCodeChannel>  cpCodeChannelMaps=LambdaUtils.toMap(cpCodeChannelService.getBySourceStates(sourceState),CpCodeChannel::getSourceState);
        //客户群详情-群成员列表
        List<CpGroupCustInfo> cpGroupCustInfos=new ArrayList<>();
        for (WxCpUserExternalGroupChatInfo.GroupMember item : groupChat.getMemberList()) {
            log.info("客户群详情-群成员列表: {}",JSON.toJSONString(item));
            CpGroupCustInfo groupCustInfo =  custInfoMap.get(item.getUserId());
            //根据state获取群渠道信息
            CpCodeChannel cpCodeChannel=StrUtil.isNotEmpty(item.getState())?cpCodeChannelMaps.get(item.getState()):null;
            if(Objects.isNull(groupCustInfo)){
                groupCustInfo = new CpGroupCustInfo();
            }
            if(Objects.nonNull(cpCodeChannel)){
                groupCustInfo.setCodeId(Long.parseLong(cpCodeChannel.getBaseId()));//系统内部群活码表id【cp_group_code】
                groupCustInfo.setCodeSource(cpCodeChannel.getSourceFrom());
            }
            //初始化群成员数据
            groupCustInfo.buildDate(groupCustInfo,item,custGroup);
            //是否管理员：0否/1是
            if(groupAdminMap.containsKey(item.getUserId())){
                groupCustInfo.setIsAdmin(1);
            }else{
                groupCustInfo.setIsAdmin(0);
            }
            cpGroupCustInfos.add(groupCustInfo);
        }

        if(CollUtil.isNotEmpty(cpGroupCustInfos)){//处理群成员
            groupCustInfoService.saveOrUpdateBatch(cpGroupCustInfos);
        }

        //处理退群客户
        if(CollUtil.isNotEmpty(custInfos)){
            Map<String,WxCpUserExternalGroupChatInfo.GroupMember> groupMemberMap=LambdaUtils.toMap(groupChat.getMemberList(),
                    WxCpUserExternalGroupChatInfo.GroupMember::getUserId);
            List<CpGroupCustInfo> updates=new ArrayList<>();
            for (CpGroupCustInfo custInfo : custInfos) {
                if(!groupMemberMap.containsKey(custInfo.getUserId())){
                    //匹配到需要退群客户
                    CpGroupCustInfo updateInfo=new CpGroupCustInfo();
                    updateInfo.setId(custInfo.getId());
                    updateInfo.setFlag(1);
                    updateInfo.setName(custInfo.getName());
                    updateInfo.setGroupNickname(custInfo.getGroupNickname());
                    updateInfo.setGroupId(custInfo.getGroupId());
                    updateInfo.setUserId(custInfo.getUserId());
                    updateInfo.setUpdateBy("批量处理退群客户");
                    updateInfo.setUpdateTime(new Date());
                    updates.add(updateInfo);
                }
            }
            if(CollUtil.isNotEmpty(updates)){
                log.info("处理退群客户:{}",JSON.toJSONString(updates));
            groupCustInfoService.updateBatchById(updates);
            }
        }
    }


}

