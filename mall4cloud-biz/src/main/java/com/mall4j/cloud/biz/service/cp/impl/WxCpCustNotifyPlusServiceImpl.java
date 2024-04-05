package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.constant.cp.CodeChannelEnum;
import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.AttachmentExtApiDTO;
import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.biz.constant.OriginType;
import com.mall4j.cloud.biz.dto.cp.AttachmentExtDTO;
import com.mall4j.cloud.biz.dto.cp.wx.externalcontact.SendWelcomeDTO;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.*;
import com.mall4j.cloud.biz.service.cp.handler.plus.CpExternalChannelPlusHandler;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.bean.external.WxCpWelcomeMsg;
import me.chanjar.weixin.cp.bean.external.contact.FollowedUser;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.MiniProgram;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.constant.WxCpConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: Administrator
 * @Description: 企业微信添加客户，接受消息处理服务
 * @Date: 2022-02-10 17:28
 * @Version: 1.0
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WxCpCustNotifyPlusServiceImpl implements WxCpCustNotifyPlusService {
    private  final WelcomeService welcomeService;
    private  final WelcomeAttachmentService attachmentService ;
    private  final StaffCodePlusService staffCodeService ;
    private  final TentacleContentFeignClient tentacleFeignClient;
    private  final WxCpPushService wxCpPushService;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    private final CpCodeChannelService cpCodeChannelService;
    private final CpExternalChannelPlusHandler cpExternalHandler;
    private final CpAutoGroupCodeService autoGroupCodeService;

    @Override
    public void create(WxCpXmlMessage wxMessage, WxCpExternalContactInfo wxCpExternalContactInfo)   {
        try {
            UserStaffCpRelationDTO userStaffCpRelationDTO = new UserStaffCpRelationDTO();
//            //客户外部联系人id
            userStaffCpRelationDTO.setQiWeiUserId(wxMessage.getExternalUserId());
            //员工的企业微信id
            userStaffCpRelationDTO.setQiWeiStaffId(wxMessage.getUserId());
            //客户unionId
            userStaffCpRelationDTO.setUserUnionId(wxCpExternalContactInfo.getExternalContact().getUnionId());
            userStaffCpRelationDTO.setQiWeiNickName(wxCpExternalContactInfo.getExternalContact().getName());
            userStaffCpRelationDTO.setAvatar(wxCpExternalContactInfo.getExternalContact().getAvatar());
            userStaffCpRelationDTO.setType(wxCpExternalContactInfo.getExternalContact().getType());
            userStaffCpRelationDTO.setGender(wxCpExternalContactInfo.getExternalContact().getGender());
            userStaffCpRelationDTO.setStatus(1);//绑定关系 1-绑定 2- 解绑中
            userStaffCpRelationDTO.setContactChangeType(wxMessage.getChangeType());
            userStaffCpRelationDTO.setCorpName(StrUtil.isNotEmpty(wxCpExternalContactInfo.getExternalContact().getCorpName())?wxCpExternalContactInfo.getExternalContact().getCorpName():null);
            userStaffCpRelationDTO.setCorpFullName(StrUtil.isNotEmpty(wxCpExternalContactInfo.getExternalContact().getCorpFullName())?wxCpExternalContactInfo.getExternalContact().getCorpFullName():null);
            if(wxMessage.getChangeType().equals( WxCpConsts.ExternalContactChangeType.ADD_EXTERNAL_CONTACT)){
                userStaffCpRelationDTO.setAutoType(1);
            }else if(wxMessage.getChangeType().equals( WxCpConsts.ExternalContactChangeType.ADD_HALF_EXTERNAL_CONTACT)){
                userStaffCpRelationDTO.setAutoType(0);
            }
            CpCodeChannel cpCodeChannel=null;
            if(CharSequenceUtil.isNotBlank(wxMessage.getState())){
                //获取渠道来源
                cpCodeChannel=cpCodeChannelService.getBySourceState(wxMessage.getState());
                if(Objects.nonNull(cpCodeChannel)){
                    userStaffCpRelationDTO.setCodeChannelId(cpCodeChannel.getId().toString());
                    userStaffCpRelationDTO.setCodeGroupId(getGroupCodeId(cpCodeChannel));
                }
            }
            //备注信息
            FollowedUser followedUser=this.getFollowUserByUserId(wxMessage.getUserId(),wxCpExternalContactInfo);
            log.info("userId:{} externalUserId:{} 外部联系人备注信息:{}", wxMessage.getUserId(),wxMessage.getExternalUserId(),JSON.toJSONString(followedUser));
            if(Objects.nonNull(followedUser)){
                //备注信息
                userStaffCpRelationDTO.setCpRemark(followedUser.getRemark());
                userStaffCpRelationDTO.setCpDescription(followedUser.getDescription());
                userStaffCpRelationDTO.setCpRemarkMobiles(ArrayUtil.isNotEmpty(followedUser.getRemarkMobiles())?JSON.toJSONString(followedUser.getRemarkMobiles()):null);
                userStaffCpRelationDTO.setCpState(followedUser.getState());
                userStaffCpRelationDTO.setCpOperUserId(followedUser.getOperatorUserId());
                userStaffCpRelationDTO.setCpAddWay(followedUser.getAddWay());
                userStaffCpRelationDTO.setCpCreateTime(WechatUtils.formatDate(followedUser.getCreateTime().toString()));
                userStaffCpRelationDTO.setCpRemarkCorpName(followedUser.getRemarkCorpName());

            }
            //处理好友关系表数据
            userStaffCpRelationFeignClient.qiWeiFriendsChange(userStaffCpRelationDTO);

            /**
             *【处理渠道逻辑】
             * 1. 渠道活码
             * 2. 自动拉群
             */
            if(wxMessage.getChangeType().equals(WxCpConsts.ExternalContactChangeType.ADD_EXTERNAL_CONTACT)
            || wxMessage.getChangeType().equals(WxCpConsts.ExternalContactChangeType.ADD_HALF_EXTERNAL_CONTACT)){
                SendWelcomeDTO dto=new SendWelcomeDTO();
                dto.setCpCodeChannel(cpCodeChannel);
                dto.setWxMessage(wxMessage);
                dto.setWxCpExternalContactInfo(wxCpExternalContactInfo);
                dto.setFollowedUser(followedUser);
                cpExternalHandler.haneleExternalState(dto);
            }
        }catch (Exception e){
            log.error("外部联系人变更事件数据处理失败: {}",e);
        }
    }

    private Long getGroupCodeId(CpCodeChannel cpCodeChannel){
        //渠道分组ID
        if(cpCodeChannel.getSourceFrom()== CodeChannelEnum.CHANNEL_CODE.getValue()){
            CpStaffCodePlus cpStaffCode=staffCodeService.getById(cpCodeChannel.getSourceId());
            if(Objects.nonNull(cpStaffCode)){
                return cpStaffCode.getGroupId();
            }
        }
        if(cpCodeChannel.getSourceFrom()== CodeChannelEnum.AUTO_GROUP_CODE.getValue()){
            CpAutoGroupCode autoGroupCode=autoGroupCodeService.getById(cpCodeChannel.getSourceId());
            if(Objects.nonNull(autoGroupCode)){
                return autoGroupCode.getGroupId();
            }
        }
        return null;
    }

    /**
     * 根据当前员工获取外部联系人对应备注信息
     * @param userId
     * @param wxCpExternalContactInfo
     */
    private FollowedUser getFollowUserByUserId(String userId,WxCpExternalContactInfo wxCpExternalContactInfo){
        if(CollUtil.isNotEmpty(wxCpExternalContactInfo.getFollowedUsers())){
            Map<String,FollowedUser> followedUserMap=LambdaUtils.toMap(wxCpExternalContactInfo.getFollowedUsers(), FollowedUser::getUserId);
            return followedUserMap.get(userId);
        }
        return null;
    }

    //补全丢失的客户或者unionId
    @Override
    public void update(WxCpXmlMessage wxMessage,WxCpExternalContactInfo wxCpExternalContactInfo) {
        create(wxMessage,wxCpExternalContactInfo);
    }

    @Override
    public void delete(WxCpXmlMessage wxMessage, String externalUserId,Integer status) {

        try {
            String userId=wxMessage.getUserId();
            UserStaffCpRelationListVO relationListVO=getUserStaffCpRelationListVO(userId,externalUserId);
            log.info("----cust delete -------userId：【"+userId+"】  externalUserId:【"+externalUserId+"】");
            UserStaffCpRelationDTO userStaffCpRelationDTO = new UserStaffCpRelationDTO();
            //员工的企业微信id
            userStaffCpRelationDTO.setQiWeiStaffId(userId);
            //客户外部联系人id
            userStaffCpRelationDTO.setQiWeiUserId(externalUserId);
            userStaffCpRelationDTO.setStatus(status);
            userStaffCpRelationDTO.setContactChangeType(wxMessage.getChangeType());
//            qiWeiFriendsSyncTemplate.syncSend(userStaffCpRelationDTO);
            userStaffCpRelationFeignClient.qiWeiFriendsChange(userStaffCpRelationDTO);
            if(status==2){//单方面解除绑定
//                wxCpPushService.lossUserNotify(userId,externalUserId);
                if(Objects.nonNull(relationListVO)){
                    NotifyMsgTemplateDTO.FollowLoss followLoss=NotifyMsgTemplateDTO.FollowLoss.builder()
                            .nickName(relationListVO.getQiWeiNickName())
                            .userName(StrUtil.isNotBlank(relationListVO.getCpRemark())?relationListVO.getCpRemark():relationListVO.getQiWeiNickName())
                            .deleteDate(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"))
                            .build();
                    wxCpPushService.lossUserNotify(NotifyMsgTemplateDTO.builder().userId(""+relationListVO.getId()).qiWeiStaffId(userId).followLoss(followLoss).build());
                }

            }
        }catch (Exception e){
            log.error("",e);
        }
    }

    private UserStaffCpRelationListVO getUserStaffCpRelationListVO(String staffUserId,String externalUserId){
        try {

            ServerResponseEntity<UserStaffCpRelationListVO> relationResponseEntity=userStaffCpRelationFeignClient.getUserInfoByQiWeiUserId(staffUserId,externalUserId);
            return relationResponseEntity.getData();
        }catch (Exception e){
            log.error("getUserStaffCpRelationListVO error:{}",e);
        }
        return null;
    }

//    @Override
//    public void sendWelcomeMsg(String welcomeCode, String state, String userId,String userName) {
//        try{
//
//            /**
//             * TODO 需要区分渠道来源：
//             * 1. 渠道活码【入群欢迎语+附件】
//             * 2. 自动拉群【入群欢迎语+自动拉群引流附件-群活码列表】
//             * 3. 标签建群【入群欢迎语+自动拉群引流附件-群活码列表】
//             */
//
//            log.info("----cust sendWelcomeMsg -------userId：【"+userId+"】  welcomeCode:【"+welcomeCode+"】 state:【"+state+"】");
//            ServerResponseEntity<StaffVO> response =  staffFeignClient.getStaffByQiWeiUserId(userId);
//            log.info("----cust sendWelcomeMsg getStaffByQiWeiUserId rep : {}", JSONObject.toJSONString(response));
//            if(response.getData()!=null) {
//                //添加的员工信息
//                StaffVO staffVO = response.getData();
//                WxCpWelcomeMsg wxCpWelcomeMsg = getWxCpStaffCodeMsg(staffVO,state,userName);
//                log.info("----cust sendWelcomeMsg ---staff code----：【"+Json.toJsonString(wxCpWelcomeMsg) +"】 ");
//                if(wxCpWelcomeMsg==null){
//                    wxCpWelcomeMsg  =  getWxCpWelcomeMsg(staffVO,state,userName);
//                }
//                if(wxCpWelcomeMsg!=null) {
//                    wxCpWelcomeMsg.setWelcomeCode(welcomeCode);
//                    log.info("----cust sendWelcomeMsg ---groble----：【"+wxCpWelcomeMsg.toJson() +"】 ");
//                    WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).getExternalContactService().sendWelcomeMsg(wxCpWelcomeMsg);
//                }
//            }
//            log.info("----cust sendWelcomeMsg -------：【SUCCESS】");
//        }catch (Exception e){
//            log.error("发送欢迎语失败", e);
//        }
//    }


//    @Override
//    public void addTagToCust(String state, String userId,String externalUserId ) {
//        try{
//            log.info("----cust addTagToCust -------userId：【"+userId+"】  state:【"+state+"】");
//            ServerResponseEntity<StaffVO> response =  staffFeignClient.getStaffByQiWeiUserId(userId);
//            log.info("----cust addTagToCust getStaffByQiWeiUserId rep : {}", JSONObject.toJSONString(response));
//            if(response.getData()!=null) {
//                //添加的员工信息
//                StaffVO staffVO = response.getData();
//                //从员工活码免活码登陆/或者验证登陆
//                if(StringUtils.isNotEmpty(state)) {
//                    CpStaffCode staffCode = staffCodeService.selectByStaffIdAndState(staffVO.getId(), state);
//                    if(staffCode!=null && StringUtils.isNotEmpty(staffCode.getTags())){
//                        List<StaffCodeTagDTO> tagList =  Json.parseArray(staffCode.getTags(),StaffCodeTagDTO[].class);
//                        UserTageAddDto userTageAddDto = new UserTageAddDto();
//                        userTageAddDto.setQiWeiUserId(externalUserId);
//                        userTageAddDto.setUserTagIds(tagList.parallelStream().map(item->item.getTagId()).collect(Collectors.toList()));
//                        userTagClient.addUserTags(userTageAddDto);
//                    }
//                }
//            }
//            log.info("----cust addTagToCust -------：【SUCCESS】");
//        }catch (Exception e){
//            log.error("客户加标签失败",e);
//        }
//    }

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
    private  WxCpWelcomeMsg  getWxCpStaffCodeMsg(StaffVO staffVO,String state,String userName){
        //从员工活码免活码登陆/或者验证登陆
        if(StringUtils.isNotEmpty(state)){
            CpStaffCodePlus staffCode = staffCodeService.selectByStaffIdAndState(staffVO.getId(),state);
            if(staffCode!=null){
                String slogan=getSlogan(staffVO,staffCode.getSlogan(),userName);
                Text text =  new Text();
                text.setContent(slogan);
//                text.setContent((staffCode.getSlogan()));
                WxCpWelcomeMsg sendWelcomeMsg = new WxCpWelcomeMsg();
                sendWelcomeMsg.setText(text);
                CpWelcomeAttachment attachment =attachmentService.selectOneByWelId(staffCode.getId(), OriginType.STAFF_CODE.getCode());
                if(Objects.nonNull(attachment)){
                    AttachmentExtDTO attachmentExtDTO = Json.parseObject(attachment.getData(), AttachmentExtDTO.class);
                    setMiniProgram(attachmentExtDTO,staffVO);
                    sendWelcomeMsg.setAttachments(Lists.newArrayList(attachmentExtDTO.getAttachment()));
                }
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
    private  WxCpWelcomeMsg  getWxCpWelcomeMsg(StaffVO staffVO,String state,String userName){
        //非员工活码进入 根据添加客户的员工的店来查
        CpWelcome welcome =  welcomeService.getWelcomeByWeight(staffVO.getStoreId());
        if(welcome==null){
            log.info("商店【"+staffVO.getStoreId()+"】尚未配置欢迎语");
            return null;
        }
        String slogan=getSlogan(staffVO,welcome.getSlogan(),userName);
        CpWelcomeAttachment welcomeAttachment =attachmentService.selectOneByWelId(welcome.getId(), OriginType.WEL_CONFIG.getCode());
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

    private String getSlogan(StaffVO staffVO,String slogan,String userName){
        log.info("---getSlogan--原始slogan->【{}】  staffVO->【{}】 userName->【{}】",slogan,staffVO,userName);
        try {
            PlaceholderResolver defaultResolver = PlaceholderResolver.getDefaultResolver();
            Map<String,Object> keyContent=new LinkedHashMap<>();
            //获取员工信息
            if(defaultResolver.checkVariables(slogan,"${staffName}")){
                keyContent.put("staffName", StrUtil.isNotBlank(staffVO.getStaffName())?staffVO.getStaffName():"");
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
    private void setMiniProgram(AttachmentExtDTO attachmentExtDTO,StaffVO staffVO){
        if(attachmentExtDTO.getAttachment().getMiniProgram()!=null){
            MiniProgram miniProgram =  attachmentExtDTO.getAttachment().getMiniProgram();
            TentacleContentDTO tentacleContent = new TentacleContentDTO();
            tentacleContent.setBusinessId(staffVO.getId());
            tentacleContent.setTentacleType(4);
            ServerResponseEntity<TentacleContentVO> tenInfo  = tentacleFeignClient.findOrCreateByContent(tentacleContent);
            if(tenInfo!=null && tenInfo.getData()!=null) {
                String t = NumberTo64.to64(Long.parseLong(tenInfo.getData().getTentacleNo()));
                if(miniProgram.getPage().indexOf("?")>0){
                    miniProgram.setPage(miniProgram.getPage() + "&t=" + t+"&s="+NumberTo64.to64(staffVO.getStoreId()));
                }else {
                    miniProgram.setPage(miniProgram.getPage() + "?t=" + t + "&s=" + NumberTo64.to64(staffVO.getStoreId()));
                }
            }
        }
    }
}
