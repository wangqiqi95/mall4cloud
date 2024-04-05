package com.mall4j.cloud.biz.service.cp.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.dto.cp.wx.*;
import com.mall4j.cloud.biz.service.cp.WxCpGroupChatService;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

/**
 * 实现群联系活码的操作
 *
 * @author hwy
 * @date 2022-01-24 11:05:42
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxCpGroupChatServiceImpl implements WxCpGroupChatService {

    @Override
    public String addJoinWay(AddJoinWay addJoinWay)  {
        try {
            WxCpService mainService = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId());
            String url = mainService.getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/groupchat/add_join_way");
            String responseContent = mainService.post(url, Json.toJsonString(addJoinWay));
            log.info("----------------addJoinWay----------------" + responseContent);
            WxCpJoinWayResult result = WxCpJoinWayResult.fromJson(responseContent);
            return result.getConfigId();
        }catch (WxErrorException ex){
            log.error("生成群活码失败",ex);
            throw new LuckException("生成群活码失败"+ex.getMessage());
        }
    }

    @Override
    public WxCpGroupChatResult getJoinWay(String configId) {
        try {
            WxCpService mainService = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId());
            String url = mainService.getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/groupchat/get_join_way");
            String responseContent = mainService.post(url, Json.toJsonString(new GetJoinWay(configId)));
            log.info("----------------getJoinWay----------------" + responseContent);
            return WxCpGroupChatResult.fromJson(responseContent);
        }catch (WxErrorException ex){
            log.error("更新群活码失败",ex);
        }
        return  null;
    }

    @Override
    public boolean updateJoinWay(AddJoinWay addJoinWay) {
        try {
            WxCpService mainService = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId());
            String url = mainService.getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/groupchat/update_join_way");
            String responseContent = mainService.post(url, Json.toJsonString(addJoinWay));
            log.info("----------------updateJoinWay----------------" + responseContent);
            WxCpBaseResp resp = WxCpBaseResp.fromJson(responseContent);
            return resp.getErrcode() == 0;
        }catch (WxErrorException ex){
            log.error("更新群活码失败",ex);
            throw new LuckException("更新群活码失败"+ex.getMessage());
        }
    }

    @Override
    public boolean delJoinWay(String configId)  {
        try {
            WxCpService mainService =  WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId());
            String url = mainService.getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/groupchat/del_join_way");
            String responseContent = mainService.post(url, Json.toJsonString(new GetJoinWay(configId)));
            log.info("----------------delJoinWay----------------{} configId:{}",responseContent,configId);
            WxCpBaseResp resp =  WxCpBaseResp.fromJson(responseContent);
            return resp.getErrcode()==0;
        }catch (WxErrorException ex){
            log.error("删除群活码失败",ex);
            throw new LuckException("删除群活码失败"+ex.getMessage());
        }
    }

    /**
     * https://developer.work.weixin.qq.com/document/path/92127
     *分配离职成员的客户群
     * @param chatIds
     * @param newOwner
     * @return
     * @throws WxErrorException
     */
    public WxBizCpUserExternalGroupChatTransferResp transferGroupChat(String[] chatIds, String newOwner) throws WxErrorException {
        JsonObject json = new JsonObject();
        if (ArrayUtils.isNotEmpty(chatIds)) {
            json.add("chat_id_list", (new Gson()).toJsonTree(chatIds).getAsJsonArray());
        }
        WxCpService mainService =  WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId());
        json.addProperty("new_owner", newOwner);
        String url = mainService.getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/groupchat/transfer");
        String result = mainService.post(url, json.toString());
        return WxBizCpUserExternalGroupChatTransferResp.fromJson(result);
    }

    /**
     * https://developer.work.weixin.qq.com/document/path/95703
     * 分配在职成员的客户群
     * @param chatIds
     * @param newOwner
     * @return
     * @throws WxErrorException
     */
    public WxBizCpUserExternalGroupChatTransferResp onjobTransferGroupChat(String[] chatIds, String newOwner) throws WxErrorException {
        JsonObject json = new JsonObject();
        if (ArrayUtils.isNotEmpty(chatIds)) {
            json.add("chat_id_list", (new Gson()).toJsonTree(chatIds).getAsJsonArray());
        }
        WxCpService mainService =  WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId());
        json.addProperty("new_owner", newOwner);
        String url = mainService.getWxCpConfigStorage().getApiUrl("/cgi-bin/externalcontact/groupchat/onjob_transfer");
        String result = mainService.post(url, json.toString());
        return WxBizCpUserExternalGroupChatTransferResp.fromJson(result);
    }
}
