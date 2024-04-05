package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.user.feign.UserStaffCpRelationFeignClient;
import com.mall4j.cloud.api.user.vo.UserStaffCpRelationListVO;
import com.mall4j.cloud.api.biz.dto.cp.NotifyMsgTemplateDTO;
import com.mall4j.cloud.biz.mapper.cp.NoticeMapper;
import com.mall4j.cloud.api.biz.constant.cp.NotityTypeEunm;
import com.mall4j.cloud.biz.wx.cp.constant.StatusType;
import com.mall4j.cloud.biz.config.WxCpMiniProgramConfig;
import com.mall4j.cloud.biz.dto.cp.wx.NotifyDto;
import com.mall4j.cloud.biz.model.cp.NotifyConfig;
import com.mall4j.cloud.biz.service.cp.NotifyConfigService;
import com.mall4j.cloud.biz.service.cp.WxCpPushService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 消息发送
 *
 * @author hwy
 * @date 2022-02-10 18:25:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxCpPushServiceImpl implements WxCpPushService {
    private final WxCpService wxCpService;
    private final NotifyConfigService notifyConfigService;
    private final UserStaffCpRelationFeignClient userStaffCpRelationFeignClient;
    private final static String MEMBER_URL = "";
    private final static String MEMBER_LIST_URL = "";
    @Autowired
    private NoticeMapper noticeMapper;
    /**
     * 全部未添加企业微信的好友
     */
    @Override
    public void unAddQwNotify(Integer nums, String staffUserId) {
        String url = "";
        msgNotify(staffUserId,NotityTypeEunm.UNADD_QW,nums.toString(), url,"点击查看未添加企微好友的会员");
    }
    /**
     * 全部未注册的好友
     */
    @Override
    public void unRegisterMemberNotify(Integer nums, String staffUserId) {
        String url = "";
        msgNotify(staffUserId,NotityTypeEunm.UNREGISTER_MEMBER,nums.toString(), url,"点击查看未注册的好友");
    }
    /**
     * 新加的未注册好友
     */
    @Override
    public void inviteUserNotify(Long userId, String staffUserId) {
        String url = "";
        msgNotify(staffUserId,NotityTypeEunm.INVITE_USER,null, url,"点击查看好友详情页");
    }

    /**
     * TODO 好友流失提醒
     */
    @Override
    public void deleteUserNotify(String staffUserId,String externalUserId) {
        try {
            log.info("好友流失提醒1->{} {}",staffUserId,externalUserId);
            String url = "packageGuideShop/pages/member/member-detail/member-detail?userId=";
            ServerResponseEntity<UserStaffCpRelationListVO> userResp = userStaffCpRelationFeignClient.getUserInfoByQiWeiUserId(staffUserId, externalUserId);
            if (userResp != null && userResp.getData() != null) {
                UserStaffCpRelationListVO userStaffCpRelationListVO = userResp.getData();
                String userName = userStaffCpRelationListVO.getQiWeiNickName();
                if(userStaffCpRelationListVO.getUserId()!=null){
                    url = url+userStaffCpRelationListVO.getUserId();
                }else {
                    url = "";
                }
                log.info("好友流失提醒2->{} {}", JSON.toJSONString(userResp.getData()));
                msgNotify(staffUserId, NotityTypeEunm.DELETE_USER, userName, url, "点击查看好友详情页");
            }
        }catch (Exception e){
            log.error("--好友流失提醒 通知失败--{}",e);
        }
    }

    /**
     * 会员注册成功提醒
     */
    @Override
    public void userRegisterSuccessNotify(String staffUserId,Long userId) {
        String url = "packageGuideShop/pages/member/member-detail/member-detail?userId="+userId;
        msgNotify(staffUserId,NotityTypeEunm.USER_REGISTER_SUCCESS,null, url,"点击查看会员详情页");
    }
    /**
     * 服务变更提醒
     */
    @Override
    public void serviceChangeNotify(String qiWeiStaffId,String qiWeiUserId) {
        String url = "packageGuideShop/pages/member/member-detail/member-detail?userId=";
        try {
            ServerResponseEntity<UserStaffCpRelationListVO> userResp = userStaffCpRelationFeignClient.getUserInfoByQiWeiUserId(qiWeiStaffId, qiWeiUserId);
            if (userResp != null && userResp.getData() != null) {
                UserStaffCpRelationListVO userStaffCpRelationListVO = userResp.getData();

                String userName = userStaffCpRelationListVO.getQiWeiNickName();
                if(userStaffCpRelationListVO.getUserId()!=null){
                    url = url+userStaffCpRelationListVO.getUserId();
                }else {
                    url = "";
                }
                msgNotify(qiWeiStaffId, NotityTypeEunm.SERVICE_CHANGE, userName, url, "点击查看会员详情页");
            }
        }catch (Exception e){
            log.error("",e);
        }
    }

    private void msgNotify(String staffUserId,NotityTypeEunm type,String paramVal,String url,String btnText){
        NotifyConfig notifyConfig = notifyConfigService.getNotifyByType(type);
        log.info("--msgNotify--{}",JSON.toJSONString(notifyConfig));
        if(notifyConfig!=null&&notifyConfig.getStatus()== StatusType.YX.getCode()) {
            /*if(type==NotityTypeEunm.UNADD_QW||type==NotityTypeEunm.INVITE_USER||type==NotityTypeEunm.UNREGISTER_MEMBER){
                notifyConfig.getPushTime();
            }*/
            NotifyDto notifyDto = new NotifyDto();
            notifyDto.setPushDate(DateUtils.dateToStrYmd(new Date()));
            log.info(paramVal+"===="+notifyConfig.getParamName());
            if(StringUtils.isNotEmpty(notifyConfig.getParamName())){
                notifyDto.setContent(notifyConfig.getMsgContent().replace("{"+notifyConfig.getParamName()+"}",paramVal));
            }
            notifyDto.setTitle(notifyConfig.getMsgName());
            notifyDto.setStaffUserId(staffUserId);
            notifyDto.setUrl(url);
            notifyDto.setBtnText(btnText);
            createMiniprogramPush(notifyDto);
        }
    }


    public boolean createPush(NotifyDto notifyDto)  {
        try {
            log.info("发送应用消息 入参：{}",JSON.toJSONString(notifyDto));
            WxCpMessage wxCpMessage =WxCpMessage.TEXTCARD()
                    .title(notifyDto.getTitle())
                    .url(notifyDto.getUrl())
                    .agentId(wxCpService.getWxCpConfigStorage().getAgentId())
                    .toUser(notifyDto.getStaffUserId())
                    .btnTxt(notifyDto.getBtnText())
                    .description("<div class=\"gray\">"+notifyDto.getPushDate()+"</div> <div class=\"normal\">"+notifyDto.getContent()+"</div>")
                    .build();
            wxCpMessage.setEnableDuplicateCheck(true);//表示是否开启重复消息检查，0表示否，1表示是，默认0
            wxCpMessage.setDuplicateCheckInterval(1800);//	表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时
            WxCpMessageSendResult result =  wxCpService.getMessageService().send(wxCpMessage);
            log.info("发送应用消息 结果：{}",JSON.toJSONString(result));
            return result.getErrCode()==0;
        }catch (WxErrorException wx){
            log.error("",wx);
            return false;
        }
    }


    @Override
    public boolean createMiniprogramPush(NotifyDto notifyDto)  {
        try {
            log.error("createMiniprogramPush 入参 --> {}",JSON.toJSONString(notifyDto));
            Map<String,String> params  = new HashMap<String,String>(2);
            params.put("提醒内容",notifyDto.getContent());
            params.put("提醒说明",notifyDto.getBtnText());
            WxCpMessage wxCpMessage =WxCpMessage.newMiniProgramNoticeBuilder()
                    .appId(WxCpMiniProgramConfig.MINI_PROGRAM_APP_ID)
                    .title(notifyDto.getTitle())
                    .description(notifyDto.getPushDate())
                    .agentId(wxCpService.getWxCpConfigStorage().getAgentId())
                    .page(notifyDto.getUrl())
                    .toUser(notifyDto.getStaffUserId())
                    .contentItems(params)
                    .emphasisFirstItem(notifyDto.isEmphasisFirstItem())
                    .build();
            WxCpMessageSendResult result =  wxCpService.getMessageService().send(wxCpMessage);
            log.error("createMiniprogramPush result --> {}",result.toString());
            return result.getErrCode()==0;
        }catch (WxErrorException wx){
            log.error("createMiniprogramPush error --> {}",wx);
            return false;
        }
    }

    @Override
    public void lossUserNotify(NotifyMsgTemplateDTO dto) {
        try {
//            noticeMsg(qiWeiStaffId,userName,"","",0,NotityTypeEunm.UNADD_QW);

            dto.setTypeEunm(NotityTypeEunm.UNADD_QW);
            noticeMsgTemplate(dto);
        }catch (Exception e){
            log.error("",e);
        }
    }

    /**
     * 跟进提醒
     * @param dto
     */
    @Override
    public void followNotify(NotifyMsgTemplateDTO dto) {
        try{
//            noticeMsg(qiWeiStaffId,userName,staffName,"",0,NotityTypeEunm.UNREGISTER_MEMBER);

            dto.setTypeEunm(NotityTypeEunm.UNREGISTER_MEMBER);
            noticeMsgTemplate(dto);
        }catch (Exception e){
            log.error("",e);
        }
    }

    @Override
    public void materialNotify(NotifyMsgTemplateDTO dto) {
        try{
//            noticeMsg(qiWeiStaffId,userName,"","",time,NotityTypeEunm.INVITE_USER);
            dto.setTypeEunm(NotityTypeEunm.INVITE_USER);
            noticeMsgTemplate(dto);
        }catch (Exception e){
            log.error("",e);
        }
    }

    /**
     * 敏感词命中
     * @param dto
     */
    @Override
    public void mateNotify(NotifyMsgTemplateDTO dto) {
        try{
//            noticeMsg(qiWeiStaffId,userName,"",mate,0,NotityTypeEunm.SUB);
            dto.setTypeEunm(NotityTypeEunm.SUB);
            noticeMsgTemplate(dto);
        }catch (Exception e){
            log.error("",e);
        }
    }

    /**
     * 回复超时提醒
     */
    @Override
    public void timeOutNotify(NotifyMsgTemplateDTO dto) {
        try{
//            noticeMsg(qiWeiStaffId,userName,"","",time,NotityTypeEunm.USER_REGISTER_SUCCESS);
            dto.setTypeEunm(NotityTypeEunm.USER_REGISTER_SUCCESS);
            noticeMsgTemplate(dto);
        }catch (Exception e){
            log.error("",e);
        }
    }

    @Override
    public void phoneTaskNotify(NotifyMsgTemplateDTO dto) {
        try{
//            noticeMsg(qiWeiStaffId,userName,"","",time,NotityTypeEunm.USER_REGISTER_SUCCESS);
            dto.setTypeEunm(NotityTypeEunm.CP_PHONE_TASK);
            noticeMsgTemplate(dto);
        }catch (Exception e){
            log.error("",e);
        }
    }

    private void noticeMsg(String qiWeiStaffId,String userName,String staffName,String mate,Integer time,NotityTypeEunm type){
        //NoticeVO vo = noticeMapper.getByMsgType(type);
        NotifyConfig notifyConfig = notifyConfigService.getNotifyByType(type);
        NotifyDto notifyDto = new NotifyDto();
        notifyDto.setPushDate(DateUtils.dateToStrYmd(new Date()));
        notifyDto.setTitle(notifyConfig.getMsgName());
        notifyDto.setUrl(notifyConfig.getMsgUrl());
        if(NotityTypeEunm.UNADD_QW.equals(type)){
            notifyDto.setContent(notifyConfig.getMsgContent().replace("{客户昵称}",userName));
        }else if(NotityTypeEunm.UNREGISTER_MEMBER.equals(type)){
            notifyDto.setContent(notifyConfig.getMsgContent().replace("{员工姓名}",staffName).replace("{客户昵称}",userName));
        }else if(NotityTypeEunm.INVITE_USER.equals(type)){
            notifyDto.setContent(notifyConfig.getMsgContent().replace("{客户昵称}",userName).replace("{变量N分钟}",String.valueOf(time)));
        }else if(NotityTypeEunm.SUB.equals(type)){
            notifyDto.setContent(notifyConfig.getMsgContent().replace("{客户昵称}",userName).replace("{敏感词}",mate));
        }else{
            notifyDto.setContent(notifyConfig.getMsgContent().replace("{客户昵称}",userName).replace("{变量N分钟}",String.valueOf(time)));
        }
        notifyDto.setStaffUserId(qiWeiStaffId);
        createPush(notifyDto);
    }

    private void noticeMsgTemplate(NotifyMsgTemplateDTO dto){
        NotityTypeEunm type=dto.getTypeEunm();
        String qiWeiStaffId=dto.getQiWeiStaffId();
        NotifyConfig notifyConfig = notifyConfigService.getNotifyByType(type);
        if(Objects.isNull(notifyConfig)){
            log.error("noticeMsgTemplate 发送应用失败，未获取到消息模版，入参：{}",JSON.toJSONString(dto));
            return;
        }
        if(notifyConfig.getStatus()==0){
            log.error("noticeMsgTemplate 发送应用失败，消息模版未启用-模版信息：{}，入参：{}",JSON.toJSONString(notifyConfig),JSON.toJSONString(dto));
            return;
        }
        log.info("获取到消息提醒模版信息:{}",JSON.toJSONString(notifyConfig));
        NotifyDto notifyDto = new NotifyDto();
        notifyDto.setPushDate(DateUtils.dateToStrYmd(new Date()));
        notifyDto.setTitle(notifyConfig.getMsgName());
        notifyDto.setUrl(notifyConfig.getMsgUrl());
        if(StrUtil.isNotEmpty(dto.getUserId())){
            String url=notifyConfig.getMsgUrl().replace("USERID",dto.getUserId());
            notifyDto.setUrl(url);
        }
        notifyDto.setStaffUserId(qiWeiStaffId);
        if(NotityTypeEunm.UNADD_QW.equals(type)){//好友流失提醒
            NotifyMsgTemplateDTO.FollowLoss followLoss=dto.getFollowLoss();
            notifyDto.setContent(notifyConfig.getMsgContent()
                    .replace("{客户昵称}",followLoss.getNickName())
                    .replace("{客户名称}",followLoss.getUserName())
                    .replace("{删除时间}",followLoss.getDeleteDate()));
            createPush(notifyDto);
            return;
        }else if(NotityTypeEunm.UNREGISTER_MEMBER.equals(type)){//跟进提醒
            NotifyMsgTemplateDTO.FollowUp followUp=dto.getFollowUp();
            notifyDto.setContent(notifyConfig.getMsgContent()
                    .replace("{员工姓名}",followUp.getStaffName())
                    .replace("{客户昵称}",followUp.getNickName())
                    .replace("{客户名称}",followUp.getUserName())
                    .replace("{创建时间}",followUp.getCreateDate())
            );
            createPush(notifyDto);
            return;
        }else if(NotityTypeEunm.INVITE_USER.equals(type)){//素材浏览
            NotifyMsgTemplateDTO.Material material=dto.getMaterial();
            notifyDto.setContent(notifyConfig.getMsgContent()
                    .replace("{客户昵称}",material.getNickName())
                    .replace("{客户名称}",material.getUserName())
                    .replace("{浏览素材}",material.getMaterialName())
                    .replace("{浏览时长}",material.getBrowseTime())
                    .replace("{所打标签}",material.getTag())
                    .replace("{浏览时间}",material.getBrowseDate())
            );
            createPush(notifyDto);
            return;
        }else if(NotityTypeEunm.SUB.equals(type)){//敏感词命中
            NotifyMsgTemplateDTO.Hitkeyword hitkeyword=dto.getHitkeyword();
            String content=notifyConfig.getMsgContent();
            if(StrUtil.isEmpty(hitkeyword.getTag())){
                content=content.replace("打标签: {打标签}，","");//员工不需要打标签
            }else{
                content=content.replace("{打标签}",""+hitkeyword.getTag());
            }
            notifyDto.setContent(content
                            .replace("{触发人}",hitkeyword.getOffendUser())
                            .replace("{敏感词}",hitkeyword.getMate())
                            .replace("{触发时间}",hitkeyword.getOffendOutTime())
            );
//            notifyDto.setContent(notifyConfig.getMsgContent()
//                    .replace("{触发人}",hitkeyword.getOffendUser())
//                    .replace("{敏感词}",hitkeyword.getMate())
//                    .replace("{触发时间}",hitkeyword.getOffendOutTime())
//                    .replace("{打标签}",""+hitkeyword.getTag())
//            );
            createPush(notifyDto);
            return;
        }else if(NotityTypeEunm.USER_REGISTER_SUCCESS.equals(type)){//回复超时提醒
            NotifyMsgTemplateDTO.TimeOut timeOut=dto.getTimeOut();
            notifyDto.setContent(notifyConfig.getMsgContent()
                    .replace("{触发人}",timeOut.getOffendUser())
                    .replace("{超时时长}",timeOut.getTimeOutTime())
                    .replace("{触发时间}",timeOut.getOffendOutTime())
                    .replace("{会话对象}",timeOut.getUserName())
            );
            notifyDto.setStaffUserId(qiWeiStaffId);
            createPushText(notifyDto);
            return;
        }else if(NotityTypeEunm.CP_PHONE_TASK.equals(type)){//手机号任务提醒
            NotifyMsgTemplateDTO.PhoneTask phoneTask=dto.getPhoneTask();
            notifyDto.setContent(notifyConfig.getMsgContent()
                    .replace("{开始日期}",phoneTask.getStartTime())
                    .replace("{结束日期}",phoneTask.getEndTime())
                    .replace("{每日数量}",phoneTask.getNumber())
            );
            notifyDto.setStaffUserId(qiWeiStaffId);
            if(Objects.nonNull(phoneTask.getTaskId())){
                String url=notifyConfig.getMsgUrl().replace("TASK_ID",""+phoneTask.getTaskId());
                notifyDto.setUrl(url);
            }
            createPush(notifyDto);
            return;
        }
    }

    public boolean createPushText(NotifyDto notifyDto)  {
        try {
            log.info("发送文本应用消息 入参：{}",JSON.toJSONString(notifyDto));
            WxCpMessage wxCpMessage =WxCpMessage.TEXT()
                    .agentId(wxCpService.getWxCpConfigStorage().getAgentId())
                    .toUser(notifyDto.getStaffUserId())
                    .content(notifyDto.getContent())
                    .build();
            wxCpMessage.setEnableDuplicateCheck(true);//表示是否开启重复消息检查，0表示否，1表示是，默认0
            wxCpMessage.setDuplicateCheckInterval(1800);//	表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时
            WxCpMessageSendResult result =  wxCpService.getMessageService().send(wxCpMessage);
            log.info("发送文本应用消息 结果：{}",JSON.toJSONString(result));
            return result.getErrCode()==0;
        }catch (WxErrorException wx){
            log.error("",wx);
            return false;
        }
    }
}
