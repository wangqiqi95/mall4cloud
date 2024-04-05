package com.mall4j.cloud.biz.service.cp.handler.plus;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.biz.vo.MsgAttachment;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.user.crm.model.UpdateTagModel;
import com.mall4j.cloud.api.user.dto.UserStaffCpRelationDTO;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.*;
import com.mall4j.cloud.biz.dto.cp.wx.ChannelCodeWelcomeDTO;
import com.mall4j.cloud.biz.dto.cp.wx.externalcontact.SendWelcomeDTO;
import com.mall4j.cloud.biz.mapper.cp.CpStaffCodePlusMapper;
import com.mall4j.cloud.biz.mapper.cp.WelcomeMapper;
import com.mall4j.cloud.biz.model.cp.*;
import com.mall4j.cloud.biz.service.cp.MaterialService;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.LambdaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.external.WxCpUpdateRemarkRequest;
import me.chanjar.weixin.cp.bean.external.WxCpWelcomeMsg;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;
import me.chanjar.weixin.cp.bean.external.msg.Text;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 渠道活码
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CpStaffCodePlusHandler {

    private final CpStaffCodePlusMapper staffCodeMapper;
    private final WelcomeMapper welcomeMapper;
    private final MaterialService materialService;
//    private final CrmManager crmManager;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;

    /**
     * 发送渠道活码欢迎语
     * @param dto
     */
    public void sendWelcomeMsgByStaffCode(SendWelcomeDTO dto){
        try{
            WxCpXmlMessage wxMessage=dto.getWxMessage();
            WxCpExternalContactInfo wxCpExternalContactInfo=dto.getWxCpExternalContactInfo();
            CpCodeChannel cpCodeChannel=dto.getCpCodeChannel();
            StaffVO staffVO=dto.getStaffVO();

            String welcomeCode=wxMessage.getWelcomeCode();//欢迎语CODE
            String userName= Objects.nonNull(wxCpExternalContactInfo)?wxCpExternalContactInfo.getExternalContact().getName():"";//客户昵称

            if(Objects.nonNull(cpCodeChannel)){
                CpStaffCodePlus cpStaffCode=staffCodeMapper.getById(Long.parseLong(cpCodeChannel.getSourceId()));
                if(Objects.nonNull(cpStaffCode)){
                    if(cpStaffCode.getWelcomeType()>0){
                        dto.setWelcomeType(cpStaffCode.getWelcomeType());
                        log.info("渠道活码----不发送渠道欢迎语-welcomeType:{}",cpStaffCode.getWelcomeType());
                    }else{
                        try {
                            WxCpWelcomeMsg wxCpWelcomeMsg=getStaffCodeWelcome(staffVO,cpStaffCode,userName);
                            if(Objects.nonNull(wxCpWelcomeMsg)){
                                wxCpWelcomeMsg.setWelcomeCode(welcomeCode);
                                log.info("渠道活码----发送渠道欢迎语----{}",wxCpWelcomeMsg.toJson());
                                WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().sendWelcomeMsg(wxCpWelcomeMsg);
                                log.info("渠道活码----发送渠道欢迎语 success----{}",wxCpWelcomeMsg.toJson());
                            }
                        }catch (Exception e){
                            log.info("渠道活码----发送渠道欢迎语 失败----{} {}",e,e.getMessage());
                        }
                    }
                    //客户打标签逻辑
                    if(StrUtil.isNotEmpty(cpStaffCode.getTags())){
                        addTagToCust(wxCpExternalContactInfo.getExternalContact().getUnionId(),cpStaffCode.getTags());
                    }
                    //自动打备注
                    if((cpStaffCode.getAutoRemarkState()==1 && StrUtil.isNotEmpty(cpStaffCode.getAutoRemark()))
                    ||
                            (cpStaffCode.getAutoDescriptionState()==1 && StrUtil.isNotEmpty(cpStaffCode.getAutoDescription()))){
                        addRemark(wxCpExternalContactInfo.getExternalContact().getName(),
                                wxCpExternalContactInfo.getExternalContact().getExternalUserId(),
                                wxMessage.getUserId(),
                                cpStaffCode.getAutoRemark(),
                                cpStaffCode.getAutoDescription());
                    }
                    //修改客户通过好友验证状态
                    if(Objects.nonNull(cpStaffCode.getAuthType())){
                        updateAutoType(wxCpExternalContactInfo.getExternalContact().getExternalUserId(),
                                wxMessage.getUserId(),cpStaffCode.getAuthType());
                    }
                }
            }
            log.info("----cust sendWelcomeMsg -------：【SUCCESS】");
        }catch (Exception e){
            log.error("处理渠道活码逻辑失败 : {}", e);
        }
    }

    /**
     * TODO 给客户打标签
     */
    public void addTagToCust(String externalUserUnionId ,String tags) {
        try{
            UpdateTagModel updateTagModel=new UpdateTagModel();
            updateTagModel.setQiWeiUserUnionIds(ListUtil.toList(externalUserUnionId));
            updateTagModel.setTags(tags);
            //待实现
//            crmManager.updateUserTagByCrm(updateTagModel, BuildTagFromEnum.AUTO_GROUP.getCode());
            log.info("----活到活码 cust addTagToCust -------：【SUCCESS】");
        }catch (Exception e){
            log.error("客户加标签失败",e);
        }
    }

    /**
     * 给客户修改备注
     * @param nickName
     * @param externalUserId
     * @param userId
     * @param remark
     */
    private void addRemark(String nickName,String externalUserId,String userId,String remark,String description){
        try{
            WxCpUpdateRemarkRequest remarkRequest=new WxCpUpdateRemarkRequest();
            remarkRequest.setExternalUserId(externalUserId);
            remarkRequest.setUserId(userId);
            remarkRequest.setRemark(remark+""+nickName);
            remarkRequest.setDescription(""+description);
            WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().updateRemark(remarkRequest);
            //更新客户备注
            UserStaffCpRelationDTO userStaffCpRelationDTO=new UserStaffCpRelationDTO();
            userStaffCpRelationDTO.setQiWeiStaffId(userId);
            userStaffCpRelationDTO.setQiWeiUserId(externalUserId);
            userStaffCpRelationDTO.setCpRemark(remark+""+nickName);
            userStaffCpRelationDTO.setCpDescription(""+description);
            userStaffCpRelationFeignClient.updateUserStaffCpRelationRemark(userStaffCpRelationDTO);
            log.info("----自动打备注 success----{}",remarkRequest.toJson());
        }catch (WxErrorException e){
            log.error("自动打备注失败",e);
        }

    }

    /**
     * 修改客户通过好友验证状态
     */
    private void updateAutoType(String externalUserId,String userId,Integer autoType){
        try{
            UserStaffCpRelationDTO userStaffCpRelationDTO=new UserStaffCpRelationDTO();
            userStaffCpRelationDTO.setQiWeiStaffId(userId);
            userStaffCpRelationDTO.setQiWeiUserId(externalUserId);
            userStaffCpRelationDTO.setAutoType(autoType);
            userStaffCpRelationFeignClient.updateUserStaffCpRelationAutoType(userStaffCpRelationDTO);
            log.info("----修改客户通过好友验证状态 success----{}",JSON.toJSONString(userStaffCpRelationDTO));
        }catch (Exception e){
            log.error("修改客户通过好友验证状态",e);
        }

    }


    /**
     * 渠道活码欢迎语
     * @param staffVO
     * @param cpStaffCode
     * @param userName
     * @return
     */
    private WxCpWelcomeMsg getStaffCodeWelcome(StaffVO staffVO, CpStaffCodePlus cpStaffCode, String userName){
        //获取附件
        List<ChannelCodeWelcomeDTO> welcomeDTOS=welcomeMapper.selectWelcomes(cpStaffCode.getId());
        //优先取分时段欢迎语
        List<ChannelCodeWelcomeDTO> timeWelcome=welcomeDTOS.stream().filter(item->item.getSourceFrom()==2).collect(Collectors.toList());

        ChannelCodeWelcomeDTO welcomeDTO=null;
        if(CollUtil.isNotEmpty(timeWelcome)){
            //获取当前是星期几
            /**
             * 周日-1/周1-2/周2-3/周3-4/周4-5/周5-6/周6-7
             */
            Date now=new Date();
            long nowTime=DateUtil.parse(DateUtil.format(now,"HH:mm"),"HH:mm").getTime();
            int dayWeek=DateUtil.dayOfWeek(now);//当前周几
            for (ChannelCodeWelcomeDTO channelCodeWelcomeDTO : timeWelcome) {
                //匹配周条件
                Map<String,String> timesMap=LambdaUtils.toMap(Arrays.asList(channelCodeWelcomeDTO.getTimeCycle().split(",")), item->item);
                if(!timesMap.containsKey(String.valueOf(dayWeek))){
                    continue;
                }
                //匹配时间段条件
                Long startTime=DateUtil.parse(channelCodeWelcomeDTO.getTimeStart(),"HH:mm").getTime();
                Long endTime=DateUtil.parse(channelCodeWelcomeDTO.getTimeEnd(),"HH:mm").getTime();
                if(nowTime<startTime || nowTime>endTime){//时间未到/时间超出
                    continue;
                }
                welcomeDTO=channelCodeWelcomeDTO;
                break;
            }

        }
//        else{//默认渠道欢迎语
//            timeWelcome=welcomeDTOS.stream().filter(item->item.getSourceFrom()==1).collect(Collectors.toList());
//            if(CollUtil.isNotEmpty(timeWelcome)){
//                welcomeDTO=timeWelcome.get(0);
//            }
//        }
        //分时段未匹配到取默认
        if(Objects.isNull(welcomeDTO)){
            timeWelcome=welcomeDTOS.stream().filter(item->item.getSourceFrom()==1).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(timeWelcome)){
                welcomeDTO=timeWelcome.get(0);
            }
        }
        String slogan=(Objects.nonNull(welcomeDTO)&&StrUtil.isNotEmpty(welcomeDTO.getSlogan()))?welcomeDTO.getSlogan():cpStaffCode.getSlogan();
        slogan= SlognUtils.getSlogan(staffVO.getStaffName(),slogan,userName);
        WxCpWelcomeMsg sendWelcomeMsg = new WxCpWelcomeMsg();
        if(StrUtil.isNotEmpty(slogan)){
            Text text =  new Text();
            text.setContent(slogan);
            sendWelcomeMsg.setText(text);
        }
        log.info("渠道活码欢迎语 welcomeDTO:【{}】 slogan:【{}】", JSON.toJSONString(welcomeDTO),slogan);
        //处理附件信息
        if(Objects.isNull(welcomeDTO) || CollUtil.isEmpty(welcomeDTO.getAttachMents())){
            log.info("渠道活码欢迎语 未获取到附件 :{} ", JSON.toJSONString(sendWelcomeMsg));
            return sendWelcomeMsg;
        }
        List<Attachment> attachments=new ArrayList<>();
        for (AttachMentBaseDTO attachMentBaseDTO : welcomeDTO.getAttachMents()) {
            if(attachMentBaseDTO.getMaterialId()!=null){
                MsgAttachment attachment = materialService.useAndFindAttachmentById(attachMentBaseDTO.getMaterialId(),staffVO.getId());
                attachments.add(attachment);
            }else{
                AttachmentExtDTO attachmentExtDTO = Json.parseObject(attachMentBaseDTO.getData(), AttachmentExtDTO.class);
                attachments.add(attachmentExtDTO.getAttachment());
            }
        }
        sendWelcomeMsg.setAttachments(attachments);
        log.info("渠道活码欢迎语 :{} ", JSON.toJSONString(sendWelcomeMsg));
        return sendWelcomeMsg;
    }


}
