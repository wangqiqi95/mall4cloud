package com.mall4j.cloud.biz.feign;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.WxCpApiFeignClient;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.biz.config.WxCpConfiguration;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.biz.model.cp.Config;
import com.mall4j.cloud.biz.service.cp.ConfigService;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalUnassignList;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactBatchInfo;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.user.WxCpDeptUserResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class WxCpApiFeignController implements WxCpApiFeignClient {
    private  final WxCpService wxCpService;
    private final ConfigService configService;
    private final FeignShopConfig feignShopConfig;
    private final String GET_TOKEN="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET";

    @Override
    public ServerResponseEntity<JSONObject> externalcontactDetail(String qiWeiUserId, String staffQiWeiUserId) {
        try {
            WxCpExternalContactInfo wxCpExternalContactInfo=null;
            List<String> externalcontactIds=wxCpService.getExternalContactService().listExternalContacts(staffQiWeiUserId);
            log.info("staffQiWeiUserId获取到全部客户列表：---> {}",externalcontactIds.toString());
            List<String> externalcontactId=externalcontactIds.stream().filter(external_userid -> external_userid.equals(qiWeiUserId)).collect(Collectors.toList());
            log.info("staffQiWeiUserId获取到当前包含客户{}列表：---> {}",qiWeiUserId,externalcontactId.toString());
            if(CollectionUtil.isNotEmpty(externalcontactId)){
                String cursor="";//上次请求返回的next_cursor【非必填 企微API文档2023/05/19更新】
                wxCpExternalContactInfo=wxCpService.getExternalContactService().getContactDetail(qiWeiUserId,cursor);
                log.info("staffQiWeiUserId获取到当前包含客户{}详情：---> {}", qiWeiUserId, JSON.toJSON(wxCpExternalContactInfo));
            }
            if(Objects.isNull(wxCpExternalContactInfo) || Objects.isNull(wxCpExternalContactInfo.getExternalContact())){
                return null;
            }
            JSONObject jsonObject=JSONObject.parseObject(JSON.toJSONString(wxCpExternalContactInfo.getExternalContact()));
            return ServerResponseEntity.success(jsonObject);
        }catch (Exception e){
            throw new LuckException("换取userId失败");
        }
    }

    @Override
    public ServerResponseEntity<List<WxCpDepart>> wxCpDepartList(Long id) {
        try {
            return ServerResponseEntity.success(wxCpService.getDepartmentService().list(id));
        }catch (WxErrorException e){
            throw new LuckException(""+e.getError().getErrorCode()+e.getError().getErrorMsg());
        }
    }

    @Override
    public ServerResponseEntity<WxCpDeptUserResult> wxCpUserListId(String cursor, Integer limit)  {
        try {
            return ServerResponseEntity.success(wxCpService.getUserService().getUserListId(cursor,limit));
        }catch (WxErrorException e){
            throw new LuckException(""+e.getError().getErrorCode()+e.getError().getErrorMsg());
        }
    }

    @Override
    public ServerResponseEntity<WxCpUser> wxCpByUserId(String userId) {
        try {
            return ServerResponseEntity.success(wxCpService.getUserService().getById(userId));
        }catch (WxErrorException e){
            throw new LuckException(""+e.getError().getErrorCode()+e.getError().getErrorMsg());
        }
    }

    @Override
    public ServerResponseEntity<String> wxCpByMobile(String mobile) {
        try {
            return ServerResponseEntity.success(wxCpService.getUserService().getUserId(mobile));
        }catch (WxErrorException e){
            throw new LuckException(""+e.getError().getErrorCode()+e.getError().getErrorMsg());
        }
    }

    @Override
    public ServerResponseEntity<String> wxCpConnectAccessToken() {

        try {
            String token= WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.CP_CONNECT_AGENT_ID).getAccessToken();
            log.error("wxCpExternalAccessToken token : {}",token);
            return  ServerResponseEntity.success(token);
        }catch (WxErrorException e){
            log.error("wxCpExternalAccessToken error : {}",e);
            throw new LuckException(e.getMessage());
        }

//        String token= StrUtil.isNotBlank(RedisUtil.get(BizCacheNames.WX_CONNECT_ACCESS_TOKEN))?RedisUtil.get(BizCacheNames.WX_CONNECT_ACCESS_TOKEN):"";
////        String token= "";
//        if(StrUtil.isBlank(token)){
//            Config config = configService.getConfig();
//            String url=GET_TOKEN.replace("ID",config.getCpId()).replace("SECRET",config.getConnetSecret());
//            JSONObject json= WechatUtils.doGetstr(url);
//            log.info("wxCpConnectAccessToken --> msg: {}",json.toJSONString());
//            if(json.getIntValue("errcode")==0){
//                token=json.getString("access_token");
//                RedisUtil.set(BizCacheNames.WX_CONNECT_ACCESS_TOKEN,token,1800);
//            }
//        }
//        return ServerResponseEntity.success(token);
    }

    @Override
    public ServerResponseEntity<String> wxCpExternalAccessToken() {

        try {
            String token= WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getAccessToken();
            log.error("wxCpExternalAccessToken token : {}",token);
            return  ServerResponseEntity.success(token);
        }catch (WxErrorException e){
            log.error("wxCpExternalAccessToken error : {}",e);
            throw new LuckException(e.getMessage());
        }

//        String token= StrUtil.isNotBlank(RedisUtil.get(BizCacheNames.WX_CONNECT_ACCESS_TOKEN))?RedisUtil.get(BizCacheNames.WX_CONNECT_ACCESS_TOKEN):"";
////        String token= "";
//        if(StrUtil.isBlank(token)){
//            Config config = configService.getConfig();
//            String url=GET_TOKEN.replace("ID",config.getCpId()).replace("SECRET",config.getAgentSecret());
//            JSONObject json= WechatUtils.doGetstr(url);
//            log.info("wxCpExternalAccessToken --> msg: {}",json.toJSONString());
//            if(json.getIntValue("errcode")==0){
//                token=json.getString("access_token");
//                RedisUtil.set(BizCacheNames.WX_CONNECT_ACCESS_TOKEN,token,1800);
//            }
//        }
//        return ServerResponseEntity.success(token);
    }

    @Override
    public ServerResponseEntity<String> wxCpAgentJsapiTicket() {
        String url="https://qyapi.weixin.qq.com/cgi-bin/ticket/get?access_token=ACCESS_TOKEN&type=agent_config";
        ServerResponseEntity<String>  responseEntity=this.wxCpExternalAccessToken();
        ServerResponseEntity.checkResponse(responseEntity);
        url=url.replace("ACCESS_TOKEN",responseEntity.getData());
        String json= HttpUtil.get(url);
        log.info("移动端JS-SDK签名agentConfig->getjsapiTicket: {}",json);
//            String jsapiTicket = WxCpConfiguration.getWxCpService(WxCpConfiguration.getAgentId()).getAgentJsapiTicket(false);
        String jsapiTicket ="";
        if(StrUtil.isNotEmpty(json)){
            JSONObject jsonObject= JSON.parseObject(json);
            if(!jsonObject.get("errcode").toString().equals("0")){
                return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
            }
            jsapiTicket=jsonObject.get("ticket").toString();
        }
        if(StrUtil.isEmpty(jsapiTicket)){
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
        return ServerResponseEntity.success(jsapiTicket);
    }

    @Override
    public ServerResponseEntity<WxCpUserExternalUnassignList> getUnassignedList(String cursor, Integer pageSize) {
        try {
            return ServerResponseEntity.success(wxCpService.getExternalContactService().listUnassignedList(null,cursor,pageSize));
        }catch (WxErrorException e){
            throw new LuckException(""+e.getError().getErrorCode()+e.getError().getErrorMsg());
        }
    }

    @Override
    public ServerResponseEntity<Void> deleteByUserId(String userId) {
        try {
//            wxCpService.getUserService().delete(userId);
            WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.CP_CONNECT_AGENT_ID).getUserService().delete(userId);
            return ServerResponseEntity.success();
        }catch (WxErrorException e){
            throw new LuckException(""+e.getError().getErrorCode()+e.getError().getErrorMsg());
        }
    }

    @Override
    public ServerResponseEntity<List<WxCpExternalContactInfo>> externalcontactList(String userId) {
        try {
            List<String> externalIds= WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService().listExternalContacts(userId);
            if(CollUtil.isEmpty(externalIds)){
                return ServerResponseEntity.success();
            }
            List<WxCpExternalContactInfo> externalContactInfos=new ArrayList<>();
            for (String externalId : externalIds) {
                String cursor="";//上次请求返回的next_cursor【非必填 企微API文档2023/05/19更新】
                WxCpExternalContactInfo wxCpExternalContactInfo = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService()
                        .getContactDetail(externalId,cursor);
                if(Objects.nonNull(wxCpExternalContactInfo)){
                    externalContactInfos.add(wxCpExternalContactInfo);
                }
            }
            return ServerResponseEntity.success(externalContactInfos);
        }catch (WxErrorException e){
            throw new LuckException(""+e.getError().getErrorCode()+e.getError().getErrorMsg());
        }
    }

    /**
     * 1.根据员工获取客户列表
     * 2.批量获取客户详情数据
     * TODO 此接口需要自行做下一页数据：cursor 查询处理
     * @param userId
     * @return
     */
    @Override
    public ServerResponseEntity<WxCpExternalContactBatchInfo> batchExternalcontactList(String userId,String cursor,int limit) {
        try {
            WxCpExternalContactBatchInfo contactDetailBatch = WxCpConfiguration.getWxCpService(WxCpConfigurationPlus.getAgentId()).getExternalContactService()
                    .getContactDetailBatch(new String[]{userId},cursor,limit);
            return ServerResponseEntity.success(contactDetailBatch);
        }catch (WxErrorException e){
            throw new LuckException(""+e.getError().getErrorCode()+e.getError().getErrorMsg());
        }
    }

    @Override
    public ServerResponseEntity<String> oauth2Url(String redirectUri,@RequestParam String scope) {
        log.info("oauth2Url-->redirectUri:{} scope:{}",redirectUri,scope);
        if(StrUtil.isEmpty(scope)){
            /**
             * 应用授权作用域。
             * snsapi_base：静默授权，可获取成员的基础信息（UserId与DeviceId）；
             * snsapi_privateinfo：手动授权，可获取成员的详细信息，包含头像、二维码等敏感信息。
             */
            scope="snsapi_base";
        }
        String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE&agentid=AGENTID#wechat_redirect";
        try {
            redirectUri=java.net.URLEncoder.encode(redirectUri, "UTF-8");
        }catch (Exception e){
            log.info("oauth2Url URLEncoder.encode.redirectUri error:{}",e);
        }
        Config config=configService.getConfig();
        String backUrl=url.replace("REDIRECT_URI",redirectUri)
                .replace("STATE","cp"+ RandomUtil.getUniqueNum())//cp->标识企业微信/mp->标识公众号
                .replace("AGENTID",""+config.getAgentId())
                .replace("SCOPE",scope)
                .replace("CORPID",""+config.getCpId());
        return ServerResponseEntity.success(backUrl);
    }


    @Override
    public ServerResponseEntity<String> Mpoauth2Url(String redirectUri, String scope) {
        if(StrUtil.isEmpty(scope)){
            /**
             * 应用授权作用域。
             * snsapi_base：静默授权，可获取成员的基础信息（UserId与DeviceId）；
             * snsapi_privateinfo：手动授权，可获取成员的详细信息，包含头像、二维码等敏感信息。
             */
            scope="snsapi_base";
        }
        String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE&agentid=AGENTID#wechat_redirect";
        try {
            redirectUri=java.net.URLEncoder.encode(redirectUri, "UTF-8");
        }catch (Exception e){
            log.info("oauth2Url URLEncoder.encode.redirectUri error:{}",e);
        }
        WxMp wxMp = feignShopConfig.getWxMp();
        log.info("--公众号信息：{}", JSON.toJSONString(wxMp));
//        Config config= configService.getConfig("MP_CONFIG");
        String backUrl=url.replace("REDIRECT_URI",redirectUri)
                .replace("STATE","mp"+ RandomUtil.getUniqueNum())//cp->标识企业微信/mp->标识公众号
                .replace("SCOPE",scope)
                .replace("CORPID",""+wxMp.getAppId());
        return ServerResponseEntity.success(backUrl);
    }
}
