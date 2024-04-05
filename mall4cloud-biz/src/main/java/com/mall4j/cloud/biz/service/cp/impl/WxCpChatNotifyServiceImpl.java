package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.model.cp.CpCustGroup;
import com.mall4j.cloud.biz.model.cp.CpGroupCustInfo;
import com.mall4j.cloud.biz.service.cp.CustGroupService;
import com.mall4j.cloud.biz.service.cp.GroupCustInfoService;
import com.mall4j.cloud.biz.service.cp.WxCpChatNotifyService;
import com.mall4j.cloud.biz.wx.cp.constant.FlagEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: Administrator
 * @Description: 企业群操作
 * @Date: 2022-02-10 15:10
 * @Version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxCpChatNotifyServiceImpl implements WxCpChatNotifyService {
    private final CustGroupService groupService;

    private  final GroupCustInfoService custInfoService;

    private  final StaffFeignClient staffFeignClient;
    /**
     * 创建群回调
     * @param chatId 群id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(String chatId){
        WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(chatId);
        log.info("----create 群详情---chatId:【" + Json.toJsonString(groupChat)+ "】");
        if(groupChat==null){
            return;
        }
        CpCustGroup custGroup = new CpCustGroup();
        custGroup.setCreateTime(new Date());
        custGroup.setFlag(FlagEunm.USE.getCode());
        custGroup.setStatus(StatusType.YX.getCode());
        custGroup.setUpdateTime(custGroup.getCreateTime());
        custGroup.setId(groupChat.getChatId());
        custGroup.setGroupName(groupChat.getName());
        custGroup.setUserId(groupChat.getOwner());
        custGroup.setTotal(groupChat.getMemberList() != null ? groupChat.getMemberList().size() : 0);
        custGroup.setTotalLimit(500);
        setOwnerInfo(groupChat.getOwner(), custGroup);
        custGroup.setTotalCust(groupChat.getMemberList() != null ? groupChat.getMemberList().stream().filter(item -> item.getType() == 2).count(): 0);
        groupService.save(custGroup);
        //加客户
        createCust(custGroup, groupChat);
        log.info("----群create---:【SUCCESS】");
    }

    private void setOwnerInfo(String ownerId, CpCustGroup custGroup) {
        ServerResponseEntity<StaffVO> staffResp = staffFeignClient.getStaffByQiWeiUserId(ownerId);
        log.info("----群ownerId---:【"+ Json.toJsonString(staffResp)+"】");
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
        }
        log.info("----群delete---:【SUCCESS】");
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCust(String chatId) {
        CpCustGroup custGroup =   groupService.getById(chatId);
        log.info("----群custGroup---custGroup:【" + Json.toJsonString(custGroup)+ "】");
        if(custGroup==null){
            create(chatId);
            return;
        }
        WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(chatId);
        if(groupChat==null){
            return;
        }
        custGroup.setTotal(groupChat.getMemberList().size());
        custGroup.setTotalCust(groupChat.getMemberList().stream().filter(item->item.getType()==2).count());
        groupService.updateById(custGroup);
        //加客户
        createCust(custGroup, groupChat);
        log.info("----群 addCust---:【SUCCESS】");
    }

    private void createCust(CpCustGroup custGroup, WxCpUserExternalGroupChatInfo.GroupChat groupChat) {
        groupChat.getMemberList().forEach(item->{
            CpGroupCustInfo groupCustInfo =  custInfoService.getById(item.getUserId(),custGroup.getId());
            if(groupCustInfo==null){
                groupCustInfo = new CpGroupCustInfo();
                groupCustInfo.setGroupId(custGroup.getId());
                groupCustInfo.setFlag(FlagEunm.USE.getCode());
                groupCustInfo.setStatus(StatusType.YX.getCode());
                groupCustInfo.setUserId(item.getUserId());
                groupCustInfo.setCreateTime(new Date());
                groupCustInfo.setUpdateTime(groupCustInfo.getCreateTime());
                groupCustInfo.setInvitorUserId(item.getInvitor()!=null?item.getInvitor().getUserId():null);
                groupCustInfo.setJoinTime(new Date(item.getJoinTime()));
                groupCustInfo.setName(item.getName());
                groupCustInfo.setGroupNickname(item.getGroupNickname());
                groupCustInfo.setJoinScene(item.getJoinScene());
                groupCustInfo.setType(item.getType());
                custInfoService.save(groupCustInfo);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCust(String chatId) {
        CpCustGroup custGroup =   groupService.getById(chatId);
        log.info("----群deleteCust custGroup---custGroup:【" + Json.toJsonString(custGroup)+ "】");
        if(custGroup==null){
            create(chatId);
            return;
        }
        WxCpUserExternalGroupChatInfo.GroupChat groupChat = getChatDetail(chatId);
        if(groupChat==null){
            return;
        }
        custGroup.setTotal(groupChat.getMemberList()==null?0:groupChat.getMemberList().size());
        custGroup.setTotalCust(groupChat.getMemberList()==null?0:groupChat.getMemberList().stream().filter(item->item.getType()==2).count());
        groupService.updateById(custGroup);
        custInfoService.deleteByGroupId(chatId,"企微回调群解散");
        createCust(custGroup, groupChat);
        log.info("----群 addCust---:【SUCCESS】");
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeName(String chatId) {
        CpCustGroup custGroup =   groupService.getById(chatId);
        log.info("----群changeName custGroup---custGroup:【" + Json.toJsonString(custGroup)+ "】");
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
        log.info("----群 changeName---:【SUCCESS】");
    }

    private WxCpUserExternalGroupChatInfo.GroupChat getChatDetail(String groupId){
        try {
            WxCpUserExternalGroupChatInfo result =  WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().getGroupChat(groupId, 1);
            return result.getGroupChat();
        }catch (WxErrorException we){
            log.error("查询群详情出错 errorCode:【"+we.getError().getErrorCode()+"】 msg:【"+we.getError().getErrorMsg()+"】");
            return null;
        }
    }

}
