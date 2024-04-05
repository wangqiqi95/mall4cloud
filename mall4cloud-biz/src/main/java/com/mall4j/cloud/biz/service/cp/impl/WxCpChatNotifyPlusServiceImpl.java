package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.CpChatBusinessDTO;
import com.mall4j.cloud.biz.manager.CpChatBusinessManager;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.mapper.cp.GroupCustInfoMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Administrator
 * @Description: 企业群操作
 * @Date: 2022-02-10 15:10
 * @Version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxCpChatNotifyPlusServiceImpl implements WxCpChatNotifyPlusService {
    private final CustGroupService groupService;
    private  final GroupCustInfoService custInfoService;
    private  final StaffFeignClient staffFeignClient;
    private final CpCodeChannelService cpCodeChannelService;
    private final GroupCustInfoMapper groupCustInfoMapper;
    private final CpChatBusinessManager cpChatBusinessManager;
    private final WeixinCpExternalManager weixinCpExternalManager;
    /**
     * 创建群回调
     * @param chatId 群id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(String chatId){
        WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(chatId);
        log.info("----create 群详情---chatId:【" +Json.toJsonString(groupChat)+ "】");
        if(groupChat==null){
            return;
        }
        CpCustGroup custGroup = new CpCustGroup();
        custGroup.initData(custGroup,groupChat,0);
        setOwnerInfo(groupChat.getOwner(), custGroup);
        groupService.save(custGroup);
        //加客户
        createCust(custGroup, groupChat);
        log.info("----群create---:【SUCCESS】");
    }

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

    /**
     * 删除群回调
     * @param chatId 群id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String chatId){
        log.info("----群delete---chatId:【" +chatId+ "】");
        CpCustGroup custGroup =   groupService.getById(chatId);
        if(custGroup!=null){
            custGroup.setFlag(FlagEunm.DELETE.getCode());
            custGroup.setStatus(StatusType.WX.getCode());
            custGroup.setUpdateTime(new Date());
            groupService.updateById(custGroup);
            custInfoService.deleteByGroupId(chatId,"企微回调群解散");
        }
        log.info("----群delete---:【SUCCESS】");
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCust(String chatId) {
        CpCustGroup custGroup =   groupService.getById(chatId);
        log.info("----群custGroup---custGroup:【" +Json.toJsonString(custGroup)+ "】");
        if(custGroup==null){
            create(chatId);
            return;
        }
        WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(chatId);
        if(groupChat==null){
            return;
        }
        custGroup.setTotal(groupChat.getMemberList() != null ? groupChat.getMemberList().size() : 0);
        custGroup.setTotalCust(groupChat.getMemberList() != null ? groupChat.getMemberList().stream().filter(item -> item.getType() == 2).count(): 0);
        custGroup.setTotalStaffCust(groupChat.getMemberList() != null ? groupChat.getMemberList().stream().filter(item -> item.getType() == 1).count(): 0);
        groupService.updateById(custGroup);
        //加客户
        createCust(custGroup, groupChat);
        log.info("----群 addCust---:【SUCCESS】");
    }

    /**
     *群添加成员处理
     * @param custGroup
     * @param groupChat
     */
    private void createCust(CpCustGroup custGroup, WxCpUserExternalGroupChatInfo.GroupChat groupChat) {

        //群管理员处理
        Map<String, WxCpUserExternalGroupChatInfo.GroupAdmin> groupAdminMap=new HashMap<>();
        if(CollUtil.isNotEmpty(groupChat.getAdminList())){
            groupAdminMap= LambdaUtils.toMap(groupChat.getAdminList(),WxCpUserExternalGroupChatInfo.GroupAdmin::getUserId);
            log.info("客户群详情-群管理员列表: {}",JSON.toJSONString(groupAdminMap));
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
            CpCodeChannel cpCodeChannel=StrUtil.isNotEmpty(item.getState())?cpCodeChannelMaps.get(item.getState()):null;
            log.info("客户群详情-群成员列表: {}  cpCodeChannel:{}",JSON.toJSONString(item),JSON.toJSONString(cpCodeChannel));
            CpGroupCustInfo groupCustInfo =  custInfoMap.get(item.getUserId());
            //根据state获取群渠道信息
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
            custInfoService.saveOrUpdateBatch(cpGroupCustInfos);
        }

        //需要处理渠道逻辑
        CpChatBusinessDTO cpChatBusinessDTO=new CpChatBusinessDTO();
        cpChatBusinessDTO.setGroupChat(groupChat);
        cpChatBusinessDTO.setCustGroup(custGroup);
        cpChatBusinessDTO.setCpCodeChannelMaps(cpCodeChannelMaps);
        cpChatBusinessManager.businessInChat(cpChatBusinessDTO);
    }

    /**
     *
     * @param chatId
     * @param quitScene 当是成员退群时有值。表示成员的退群方式: 0 - 自己退群 / 1 - 群主/群管理员移出
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCust(WxCpXmlMessage wxMessage, String chatId, String quitScene) {
        CpCustGroup custGroup =   groupService.getById(chatId);
        log.info("----群deleteCust custGroup---custGroup:【" +Json.toJsonString(custGroup)+ "】");
        if(custGroup==null){
//            create(chatId);
            return;
        }
        WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(chatId);
        if(groupChat==null){
            return;
        }
        custGroup.setTotal(groupChat.getMemberList()==null?0:groupChat.getMemberList().size());
        custGroup.setTotalCust(groupChat.getMemberList()==null?0:groupChat.getMemberList().stream().filter(item->item.getType()==2).count());
        custGroup.setTotalStaffCust(groupChat.getMemberList().stream().filter(item->item.getType()==1).count());
        groupService.updateById(custGroup);
        //删除群成员
        CpChatBusinessDTO dto=new CpChatBusinessDTO();
        dto.setCustGroup(custGroup);
        dto.setQuitScene(quitScene);
        dto.setGroupChat(groupChat);
        dto.setWxMessage(wxMessage);
        cpChatBusinessManager.businessOutChat(dto);
        log.info("----群 deleteCust---:【SUCCESS】");
    }


    /**
     * 修改群名称
     * @param chatId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeName(String chatId) {
        CpCustGroup custGroup =   groupService.getById(chatId);
        log.info("----群changeName custGroup---custGroup:【" +Json.toJsonString(custGroup)+ "】");
        if(custGroup==null){
            create(chatId);
            return;
        }
        WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(chatId);
        if(groupChat==null){
            return;
        }
        custGroup.setGroupName(groupChat.getName());
        custGroup.setUpdateTime(new Date());
        groupService.updateById(custGroup);
        log.info("----群 changeName---:【SUCCESS】");
    }

    /**
     * 修改群主
     * @param chatId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeOwner(String chatId) {
        log.info("----群 changeOwner---chatId:【" +chatId+ "】");
        CpCustGroup custGroup =   groupService.getById(chatId);
        if(custGroup==null){
            create(chatId);
            return;
        }
        WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(chatId);
        if(groupChat==null){
            return;
        }
        custGroup.setGroupName(groupChat.getName());
        custGroup.setUpdateTime(new Date());
        custGroup.setUserId(groupChat.getOwner());
        custGroup.setStatus(StatusType.YX.getCode());
        setOwnerInfo(groupChat.getOwner(), custGroup);

        groupService.updateById(custGroup);
        //变更去成员关联员工id
        custInfoService.update(new LambdaUpdateWrapper<CpGroupCustInfo>().eq(CpGroupCustInfo::getGroupId,chatId).set(CpGroupCustInfo::getStaffId,custGroup.getOwnerId()));
        log.info("----群 changeName---:【SUCCESS】");
    }

    @Override
    public void changeChaneNotice(String chatId) {
        log.info("----群 changeChaneNotice---chatId:【" +chatId+ "】");
        CpCustGroup custGroup =   groupService.getById(chatId);
        if(custGroup==null){
            create(chatId);
            return;
        }
        WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(chatId);
        if(groupChat==null){
            return;
        }
        custGroup.setNotice(groupChat.getNotice());
        custGroup.setUpdateTime(new Date());
        custGroup.setUserId(groupChat.getOwner());
        custGroup.setStatus(StatusType.YX.getCode());
        setOwnerInfo(groupChat.getOwner(), custGroup);

        groupService.updateById(custGroup);
        log.info("----群 changeChaneNotice---:【SUCCESS】");
    }

    /**
     * 获取企微群详情
     * @param groupId
     * @return
     */
    private WxCpUserExternalGroupChatInfo.GroupChat getChatDetail(String groupId){
        return weixinCpExternalManager.getChatDetail(groupId);
    }

}
