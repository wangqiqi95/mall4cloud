package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.AttachmentExtApiDTO;
import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.api.user.dto.UserTageAddDto;
import com.mall4j.cloud.api.user.feign.UserTagClient;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.dto.cp.StaffCodeTagDTO;
import com.mall4j.cloud.biz.model.cp.CpWelcome;
import com.mall4j.cloud.biz.model.cp.CpWelcomeAttachment;
import com.mall4j.cloud.biz.model.cp.StaffCode;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.NumberTo64;
import com.mall4j.cloud.common.util.PlaceholderResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.external.WxCpWelcomeMsg;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.MiniProgram;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Administrator
 * @Description: 企业微信添加客户，接受消息处理服务
 * @Date: 2022-02-10 17:28
 * @Version: 1.0
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WxCpCustNotifyServiceImpl implements WxCpCustNotifyService {
    private  final WelcomeService welcomeService;
    private  final StaffFeignClient staffFeignClient;
    private  final WelcomeAttachmentService attachmentService ;
    private  final StaffCodeService staffCodeService ;
    private  final TentacleContentFeignClient tentacleFeignClient;
    private  final OnsMQTemplate qiWeiFriendsSyncTemplate;
    private  final UserTagClient userTagClient;
    private  final WxCpPushService wxCpPushService;

    @Override
    public void create(String userId, String externalUserId, WxCpExternalContactInfo wxCpExternalContactInfo)   {
        try {
//            log.info("----cust create -------userId：【"+userId+"】  externalUserId:【"+externalUserId+"】");
//            WxCpExternalContactInfo wxCpExternalContactInfo = WxCpConfiguration.getWxCpService(WxCpConfiguration.CP_EXTERNAL_AGENT_ID).getExternalContactService()
//                    .getContactDetail(externalUserId);
//            log.info("----cust detail -------：【"+Json.toJsonString(wxCpExternalContactInfo)+"】");
            UserStaffCpRelationDTO userStaffCpRelationDTO = new UserStaffCpRelationDTO();
            //客户外部联系人id
            userStaffCpRelationDTO.setQiWeiUserId(externalUserId);
            //员工的企业微信id
            userStaffCpRelationDTO.setQiWeiStaffId(userId);
            //客户unionId
            userStaffCpRelationDTO.setUserUnionId(wxCpExternalContactInfo.getExternalContact().getUnionId());
            userStaffCpRelationDTO.setQiWeiNickName(wxCpExternalContactInfo.getExternalContact().getName());
            userStaffCpRelationDTO.setStatus(1);
            qiWeiFriendsSyncTemplate.syncSend(userStaffCpRelationDTO);
//        }catch (WxErrorException e){
        }catch (Exception e){
            log.error("",e);
        }
    }
    //补全丢失的客户或者unionId
    @Override
    public void update(String userId, String externalUserId, WxCpExternalContactInfo wxCpExternalContactInfo) {
        create(userId, externalUserId,wxCpExternalContactInfo);
    }

    @Override
    public void delete(String userId, String externalUserId) {
        try {
            log.info("----cust delete -------userId：【"+userId+"】  externalUserId:【"+externalUserId+"】");
            UserStaffCpRelationDTO userStaffCpRelationDTO = new UserStaffCpRelationDTO();
            //员工的企业微信id
            userStaffCpRelationDTO.setQiWeiStaffId(userId);
            //客户外部联系人id
            userStaffCpRelationDTO.setQiWeiUserId(externalUserId);
            userStaffCpRelationDTO.setStatus(3);
            qiWeiFriendsSyncTemplate.syncSend(userStaffCpRelationDTO);
            wxCpPushService.deleteUserNotify(userId,externalUserId);
        }catch (Exception e){
            log.error("",e);
        }
    }

    @Override
    public void sendWelcomeMsg(String welcomeCode, String state, String userId,String userName) {
        try{
            log.info("----cust sendWelcomeMsg -------userId：【"+userId+"】  welcomeCode:【"+welcomeCode+"】 state:【"+state+"】");
            ServerResponseEntity<StaffVO> response =  staffFeignClient.getStaffByQiWeiUserId(userId);
            log.info("----cust sendWelcomeMsg getStaffByQiWeiUserId rep : {}", JSONObject.toJSONString(response));
            if(response.getData()!=null) {
                //添加的员工信息
                StaffVO staffVO = response.getData();
                WxCpWelcomeMsg wxCpWelcomeMsg = getWxCpStaffCodeMsg(staffVO,state,userName);
                log.info("----cust sendWelcomeMsg ---staff code----：【"+ Json.toJsonString(wxCpWelcomeMsg) +"】 ");
                if(wxCpWelcomeMsg==null){
                    wxCpWelcomeMsg  =  getWxCpWelcomeMsg(staffVO,state,userName);
                }
                if(wxCpWelcomeMsg!=null) {
                    wxCpWelcomeMsg.setWelcomeCode(welcomeCode);
                    log.info("----cust sendWelcomeMsg ---groble----：【"+wxCpWelcomeMsg.toJson() +"】 ");
                    WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().sendWelcomeMsg(wxCpWelcomeMsg);
                }
            }
            log.info("----cust sendWelcomeMsg -------：【SUCCESS】");
        }catch (Exception e){
            log.error("发送欢迎语失败", e);
        }
    }


    @Override
    public void addTagToCust(String state, String userId,String externalUserId ) {
        try{
            log.info("----cust addTagToCust -------userId：【"+userId+"】  state:【"+state+"】");
            ServerResponseEntity<StaffVO> response =  staffFeignClient.getStaffByQiWeiUserId(userId);
            log.info("----cust addTagToCust getStaffByQiWeiUserId rep : {}", JSONObject.toJSONString(response));
            if(response.getData()!=null) {
                //添加的员工信息
                StaffVO staffVO = response.getData();
                //从员工活码免活码登陆/或者验证登陆
                if(StringUtils.isNotEmpty(state)) {
                    StaffCode staffCode = staffCodeService.selectByStaffIdAndState(staffVO.getId(), state);
                    if(staffCode!=null && StringUtils.isNotEmpty(staffCode.getTags())){
                        List<StaffCodeTagDTO> tagList =  Json.parseArray(staffCode.getTags(), StaffCodeTagDTO[].class);
                        UserTageAddDto userTageAddDto = new UserTageAddDto();
                        userTageAddDto.setQiWeiUserId(externalUserId);
                        userTageAddDto.setUserTagIds(tagList.parallelStream().map(item->item.getTagId()).collect(Collectors.toList()));
                        userTagClient.addUserTags(userTageAddDto);
                    }
                }
            }
            log.info("----cust addTagToCust -------：【SUCCESS】");
        }catch (Exception e){
            log.error("客户加标签失败",e);
        }
    }

    @Override
    public ServerResponseEntity<String> defineStaffMiniProgram(AttachmentExtApiDTO attachmentExtApiDTO, StaffVO staffVO) {
        AttachmentExtDTO attachmentExtDTO = new AttachmentExtDTO();

        Attachment attachment = new Attachment();

        MiniProgram miniprogram = new MiniProgram();

        BeanUtils.copyProperties(attachmentExtApiDTO.getAttachment(), attachment);
        BeanUtils.copyProperties(attachmentExtApiDTO.getAttachment().getMiniProgram(), miniprogram);

        attachment.setMiniProgram(miniprogram);
        attachmentExtDTO.setAttachment(attachment);
        attachmentExtDTO.setLocalUrl(attachmentExtApiDTO.getLocalUrl());

        this.setMiniProgram(attachmentExtDTO, staffVO);

        return ServerResponseEntity.success(attachment.getMiniProgram().getPage());
    }


    /**
     * 获取员工活码的欢迎语配置信息
     * @param staffVO 员工信息
     * @param state 活码的配置信息
     * @return
     */
    private WxCpWelcomeMsg getWxCpStaffCodeMsg(StaffVO staffVO, String state, String userName){
        //从员工活码免活码登陆/或者验证登陆
        if(StringUtils.isNotEmpty(state)){
            StaffCode staffCode = staffCodeService.selectByStaffIdAndState(staffVO.getId(),state);
            if(staffCode!=null){
                String slogan=getSlogan(staffVO,staffCode.getSlogan(),userName);
                CpWelcomeAttachment attachment =attachmentService.getAttachmentByWelId(staffCode.getId(), OriginType.STAFF_CODE.getCode());
                Text text =  new Text();
                text.setContent(slogan);
//                text.setContent((staffCode.getSlogan()));
                WxCpWelcomeMsg sendWelcomeMsg = new WxCpWelcomeMsg();
                sendWelcomeMsg.setText(text);
                AttachmentExtDTO attachmentExtDTO = Json.parseObject(attachment.getData(), AttachmentExtDTO.class);
                setMiniProgram(attachmentExtDTO,staffVO);
                sendWelcomeMsg.setAttachments(Lists.newArrayList(attachmentExtDTO.getAttachment()));
                return sendWelcomeMsg;
            }
        }
        return null;
    }
    /**
     * 获取商店欢迎语配置信息
     * @param staffVO 员工信息
     * @param state 活码的配置信息
     * @return
     */
    private WxCpWelcomeMsg getWxCpWelcomeMsg(StaffVO staffVO, String state, String userName){
        //非员工活码进入 根据添加客户的员工的店来查
        CpWelcome welcome =  welcomeService.getWelcomeByWeight(staffVO.getStoreId());
        if(welcome==null){
            log.info("商店【"+staffVO.getStoreId()+"】尚未配置欢迎语");
            return null;
        }
        String slogan=getSlogan(staffVO,welcome.getSlogan(),userName);
        CpWelcomeAttachment welcomeAttachment =attachmentService.getAttachmentByWelId(welcome.getId(), OriginType.WEL_CONFIG.getCode());
        Text text =  new Text();
        text.setContent(slogan);
//        text.setContent((welcome.getSlogan()));
        WxCpWelcomeMsg sendWelcomeMsg = new WxCpWelcomeMsg();
        sendWelcomeMsg.setText(text);
        AttachmentExtDTO attachmentExtDTO = Json.parseObject(welcomeAttachment.getData(), AttachmentExtDTO.class);
        setMiniProgram(attachmentExtDTO,staffVO);
        sendWelcomeMsg.setAttachments(Lists.newArrayList(attachmentExtDTO.getAttachment()));
        return sendWelcomeMsg;
    }

    private String getSlogan(StaffVO staffVO, String slogan, String userName){
        log.info("---getSlogan--原始slogan->【{}】  staffVO->【{}】 userName->【{}】",slogan,staffVO,userName);
        try {
            PlaceholderResolver defaultResolver = PlaceholderResolver.getDefaultResolver();
            Map<String,Object> keyContent=new LinkedHashMap<>();
            //获取员工信息
            if(defaultResolver.checkVariables(slogan,"${staffName}")){
                keyContent.put("staffName", StrUtil.isNotBlank(staffVO.getStaffName())?staffVO.getStaffName():"");
            }
            //获取员工所属门店信息
            if(defaultResolver.checkVariables(slogan,"${storeName}")){
                keyContent.put("storeName",StrUtil.isNotBlank(staffVO.getStoreName())?staffVO.getStoreName():"");
            }
            //获取门店简称
            if(defaultResolver.checkVariables(slogan,"${orgName}")){
                keyContent.put("orgName",StrUtil.isNotBlank(staffVO.getOrgName())?staffVO.getOrgName():"");
            }
            //客户昵称
            if(defaultResolver.checkVariables(slogan,"${userName}")){
                keyContent.put("userName",StrUtil.isNotBlank(userName)?userName:"");
            }
            slogan=defaultResolver.resolveByMap(slogan,keyContent);
        }catch (Exception e){
//            e.printStackTrace();
            log.info("欢迎语变量替换失败 {} {}",e,e.getMessage());
        }

        log.info("---getSlogan--替换后->"+slogan);
        return slogan;
    }

    /**
     * 添加小程序参数
     * @param attachmentExtDTO
     * @param staffVO
     */
    private void setMiniProgram(AttachmentExtDTO attachmentExtDTO, StaffVO staffVO){
        if(attachmentExtDTO.getAttachment().getMiniProgram()!=null){
            MiniProgram miniProgram =  attachmentExtDTO.getAttachment().getMiniProgram();
            TentacleContentDTO tentacleContent = new TentacleContentDTO();
            tentacleContent.setBusinessId(staffVO.getId());
            tentacleContent.setTentacleType(4);
            ServerResponseEntity<TentacleContentVO> tenInfo  = tentacleFeignClient.findOrCreateByContent(tentacleContent);
            if(tenInfo!=null && tenInfo.getData()!=null) {
                String t = NumberTo64.to64(Long.parseLong(tenInfo.getData().getTentacleNo()));
                if(miniProgram.getPage().indexOf("?")>0){
                    miniProgram.setPage(miniProgram.getPage() + "&t=" + t+"&s="+ NumberTo64.to64(staffVO.getStoreId()));
                }else {
                    miniProgram.setPage(miniProgram.getPage() + "?t=" + t + "&s=" + NumberTo64.to64(staffVO.getStoreId()));
                }
            }
        }
    }
}
