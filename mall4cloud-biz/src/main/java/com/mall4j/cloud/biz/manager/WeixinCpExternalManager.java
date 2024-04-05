package com.mall4j.cloud.biz.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.JsonObject;
import com.mall4j.cloud.api.biz.dto.cp.externalcontact.CpChatAddMsgTemplateDTO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.Json;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;

import me.chanjar.weixin.cp.bean.external.contact.WxCpGroupMsgSendResult;
import me.chanjar.weixin.cp.bean.external.WxCpMsgTemplateAddResult;
import me.chanjar.weixin.cp.bean.external.contact.WxCpGroupMsgTaskResult;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Component;

import static me.chanjar.weixin.cp.constant.WxCpApiPathConsts.ExternalContact.ADD_MSG_TEMPLATE;

@Slf4j
@Component
public class WeixinCpExternalManager {


    /**
     * 获取客户详情
     * @param userId
     * @param externalUserId
     * @return
     */
    public WxCpExternalContactInfo getWxCpExternalContactInfo(String userId,String externalUserId){
        try {
            log.info("----getWxCpExternalContactInfo start -------userId：【"+userId+"】  externalUserId:【"+externalUserId+"】");
            String cursor="";//上次请求返回的next_cursor【非必填 企微API文档2023/05/19更新】
            WxCpExternalContactInfo wxCpExternalContactInfo = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService()
                    .getContactDetail(externalUserId,cursor);
            log.info("----getWxCpExternalContactInfo back ------->【{}】 nextCursor-->【{}】", Json.toJsonString(wxCpExternalContactInfo),wxCpExternalContactInfo.getNextCursor());
            cursor=wxCpExternalContactInfo.getNextCursor();
            if(StrUtil.isNotEmpty(cursor)){
                //应用只能获取到可见范围内的成员;当客户在企业内的跟进人超过500人时需要使用cursor参数进行分页获取。
                do {
                    log.info("----getWxCpExternalContactInfo nextCursor ------->",cursor);
                    wxCpExternalContactInfo = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService()
                            .getContactDetail(externalUserId,cursor);
                    if(CollUtil.isEmpty(wxCpExternalContactInfo.getFollowedUsers())){
                        break;
                    }
                    wxCpExternalContactInfo.getFollowedUsers().addAll(wxCpExternalContactInfo.getFollowedUsers());
                    cursor = wxCpExternalContactInfo.getNextCursor();
                    if(StrUtil.isEmpty(cursor)){
                        break;
                    }
                }while (StrUtil.isEmpty(cursor));
            }

            return wxCpExternalContactInfo;
        }catch (WxErrorException e){
            log.error("getWxCpExternalContactInfo error {}",e);
        }
        return null;
    }

    /**
     * 提醒成员群发
     * @param msgId
     */
    public WxCpBaseResp remindGroupmsgSend(String msgId) throws WxErrorException{
        JsonObject json = new JsonObject();
        json.addProperty("msgid", msgId);
        String url = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/remind_groupmsg_send");
        log.info("提醒成员群发入参：{}",json.toString());
        String result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).post(url, json.toString());
        log.info("提醒成员群发结果：{}",result);
        return WxCpBaseResp.fromJson(result);
    }

    // 停止企业群发
    public WxCpGroupMsgTaskResult cancelGroupmsgSend(String msgid) throws WxErrorException {
        JsonObject json = new JsonObject();
        json.addProperty("msgid", msgid);
        String url = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/cancel_groupmsg_send");
        log.info("停止企业群发入参：{}",json.toString());
        String result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).post(url, json.toString());
        return WxCpGroupMsgTaskResult.fromJson(result);
    }

    // 获取群发成员发送任务列表
    public WxCpGroupMsgTaskResult getGroupMessageTask(String msgid, Integer limit, String cursor) throws WxErrorException {
        JsonObject json = new JsonObject();
        json.addProperty("msgid", msgid);
        json.addProperty("limit", limit);
        if (StringUtils.isNotEmpty(cursor)){
            json.addProperty("cursor", cursor);
        }
        String url = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/get_groupmsg_task");
        log.info("CP API PARAM IS:{}", json.toString());
        String result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).post(url, json.toString());
        return WxCpGroupMsgTaskResult.fromJson(result);
    }

    // 获取企业群发成员执行结果
    public WxCpGroupMsgSendResult getGroupMessageSendResult(String msgId, String userId, Integer limit, String cursor) throws WxErrorException {
        JsonObject json = new JsonObject();
        json.addProperty("msgid", msgId);
        json.addProperty("userid", userId);
        json.addProperty("limit", limit);
        json.addProperty("cursor", cursor);
        String url = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/get_groupmsg_send_result");
        String result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).post(url, json.toString());
        return WxCpGroupMsgSendResult.fromJson(result);
    }

    public ServerResponseEntity<WxCpMsgTemplateAddResult> addMsgTemplate(String wxCpMsgTemplate){
        WxCpMsgTemplateAddResult wxCpMsgTemplateAddResult;
        try {
            final String url = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getWxCpConfigStorage().getApiUrl(ADD_MSG_TEMPLATE);
            wxCpMsgTemplateAddResult = WxCpMsgTemplateAddResult.fromJson(WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).post(url, wxCpMsgTemplate));

            if (wxCpMsgTemplateAddResult.getErrMsg().equals("ok")){
                return ServerResponseEntity.success(wxCpMsgTemplateAddResult);
            }
            return ServerResponseEntity.showFailMsg(wxCpMsgTemplateAddResult.getErrMsg());
        }catch (Exception e){
            log.error("WECHAT CP ADD MSG TEMPLATE IS FAIL,MESSAGE IS {}",e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 创建企业群发-客户群
     * @param extendWxCpMsgTemplateDTO
     * @return
     */
    public ServerResponseEntity<WxCpMsgTemplateAddResult> addChatMsgTemplate(CpChatAddMsgTemplateDTO extendWxCpMsgTemplateDTO){
        WxCpMsgTemplateAddResult wxCpMsgTemplateAddResult;
        try {
            log.error("创建企业群发-客户群入参： {}", extendWxCpMsgTemplateDTO.toJson());
            final String url = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getWxCpConfigStorage().getApiUrl(ADD_MSG_TEMPLATE);
            wxCpMsgTemplateAddResult=WxCpMsgTemplateAddResult.fromJson(WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).post(url, extendWxCpMsgTemplateDTO.toJson()));
            log.error("创建企业群发-客户群日志： {}", wxCpMsgTemplateAddResult.toString());
            if (wxCpMsgTemplateAddResult.getErrMsg().equals("ok")){
                return ServerResponseEntity.success(wxCpMsgTemplateAddResult);
            }
            return ServerResponseEntity.showFailMsg(wxCpMsgTemplateAddResult.getErrMsg());
        }catch (Exception e){
            log.error("创建企业群发-客户群失败 {}",e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 停止发表企业朋友圈
     * @param qwMomentsId
     * @return
     */
    public WxCpBaseResp cancelMomentTask(String qwMomentsId) throws WxErrorException {
        JsonObject json = new JsonObject();
        json.addProperty("moment_id", qwMomentsId);
        String url = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/cancel_moment_task");
//        String url = super.mainService.getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/cancel_moment_task");
        String result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).post(url, json.toString());
        return WxCpBaseResp.fromJson(result);
    }

    /**
     * 获取企微群详情
     * @param groupId
     * @return
     */
    public WxCpUserExternalGroupChatInfo.GroupChat getChatDetail(String groupId){
        try {
            WxCpUserExternalGroupChatInfo result =  WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().getGroupChat(groupId, 1);
            return result.getGroupChat();
        }catch (WxErrorException we){
            log.error("查询群详情出错 errorCode:【"+we.getError().getErrorCode()+"】 msg:【"+we.getError().getErrorMsg()+"】");
            return null;
        }
    }

    /**
     * 删除渠道活码
     * @param configId
     * @return
     */
    public boolean delContactWay(String configId)  {
        try {

            WxCpBaseResp result = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().deleteContactWay(configId);
            log.info("----------------delContactWay----------------"+result.toJson());
            if (result.getErrcode() == 0) {
                return true;
            } else {
                throw new WxErrorException("删除渠道活码失败"+result.getErrmsg());
            }
        }catch (WxErrorException ex){
            log.error("删除渠道活码失败",ex);
            throw new LuckException("删除渠道活码失败"+ex.getMessage());
        }
    }
}
