package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.wx.wx.util.AccessTokenUtil;
import com.mall4j.cloud.biz.wx.wx.util.WXBizMsgCrypt;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.config.MinioTemplate;
import com.mall4j.cloud.biz.model.ComponentAccessTokenResp;
import com.mall4j.cloud.biz.model.PreAuthCodeResp;
import com.mall4j.cloud.biz.model.WeixinOpenAccount;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.biz.vo.AccessTokenVo;
import com.mall4j.cloud.biz.vo.ComponentAccessTokenVo;
import com.mall4j.cloud.biz.vo.PreAuthCodeVo;
import com.mall4j.cloud.biz.vo.RefreshTokenVo;
import com.mall4j.cloud.common.bean.WxOpen;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.yaml.snakeyaml.error.YAMLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Date 2021年12月30日, 0030 14:50
 * @Created by eury
 */
@Service
public class WechatOpServiceImpl  implements WechatOpService {
    private static final Logger log = LoggerFactory.getLogger(WechatOpServiceImpl.class);


    @Autowired
    private FeignShopConfig feignShopConfig;

    @Resource
    private WeixinOpenAccountService weixinOpenAccountService;

    @Resource
    private WeixinWebAppService weixinWebAppService;

    @Autowired
    private MinioTemplate minioTemplate;

    @Autowired
    private WechatOpMsgEventService wechatOpMsgEventService;

    WxOpen getWxOpen(){
//        String json=RedisUtil.get(WechatConstants.WX_OPEN_INFO);
//        if(StringUtils.isNotEmpty(json)){
//            return JSON.parseObject(json,WxOpen.class);
//        }
        WxOpen wxOpen=feignShopConfig.getWxOpen();
        log.info("--获取开放平台数据getWxOpen {}",JSON.toJSONString(wxOpen));
//        RedisUtil.set(WechatConstants.WX_OPEN_INFO, JSON.toJSONString(wxOpen), WechatConstants.WXOPEN_EXPIRE_TIME);
        return wxOpen;
    }

    /**
     * 获取授权账号信息
     * @param appid
     * @return
     */
    WeixinOpenAccount getWeixinOpenAccount(String appid){
        return weixinOpenAccountService.queryOneByAppid(appid);
    }


    /**
     * 授权事件接收URL
     * 用于接收平台推送给第三方平台账号的消息与事件，如授权事件通知、component_verify_ticket等。
     * 注意，该URL的一级域名需与“消息与事件接收URL”的一级域名一致
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @param postdata  消息体
     * @return 如果获得只需要返回 SUCCESS
     */
    @Override
    public String authEvent(String signature, String timestamp, String nonce, String echostr, String encryptType, String msgSignature, String postdata) {
        log.info("【微信开放平台授权事件接收URL】请求参数：signature：【{}】，timestamp：【{}】，nonce：【{}】，echostr：【{}】，encryptType：【{}】，msgSignature：【{}】，postdata：【{}】", signature, timestamp, nonce, echostr, encryptType, msgSignature, postdata);
        //配置url验证
        if (StrUtil.isEmpty(postdata)) {
            log.info("【微信开放平台授权事件接收URL】消息体为空直接返回验证成功，返回随机数，echostr：{}", echostr);
            return echostr;
        }

        //接收事件url
        try {
            //这个类是微信官网提供的解密类,需要用到消息校验Token 消息加密Key和服务平台appid
            WxOpen wxOpen=getWxOpen();
            WXBizMsgCrypt pc = new WXBizMsgCrypt(wxOpen.getToken(), wxOpen.getAesKey(), wxOpen.getAppId());
            //加密模式：需要解密
            String xml = pc.decryptMsg(msgSignature, timestamp, nonce, postdata);
            log.info("【微信开放平台授权事件接收URL】解密后xml：【{}】", xml);

            //将xml转为map
            Map<String, String> resultMap = WechatUtils.xmlToMap(xml);
            log.info("【微信开放平台授权事件接收URL】resultMap：【{}】", JSON.toJSONString(resultMap));

            //授权事件类型
            String infotype = MapUtil.getStr(resultMap, "InfoType");
            if ("component_verify_ticket".equals(infotype)) {
                log.info("【微信开放平台授权事件接收URL】票据类型事件");

                //获取验证票据
                String componentVerifyTicket = MapUtil.getStr(resultMap, "ComponentVerifyTicket");
                log.info("【微信开放平台授权事件接收URL】三方平台获取验证票据，componentVerifyTicket：【{}】", componentVerifyTicket);

                if (StrUtil.isEmpty(componentVerifyTicket)) {
                    log.info("【微信开放平台授权事件接收URL】三方平台获取验证票据失败,验证票据为空");
                    return WechatConstants.TICKET_SUCCESS;
                }

                //将验证票据放入容器，后期可以优化到缓存Redis
//                wechatOpMap.put(COMPONENT_APP_ID + "ComponentVerifyTicket", componentVerifyTicket);

                if(StringUtils.isNotEmpty(componentVerifyTicket)){
                    WeixinOpenAccount entity = getWeixinOpenAccount(wxOpen.getAppId());
                    if(entity==null){
                        entity=new WeixinOpenAccount();
                        Long id= RandomUtil.getUniqueNum();
                        entity.setId(String.valueOf(id));
                        entity.setTicket(componentVerifyTicket);
                        entity.setAppId(wxOpen.getAppId());
                        entity.setGetTicketTime(new Date());
                        entity.setCreateTime(new Date());
                        entity.setCreateBy(entity.getAppId());
                        weixinOpenAccountService.save(entity);
                        log.info("微信第三方添加TICKET成功！TICKET={}.",new Object[]{componentVerifyTicket});
                    }else{
                        entity.setTicket(componentVerifyTicket);
                        entity.setAppId(wxOpen.getAppId());
                        entity.setGetTicketTime(new Date());
                        log.info("===================={}=======================",new Object[]{entity});
                        entity.setUpdateTime(new Date());
                        entity.setUpdateBy(entity.getAppId());
                        weixinOpenAccountService.update(entity);
                        log.info("微信第三方重置TICKET成功！TICKET={}.",new Object[]{componentVerifyTicket});
                    }
                }

                log.info("【微信开放平台授权事件接收URL】验证票据处理成功");
            } else if ("authorized".equals(infotype)) {
                log.info("【微信开放平台授权事件接收URL】授权类型事件");

                //微信开放平台授权事件参数
                String AuthorizerAppid = MapUtil.getStr(resultMap, "AuthorizerAppid");
                String AuthorizationCode = MapUtil.getStr(resultMap, "AuthorizationCode");
                String AuthorizationCodeExpiredTime = MapUtil.getStr(resultMap, "AuthorizationCodeExpiredTime");
                log.info("【微信开放平台授权事件接收URL】授权类型事件，AuthorizerAppid：【{}】，AuthorizationCode：【{}】", AuthorizerAppid, AuthorizationCode);

                //获取授权
                scanCodecallback(AuthorizationCode, Long.valueOf(AuthorizationCodeExpiredTime));

            } else if ("unauthorized".equals(infotype)) {
                log.info("【微信开放平台授权事件接收URL】用户取消授权事件");
                String AuthorizerAppid = MapUtil.getStr(resultMap, "AuthorizerAppid");
                WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(AuthorizerAppid);
                if(weixinWebApp!=null){
                    weixinWebApp.setAuthorizationStatus("2");
                    weixinWebAppService.update(weixinWebApp);
                }
            } else if ("updateauthorized".equals(infotype)) {
                log.info("【微信开放平台授权事件接收URL】授权变更事件");
            } else {
                log.info("【微信开放平台授权事件接收URL】未知类型，暂不处理");
            }

            return WechatConstants.TICKET_SUCCESS;
        } catch (Exception e) {
            log.info("【微信开放平台授权事件接收URL】处理异常：{}", e);
            return WechatConstants.TICKET_SUCCESS;
        }
    }

    /**
     * 消息事件接收URL
     * 用于代授权的公众号或小程序的接收平台推送的消息与事件，该参数按规则填写（需包含/$APPID$，如www.abc.com/$APPID$/callback），实际接收消息时$APPID$将被替换为公众号或小程序AppId。
     * 注意：该URL的一级域名需与“授权事件接收URL”的一级域名一致
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @param postData  消息体
     * @return 如果获得只需要返回 SUCCESS
     */
//    @Async
    @Override
    public String msgEvent(String appId,
                           String signature,
                           String timestamp,
                           String nonce,
                           String openid,
                           String echostr,
                           String msgSignature,
                           String postData,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        log.info("【微信开放平台消息事件接收URL】请求参数：appId：【{}】，signature：【{}】，timestamp：【{}】，nonce：【{}】，openid：【{}】，echostr：【{}】，msgSignature：【{}】，postdata：【{}】",appId, signature, timestamp, nonce, openid, echostr, msgSignature, postData);

        try {
            // 微信配置时才为空, 返回echostr表示联通
            if (postData == null || postData.isEmpty()) {
                return WechatConstants.TICKET_SUCCESS;
            }

            //这个类是微信官网提供的解密类,需要用到消息校验Token 消息加密Key和服务平台appid
            WxOpen wxOpen=getWxOpen();
            if (StringUtils.isBlank(appId)) {
                appId = wxOpen.getAppId();
            }
            WXBizMsgCrypt pc = new WXBizMsgCrypt(wxOpen.getToken(), wxOpen.getAesKey(), appId);
            //加密模式：需要解密
            String xml = pc.decryptMsg(msgSignature, timestamp, nonce, postData);
            log.info("【微信开放平台消息事件接收URL】解密后xml：【{}】", xml);

            //将xml转为map
            Map<String, String> resultMap = WechatUtils.xmlToMap(xml);
            log.info("【微信开放平台消息事件接收URL】resultMap：【{}】", JSON.toJSONString(resultMap));

            return wechatOpMsgEventService.doBackEvent(xml,resultMap,request,response);
        }catch (Exception e){
            log.info("【微信开放平台消息事件接收URL】处理异常：{}", e);
            return WechatConstants.FAILED;
        }
    }



    /**
     * 1.启动票据推送服务
     */
    @Override
    public ServerResponseEntity apiStartPushTicket() {
        log.info("【微信开放平台启动票据推送服务】");

        try {
            WxOpen wxOpen=getWxOpen();
            WXBizMsgCrypt pc = new WXBizMsgCrypt(wxOpen.getToken(), wxOpen.getAesKey(), wxOpen.getAppId());
            //构建请求参数
            Map<String, Object> paramMap = new HashMap<>(16);
            //平台型第三方平台的appid
            paramMap.put("component_appid", wxOpen.getAppId());
            //平台型第三方平台的APPSECRET
            paramMap.put("component_secret", wxOpen.getSecret());

            //执行请求，获取结果
            log.info("【微信开放平台启动票据推送服务】请求参数：【{}】，请求地址：【{}】", JSON.toJSONString(paramMap), WechatConstants.API_START_PUSH_TICKET);
            String result = HttpUtil.post(WechatConstants.API_START_PUSH_TICKET, JSON.toJSONString(paramMap));
            JSONObject resultJsonObject = JSON.parseObject(result);
            log.info("【微信开放平台启动票据推送服务】响应结果：【{}】", resultJsonObject);

            if (0 != resultJsonObject.getIntValue("errcode")) {
                log.info("【微信开放平台启动票据推送服务失败】");
                return ServerResponseEntity.showFailMsg("微信开放平台启动票据推送服务失败");
            }

            log.info("【微信开放平台启动票据推送服务成功】");
            return ServerResponseEntity.success(resultJsonObject);
        } catch (Exception e) {
            log.info("【微信开放平台启动票据推送服务异常】处理异常：{}", e);
            return ServerResponseEntity.showFailMsg("微信开放平台启动票据推送服务异常");
        }
    }

    /**
     * 2.获取令牌，第三方平台接口的调用凭据
     * 获得component_verify_ticket后，按照获取第三方平台 component_access_token 接口文档，调用接口获取component_access_token
     * component_access_token有效期2h，当遭遇异常没有及时收到component_verify_ticket时，建议以上一次可用的component_verify_ticket继续生成component_access_token。避免出现因为 component_verify_ticket 接收失败而无法更新 component_access_token 的情况。
     */
    @Override
    public ServerResponseEntity getComponentAccessToken() {
        log.info("【微信开放平台获取令牌】");

        //将验证票据放入容器，后期可以优化到缓存Redis
        WxOpen wxOpen=getWxOpen();
        WeixinOpenAccount openAccount=getWeixinOpenAccount(wxOpen.getAppId());
        if(openAccount==null){
            return ServerResponseEntity.showFailMsg("微信开放平台获取令牌失败");
        }
        String componentVerifyTicket = openAccount.getTicket();

        //构建请求参数，测试用
        ComponentAccessTokenVo componentAccessTokenVo=new ComponentAccessTokenVo();
        componentAccessTokenVo.setComponent_appid(wxOpen.getAppId());
        componentAccessTokenVo.setComponent_appsecret(wxOpen.getSecret());
        componentAccessTokenVo.setComponent_verify_ticket(componentVerifyTicket);

        try {
            String jsonString = JSON.toJSONString(componentAccessTokenVo);
            log.info("【微信开放平台获取令牌】请求参数：【{}】，请求路径：【{}】", jsonString, WechatConstants.COMPONENT_ACCESS_TOKEN_URL);
            JSONObject jsonObject = WechatUtils.doPoststr(WechatConstants.COMPONENT_ACCESS_TOKEN_URL, jsonString);
            log.info("【微信开放平台获取令牌】响应结果：【{}】", jsonObject);

            if (jsonObject == null) {
                log.info("【微信开放平台获取令牌】微信开放平台获取令牌失败，返回结果为空");
                return ServerResponseEntity.showFailMsg("微信开放平台获取令牌失败");
            }

            String component_access_token = jsonObject.getString("component_access_token");
            Long expires_in = jsonObject.getLong("expires_in");
            if (StrUtil.isEmpty(component_access_token)) {
                log.info("获取令牌，第三方平台接口的调用凭据失败，返回结果为空，component_access_token：{}", component_access_token);
            }

            //将令牌放入容器，后期可以优化到缓存Redis
//            wechatOpMap.put(COMPONENT_APP_ID + "_component_access_token", component_access_token);

            if(StringUtils.isNotEmpty(component_access_token)){
                WeixinOpenAccount entity = getWeixinOpenAccount(wxOpen.getAppId());
                if(entity==null){
                    entity=new WeixinOpenAccount();
                    entity.setComponentAccessToken(component_access_token);
                    entity.setAppId(wxOpen.getAppId());
                    Long id= RandomUtil.getUniqueNum();
                    entity.setId(String.valueOf(id));
                    entity.setCreateTime(new Date());
                    entity.setCreateBy(entity.getAppId());
                    weixinOpenAccountService.save(entity);
                    log.info("微信第三方添加component_access_token成功！component_access_token={}.",new Object[]{component_access_token});
                }else{
                    entity.setComponentAccessToken(component_access_token);
                    entity.setAppId(wxOpen.getAppId());
                    log.info("===================={}=======================",new Object[]{entity});
                    entity.setUpdateTime(new Date());
                    entity.setUpdateBy(entity.getAppId());
                    weixinOpenAccountService.update(entity);
                    log.info("微信第三方重置component_access_token成功！component_access_token={}.",new Object[]{component_access_token});
                }
            }

            ComponentAccessTokenResp componentAccessTokenResp = new ComponentAccessTokenResp();
            componentAccessTokenResp.setComponent_access_token(component_access_token);
            componentAccessTokenResp.setExpires_in(expires_in);

            log.info("【微信开放平台获取令牌成功】");
            return ServerResponseEntity.success(componentAccessTokenResp);
        } catch (Exception e) {
            log.info("【微信开放平台获取令牌异常】处理异常：{}", e);
            return ServerResponseEntity.showFailMsg("微信开放平台获取令牌异常");
        }
    }

    /**
     * 3.获取预授权码 (有效期：30分钟)
     * 获得component_access_token后，按照获取预授权码 pre_auth_code接口文档 ，调接口获取pre_auth_code
     * 用于生成扫码授权二维码或者链接需要的pre_auth_code
     *
     * @return
     */
    @Override
    public ServerResponseEntity getPreAuthCode() {
        log.info("【微信开放平台获取预授权码】");

        WxOpen wxOpen=getWxOpen();
        WeixinOpenAccount entity = getWeixinOpenAccount(wxOpen.getAppId());
        if(entity==null || entity.getComponentAccessToken()==null){
            return ServerResponseEntity.showFailMsg("获取预授权码失败");
        }
        //将验证票据放入容器，后期可以优化到缓存Redis
//        String component_access_token = MapUtil.getStr(wechatOpMap, COMPONENT_APP_ID + "_component_access_token");
        String component_access_token = entity.getComponentAccessToken();
        //构建请求参数，测试用
        PreAuthCodeVo preAuthCodeVo=new PreAuthCodeVo();
        preAuthCodeVo.setComponent_appid(wxOpen.getAppId());
        preAuthCodeVo.setComponent_access_token(component_access_token);

        try {
            String jsonString = JSON.toJSONString(preAuthCodeVo);
            String preAuthCodeUrl = WechatUtils.replaceComponentAccessToken(WechatConstants.PRE_AUTH_CODE_URL, component_access_token);
            log.info("【微信开放平台获取预授权码】请求参数：【{}】，请求路径：【{}】", jsonString, preAuthCodeUrl);
            JSONObject jsonObject = WechatUtils.doPoststr(preAuthCodeUrl, jsonString);
            log.info("【微信开放平台获取预授权码】响应结果：【{}】", JSON.toJSONString(jsonObject));
            if (jsonObject == null) {
                log.info("【微信开放平台获取预授权码】获取预授权码失败，返回结果为空");
                return ServerResponseEntity.showFailMsg("获取预授权码失败,返回结果为空");
            }
            //获取预授权码
            String preAuthCode = jsonObject.getString("pre_auth_code");
            Long expiresIn = jsonObject.getLong("expires_in");
            log.info("【微信开放平台获取预授权码】预授权码：【{}】，有效时间：【{}】", preAuthCode, expiresIn);

            //将预授权码放入容器，后期可以优化到缓存Redis
//            wechatOpMap.put(COMPONENT_APP_ID + "_pre_auth_code", preAuthCode);

            if(StringUtils.isNotEmpty(preAuthCode)){
                if(entity==null){
                    entity=new WeixinOpenAccount();
                    entity.setPreAuthCode(preAuthCode);
                    entity.setAppId(wxOpen.getAppId());
                    entity.setAppsecret(wxOpen.getSecret());
                    Long id= RandomUtil.getUniqueNum();
                    entity.setId(String.valueOf(id));
                    entity.setCreateTime(new Date());
                    entity.setCreateBy(entity.getAppId());
                    weixinOpenAccountService.save(entity);
                    log.info("微信第三方添加preAuthCode成功！preAuthCode={}.",new Object[]{preAuthCode});
                }else{
                    entity.setPreAuthCode(preAuthCode);
                    entity.setAppId(wxOpen.getAppId());
                    log.info("===================={}=======================",new Object[]{entity});
                    entity.setUpdateTime(new Date());
                    entity.setUpdateBy(entity.getAppId());
                    weixinOpenAccountService.update(entity);
                    log.info("微信第三方重置preAuthCode成功！preAuthCode={}.",new Object[]{preAuthCode});
                }
            }

            PreAuthCodeResp preAuthCodeResp =new PreAuthCodeResp();
            preAuthCodeResp.setPre_auth_code(preAuthCode);
            preAuthCodeResp.setExpires_in(expiresIn);

            log.info("【微信开放平台获取预授权码成功】");
            return ServerResponseEntity.success( preAuthCodeResp);
        } catch (Exception e) {
            log.info("【微信开放平台获取预授权码异常】处理异常：{}", e);
            return ServerResponseEntity.showFailMsg("微信开放平台获取预授权码异常");
        }
    }

    /**
     * 获取扫码授权链接
     * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/2.0/api/Before_Develop/Authorization_Process_Technical_Description.html
     * 1、构建PC端授权链接的方法
     * https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=xxxx&pre_auth_code=xxxxx&redirect_uri=xxxx&auth_type=xxx。
     * component_appid 	是 	第三方平台方 appid
     * pre_auth_code 	是 	预授权码
     * redirect_uri 	是 	回调 URI（插件版授权页面，无该参数）
     * auth_type 	    是 	要授权的帐号类型：1 则商户点击链接后，手机端仅展示公众号、2 表示仅展示小程序，3 表示公众号和小程序都展示。如果为未指定，则默认小程序和公众号都展示。第三方平台开发者可以使用本字段来控制授权的帐号类型。
     * biz_appid 	    否 	指定授权唯一的小程序或公众号
     * 2、构建移动端授权链接的方法
     * 3、使用插件
     *
     * @return
     */
    @Override
    public ServerResponseEntity getScanCodeAuthUrl() {
        log.info("【微信开放平台获取扫码授权链接】");
        WxOpen wxOpen=getWxOpen();
        WeixinOpenAccount entity = getWeixinOpenAccount(wxOpen.getAppId());
        if(entity==null || entity.getPreAuthCode()==null){
            return ServerResponseEntity.showFailMsg("微信开放平台获取扫码授权链接失败");
        }

        try {
            String componentAppId = wxOpen.getAppId();
//            String preAuthCode = MapUtil.getStr(wechatOpMap, COMPONENT_APP_ID + "_pre_auth_code");
            String preAuthCode = entity.getPreAuthCode();
            //扫码授权回调路径
//            String redirectUri = WechatConstants.redirectUri;
            String redirectUri = wxOpen.getRedirectUri();
            String authType = "3";
            String QRLink = WechatConstants.QRLink;
            log.info("【微信开放平台获取扫码授权链接】QRLink：【{}】", QRLink);

            String scanCodeAuthUrl = QRLink.replaceAll("COMPONENT_APPID", componentAppId).replaceAll("PRE_AUTH_CODE", preAuthCode)
                    .replaceAll("REDIRECT_URI", redirectUri)
                    .replaceAll("AUTH_TYPE", authType);

            log.info("【微信开放平台获取扫码授权链接成功】: 【{}】",scanCodeAuthUrl);
            return ServerResponseEntity.success( scanCodeAuthUrl);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("【微信开放平台获取扫码授权链接异常】处理异常：{}", e.getMessage());
            return ServerResponseEntity.showFailMsg("微信开放平台获取扫码授权链接异常");
        }
    }

    /**
     * 扫码授权回调
     * 用户扫码授权获取授权码，AuthAccessToken，AuthRefreshToken
     * 4.获得pre_auth_code后，按照授权技术流程说明文档 ，引导用户授权后获取authorization_code
     * 5.获得authorization_code后，按照使用授权码换取公众号或小程序的接口调用凭据和授权信息 接口文档 ，调接口获取authorizer_refresh_token
     *
     * @param authCode      授权码
     * @param authExpiresIn 有效期
     * @return
     */
    @Override
    public ServerResponseEntity scanCodecallback(String authCode, Long authExpiresIn) {
        log.info("【微信开放平台用户扫码授权】请求参数，authCode：【{}】，authExpiresIn：【{}】", authCode, authExpiresIn);

        try {
            WxOpen wxOpen=getWxOpen();
            //缓存获取鉴权信息
            WeixinOpenAccount entity=getWeixinOpenAccount(wxOpen.getAppId());
            if(entity==null || entity.getPreAuthCode()==null){
                return ServerResponseEntity.showFailMsg("微信开放平台获取扫码授权失败");
            }
            String componentAccessToken = entity.getComponentAccessToken();
            ComponentAccessTokenResp tokenResp=(ComponentAccessTokenResp)getComponentAccessToken().getData();
            componentAccessToken=tokenResp.getComponent_access_token();

            AccessTokenVo authTokenReq = new AccessTokenVo();
            //第三方平台 appid
            authTokenReq.setComponent_appid(wxOpen.getAppId());
            //授权码, 会在授权成功时返回给第三方平台，详见第三方平台授权流程说明
            authTokenReq.setAuthorization_code(authCode);
            String jsonString = JSON.toJSONString(authTokenReq);

            String accessUrl = WechatUtils.replaceComponentAccessToken(WechatConstants.AUTHORIZER_ACCESS_TOKEN_URL, componentAccessToken);
            log.info("【微信开放平台用户扫码授权】使用授权码获取授权信息，请求参数：【{}】，请求路径：【{}】", jsonString, accessUrl);
            JSONObject jsonObject = WechatUtils.doPoststr(accessUrl, jsonString);
            log.info("【微信开放平台用户扫码授权】使用授权码获取授权信息，响应结果：【{}】", JSON.toJSONString(jsonObject));
            if (jsonObject == null) {
                log.info("【微信开放平台用户扫码授权】使用授权码获取授权信息失败，返回结果为空");
                return ServerResponseEntity.showFailMsg("使用授权码获取授权信息失败，返回结果为空");
            }

            JSONObject authorizationInfo = jsonObject.getJSONObject("authorization_info");
            if (authorizationInfo == null) {
                log.info("【微信开放平台用户扫码授权】使用授权码获取授权信息失败，返回结果为空");
                return ServerResponseEntity.showFailMsg("使用授权码获取授权信息失败，返回结果为空");
            }

            Long expiresIn = authorizationInfo.getLong("expires_in");
            String authorizerAppId = authorizationInfo.getString("authorizer_appid");
            String authorizerAccessToken = authorizationInfo.getString("authorizer_access_token");
            String authorizerRefreshToken = authorizationInfo.getString("authorizer_refresh_token");
            String authorizationCode = authorizationInfo.getString("authorization_code");

            if(entity!=null){
                entity.setAuthorizerAccessToken(authorizerAccessToken);
                entity.setAuthorizerRefreshToken(authorizerRefreshToken);
                entity.setAuthorizerAppId(authorizerAppId);
                entity.setGetAccessTokenTime(new Date());
                entity.setAppId(wxOpen.getAppId());
                log.info("=====微信开放平台用户扫码授权回调==============={}=======================",new Object[]{entity});
                entity.setUpdateTime(new Date());
                entity.setUpdateBy(entity.getAppId());
                weixinOpenAccountService.update(entity);
                log.info("微信第三方重置getAuthAccessToken成功！getAuthAccessToken={}.");
            }

            // 通过第三方token获取公众号信息
//            String getAuthorizerInfoUrl = WechatConstants.AUTHORIZER_INFO_URL.replace("COMPONENT_ACCESS_TOKEN", componentAccessToken);
//            JSONObject j = new JSONObject();
//            // 第三方平台appid
//            j.put("component_appid", wxOpen.getAppId());
//            // 授权用户的appid
//            j.put("authorizer_appid", authorizerAppId);
//            j.put("authorization_code", authorizationCode);
//            j.put("authorizer_refresh_token", authorizerRefreshToken);
//            jsonString=j.toJSONString();
//            JSONObject jsonObj = WechatUtils.doPoststr(getAuthorizerInfoUrl, jsonString);
//            log.info("===========授权回调方法===获取授权公众号详细Info==="+jsonObj.toString()+"===========");

            tokenResp=(ComponentAccessTokenResp)getComponentAccessToken().getData();
            componentAccessToken=tokenResp.getComponent_access_token();
            //获取授权账号信息
            JSONObject j = new JSONObject();
            // 第三方平台appid
            j.put("component_appid", wxOpen.getAppId());
            // 授权用户的appid
            j.put("authorizer_appid", authorizerAppId);
            jsonString=j.toJSONString();
            String apiGetAuthorizerInfo = WechatConstants.API_GET_AUTHORIZER_INFO.replace("COMPONENT_ACCESS_TOKEN", componentAccessToken);
            log.info("获取授权账号信息，请求参数：【{}】，请求路径：【{}】", jsonString, apiGetAuthorizerInfo);
            JSONObject jsonObj = WechatUtils.doPoststr(apiGetAuthorizerInfo, jsonString);
            log.info("===========授权回调方法===获取授权公众号详细Info==="+jsonObj.toString()+"===========");
            if (jsonObj != null && !jsonObj.containsKey("errcode")) {

                WeixinWebApp weixinWebApp = new WeixinWebApp();
                // 保存授权公众号的部分信息
                save(jsonObject, weixinWebApp);

                // 增加授权返回标识，已授权的提示用户更新成功！ 同时保存授权的公众号信息
                callbackUpdate(jsonObj, weixinWebApp);
            }

            log.info("【微信开放平台用户扫码授权】使用授权码获取授权信息成功");
            return ServerResponseEntity.success( jsonObject);
        } catch (Exception e) {
            log.info("【微信开放平台用户扫码授权异常】处理异常：{}", e);
            return ServerResponseEntity.showFailMsg("微信开放平台用户扫码授权异常");
        }
    }


    /****************公众号授权回调保存 start***********************************************/
    private void callbackUpdate(JSONObject jsonObj,WeixinWebApp weixinWebApp){
        try {
            String authorizerInfoStr = jsonObj.getString("authorizer_info");
            String qrcodeUrl = null;
            JSONObject authorizerInfoJson = JSON.parseObject(authorizerInfoStr);
            if(authorizerInfoJson.containsKey("qrcode_url")){
                qrcodeUrl = authorizerInfoJson.getString("qrcode_url");
            }
            String nickName = authorizerInfoJson.getString("nick_name");
            String headImg=null;
            if(authorizerInfoJson.containsKey("head_img")&& StringUtils.isNotEmpty(authorizerInfoJson.getString("head_img"))){
                headImg = authorizerInfoJson.getString("head_img");
                weixinWebApp.setHeadimgurl(headImg);
            }
            String serviceTypeInfo = authorizerInfoJson.getString("service_type_info");
            String verifyTypeInfo = authorizerInfoJson.getString("verify_type_info");
            String userName = authorizerInfoJson.getString("user_name");//公众号 appid
            String businessInfo = authorizerInfoJson.getString("business_info");
            String alias="";
            if(authorizerInfoJson.containsKey("alias")){
                alias = authorizerInfoJson.getString("alias");
            }
            String authorizationInfoS = jsonObj.getString("authorization_info");
            JSONObject authorization_info_json = JSON.parseObject(authorizationInfoS);
            String func_info = authorization_info_json.getString("func_info");
            weixinWebApp.setWeixinNumber(alias);
            weixinWebApp.setBusinessInfo(businessInfo);
            weixinWebApp.setFuncInfo(func_info);
            weixinWebApp.setName(nickName);
//            String fileName = UUID.randomUUID().toString().replace("-", "").toUpperCase()+".jpg";
//            //update-begin--Author:zhaofei  Date: 20191016 for：将微信返回的二维码链接上传云服务器
//            MultipartFile multipartFile = createFileItem(qrcodeUrl,fileName);
//            String fileNames=minioTemplate.uploadStream(WechatConstants.uploadDir, multipartFile.getInputStream(), multipartFile.getContentType());
            //update-end--Author:zhaofei  Date: 20191016 for：将微信返回的二维码链接上传云服务器
            //download(qrcodeUrl, fileName, uploadDir);
            weixinWebApp.setQrcodeimg(qrcodeUrl);
            JSONObject json=JSON.parseObject(serviceTypeInfo);
            if(json!=null&&json.containsKey("id")){
                int accountType = json.getIntValue("id");
                weixinWebApp.setAccountType(String.valueOf(accountType));
//                if(2==accountType){
//                    weixinWebApp.setAccountType("1");
//                }else{
//                    weixinWebApp.setAccountType("2");
//                }
            }
            json=JSON.parseObject(verifyTypeInfo);
            if(json!=null&&json.containsKey("id")){
                int authStatus=json.getIntValue("id");
                if(authStatus==-1){
                    weixinWebApp.setAuthStatus("0");
                }else{
                    weixinWebApp.setAuthStatus("1");
                }
            }
            weixinWebApp.setAppId(userName);
            //获取apiticket
            Map<String, String> apiTicket = AccessTokenUtil.getApiTicket(weixinWebApp.getAccessToken());
            if("true".equals(apiTicket.get("status"))){
                weixinWebApp.setApiticket(apiTicket.get("apiTicket"));
                weixinWebApp.setApiticketGettime(new Date());
                weixinWebApp.setJsapiticket(apiTicket.get("jsApiTicket"));
                weixinWebApp.setJsapiticketGettime(new Date());
            }
            //TODO 没有授权就新增，授权过就修改
            WeixinWebApp webJwid = weixinWebAppService.queryByAppid(userName);
            if(webJwid==null){
                weixinWebApp.setCreateTime(new Date());
                weixinWebApp.setCreateBy(weixinWebApp.getAppId());
                weixinWebApp.setId(RandomUtil.getUniqueNumStr());
                weixinWebApp.setDelFlag(0);
                weixinWebAppService.save(weixinWebApp);
            }else{
                weixinWebApp.setId(webJwid.getId());
                weixinWebApp.setUpdateTime(new Date());
                weixinWebApp.setUpdateBy(weixinWebApp.getAppId());
                weixinWebAppService.update(weixinWebApp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new YAMLException("解析授权信息==UPDATE时发生错误"+e.getMessage());
        }
    }

    /**
     * 保存内容
     * @param jsonObject
     * @param myJwWebJwid
     */
    private void save(JSONObject jsonObject,WeixinWebApp myJwWebJwid) {
        try {
            String authorizationInfoStr = jsonObject.getString("authorization_info");
            JSONObject authorizationInfoJson = JSON.parseObject(authorizationInfoStr);
            String authorizerAppid = null;
            if(authorizationInfoJson.containsKey("authorizer_appid")){
                authorizerAppid=authorizationInfoJson.getString("authorizer_appid");
            }else if(jsonObject.containsKey("authorizer_appid")){
                authorizerAppid = jsonObject.getString("authorizer_appid");
            }
            String authorizerAccessToken = authorizationInfoJson.getString("authorizer_access_token");
            String authorizerRefreshToken = authorizationInfoJson.getString("authorizer_refresh_token");
            String funcInfoStr ="";
            if(authorizationInfoJson.containsKey("func_info")){
                funcInfoStr= authorizationInfoJson.getString("func_info");
            }else if(jsonObject.containsKey("func_info")){
                funcInfoStr= jsonObject.getString("func_info");
            }
            myJwWebJwid.setAuthorizationInfo(authorizationInfoStr);
            myJwWebJwid.setAccessToken(authorizerAccessToken);
            myJwWebJwid.setTokenGettime(new Date());
            myJwWebJwid.setWeixinAppId(authorizerAppid);
            myJwWebJwid.setAuthorizerRefreshToken(authorizerRefreshToken);
            myJwWebJwid.setFuncInfo(funcInfoStr);
            myJwWebJwid.setAuthType("2");
            //授权状态（1正常，2已取消）
            myJwWebJwid.setAuthorizationStatus("1");

        } catch (Exception e) {
            e.printStackTrace();
            throw new YAMLException("解析授权信息==DOADD时发生错误"+e.getMessage());
        }
    }
    /****************公众号授权回调保存 end***********************************************/

    /**
     * 6.获取刷新授权公众号或小程序的接口调用凭据
     * 刷新令牌可以通过定时任务处理
     * 获得authorizer_refresh_token后，按照获取/刷新授权公众号或小程序的接口调用凭据 接口文档 ，调接口获取authorizer_access_token
     */
    @Override
    public ServerResponseEntity getAuthRefreshToken() {
        log.info("【微信开放平台刷新凭据】获取刷新授权公众号或小程序的接口调用凭据");

        try {
            WxOpen wxOpen=getWxOpen();
            WeixinOpenAccount entity = getWeixinOpenAccount(wxOpen.getAppId());
            if(entity==null){
                return ServerResponseEntity.showFailMsg("微信开放平台刷新凭据异常");
            }

            //缓存获取鉴权信息
            String authorizerAccessToken = entity.getAuthorizerAccessToken();
            String authorizerRefreshToken = entity.getAuthorizerRefreshToken();
            String authorizerAppId = entity.getAuthorizerAppId();
            String componentAccessToken = entity.getComponentAccessToken();

            // 构建请求参数
            RefreshTokenVo refreshTokenReq = new RefreshTokenVo();
            refreshTokenReq.setComponent_appid(wxOpen.getAppId());
            refreshTokenReq.setAuthorizer_appid(authorizerAppId);
            refreshTokenReq.setAuthorizer_refresh_token(authorizerRefreshToken);
            String jsonString = JSON.toJSONString(refreshTokenReq);

            String refreshUrl = WechatUtils.replaceComponentAccessToken(WechatConstants.AUTHORIZER_REFRESH_TOKEN_URL, componentAccessToken);
            log.info("【微信开放平台刷新凭据】请求参数：【{}】，请求路径：【{}】", jsonString, refreshUrl);
            JSONObject jsonObject = WechatUtils.doPoststr(refreshUrl, jsonString);
            log.info("【微信开放平台刷新凭据】响应结果：【{}】", JSON.toJSONString(jsonObject));

            if (jsonObject == null) {
                log.info("【微信开放平台刷新凭据】使用授权码获取授权信息失败，返回结果为空");
                return ServerResponseEntity.showFailMsg("使用授权码获取授权信息失败");
            }

            Long expiresInNew = jsonObject.getLong("expires_in");
            String authorizerAppIdNew = refreshTokenReq.getAuthorizer_appid();
            String authorizerAccessTokenNew = jsonObject.getString("authorizer_access_token");
            String authorizerRefreshTokenNew = jsonObject.getString("authorizer_refresh_token");

            //缓存授权信息,覆盖之前的token
            entity.setAuthorizerAppId(authorizerAppIdNew);
            entity.setAuthorizerAccessToken(authorizerAccessTokenNew);
            entity.setAuthorizerRefreshToken(authorizerRefreshTokenNew);
            entity.setUpdateTime(new Date());
            entity.setUpdateBy(entity.getAppId());
            weixinOpenAccountService.update(entity);

            log.info("【微信开放平台刷新凭据成功】");
            return ServerResponseEntity.success( jsonObject);
        } catch (Exception e) {
            log.info("【微信开放平台刷新凭据异常】处理异常：{}", e);
            return ServerResponseEntity.showFailMsg("微信开放平台刷新凭据异常");
        }
    }

    /**
     * url转变为 MultipartFile对象
     * @param url
     * @param fileName
     * @return
     * @throws Exception
     */
    private static MultipartFile createFileItem(String url, String fileName) throws Exception{
        FileItem item = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            //设置应用程序要从网络连接读取数据
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();

                FileItemFactory factory = new DiskFileItemFactory(16, null);
                String textFieldName = "uploadfile";
                item = factory.createItem(textFieldName, ContentType.APPLICATION_OCTET_STREAM.toString(), false, fileName);
                OutputStream os = item.getOutputStream();

                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }

        return new CommonsMultipartFile(item);
    }


//
//    /*************微信开放平台消息事件接收URL start************************************************************/
//
//    public void replyEventMessage(HttpServletRequest request, HttpServletResponse response, String event, String toUserName, String fromUserName) throws DocumentException, IOException {
//        String content = event + "from_callback";
//        replyTextMessage(request,response,content,toUserName,fromUserName);
//    }
//
//    public void processTextMessage(HttpServletRequest request, HttpServletResponse response,String content,String toUserName, String fromUserName) throws IOException, DocumentException{
//        if("TESTCOMPONENT_MSG_TYPE_TEXT".equals(content)){
//            String returnContent = content+"_callback";
//            replyTextMessage(request,response,returnContent,toUserName,fromUserName);
//        }else if(StringUtils.startsWithIgnoreCase(content, "QUERY_AUTH_CODE")){
//            output(response, "");
//            WxOpen wxOpen=getWxOpen();
//            WeixinOpenAccount weixinOpenAccount = this.getWeixinOpenAccount(wxOpen.getAppId());
//            //接下来客服API再回复一次消息
//            replyApiTextMessage(weixinOpenAccount.getComponentAccessToken(),content.split(":")[1],fromUserName);
//        }else{
//            //匹配关键词回复
//
//        }
//    }
//
//    public void replyApiTextMessage(String componentAccessToken,String authCode, String fromUserName) throws DocumentException, IOException {
//        // 得到微信授权成功的消息后，应该立刻进行处理！！相关信息只会在首次授权的时候推送过来
//        log.info("------step.1----使用客服消息接口回复粉丝----逻辑开始-------------------------");
//        try {
//            WxOpen wxOpen=getWxOpen();
//            log.info("------step.2----使用客服消息接口回复粉丝------- component_access_token = "+componentAccessToken + "---------authorization_code = "+authCode);
//            JSONObject authorizationInfoJson = WechatOpenApi.getApiQueryAuthInfo(wxOpen.getAppId(), authCode, componentAccessToken);
//            log.info("------step.3----使用客服消息接口回复粉丝-------------- 获取authorizationInfoJson = "+authorizationInfoJson);
//            JSONObject infoJson = authorizationInfoJson.getJSONObject("authorization_info");
//            String authorizer_access_token = infoJson.getString("authorizer_access_token");
//
//
//            JSONObject obj = new JSONObject();
//            Map<String,Object> msgMap = new HashMap<String,Object>();
//            String msg = authCode + "_from_api";
//            msgMap.put("content", msg);
//
//            obj.put("touser", fromUserName);
//            obj.put("msgtype", "text");
//            obj.put("text", msgMap);
//            WechatOpenApi.sendMessage(obj, authorizer_access_token);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 回复微信服务器"文本消息"
//     * @param request
//     * @param response
//     * @param content
//     * @param toUserName
//     * @param fromUserName
//     * @throws IOException
//     */
//    public void replyTextMessage(HttpServletRequest request, HttpServletResponse response, String content, String toUserName, String fromUserName) throws DocumentException, IOException {
//        Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
//        StringBuffer sb = new StringBuffer();
//        sb.append("<xml>");
//        sb.append("<ToUserName><![CDATA["+fromUserName+"]]></ToUserName>");
//        sb.append("<FromUserName><![CDATA["+toUserName+"]]></FromUserName>");
//        sb.append("<CreateTime>"+createTime+"</CreateTime>");
//        sb.append("<MsgType><![CDATA[text]]></MsgType>");
//        sb.append("<Content><![CDATA["+content+"]]></Content>");
//        sb.append("</xml>");
//        String replyMsg = sb.toString();
//
//        String returnvaleue = "";
//        try {
//            WxOpen wxOpen=getWxOpen();
//            //这个类是微信官网提供的解密类,需要用到消息校验Token 消息加密Key和服务平台appid
//            WXBizMsgCrypt pc = new WXBizMsgCrypt(wxOpen.getToken(), wxOpen.getAesKey(), wxOpen.getAppId());
//            //加密模式：需要解密
//            returnvaleue = pc.encryptMsg(replyMsg,  createTime.toString(),"easemob");
//        } catch (AesException e) {
//            e.printStackTrace();
//        }
//        //TODO 暂时去掉第三方回复消息
//        output(response, "");
//    }
//    /**
//     * 工具类：回复微信服务器"文本消息"
//     * @param response
//     * @param returnvaleue
//     */
//    public void output(HttpServletResponse response, String returnvaleue){
//        try {
//            PrintWriter pw = response.getWriter();
//            pw.write(returnvaleue);
//            pw.flush();
//            pw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*************微信开放平台消息事件接收URL end************************************************************/


    public static void main(String[] strings){
//        AccessTokenVo authTokenReq = new AccessTokenVo();
//        //第三方平台 appid
//        authTokenReq.setComponent_appid("wx5164815fb06a2dd5");
//        //授权码, 会在授权成功时返回给第三方平台，详见第三方平台授权流程说明
//        authTokenReq.setAuthorization_code("queryauthcode@@@gGO6QhU-7bm8RRUVkfySa4rZGwbP5Vld9j2ZxuhgsIhvjpxRjtihI9xjb07anRemql2q1e9YWXVRV2gASyTsQg");
//        String jsonString = JSON.toJSONString(authTokenReq);

        // 通过第三方token获取公众号信息
        String componentAccessToken="53_cVK179hTZKSmeYoVDd4CFxU512Zo8-7r_gH6HlqCVBmS42ja7Fp5HFtCZw2UPOasoKQyrCq87LXy2EsqevxTcRhUiRvJC_wnP8c2zRTbmC-WmU1Z9fyEfBxfW5DnElqH6KAnsiGulbNEkVjvIUQdAHAUGC";
        String getAuthorizerInfoUrl = WechatConstants.AUTHORIZER_INFO_URL.replace("COMPONENT_ACCESS_TOKEN", componentAccessToken);
        JSONObject j = new JSONObject();
        // 第三方平台appid
        j.put("component_appid", "wx5164815fb06a2dd5");
        // 授权用户的appid
        j.put("authorizer_appid", "wxeebaa266872cb997");
        j.put("authorization_code", "queryauthcode@@@gGO6QhU-7bm8RRUVkfySa4rZGwbP5Vld9j2ZxuhgsIhvjpxRjtihI9xjb07anRemql2q1e9YWXVRV2gASyTsQg");
        j.put("authorizer_refresh_token", "refreshtoken@@@MJ3NL5LdJ1abrtMXxEh44FHrAnVNZBaiLKFcbD2cFKg");
        String jsonString=j.toJSONString();
        JSONObject jsonObj = WechatUtils.doPoststr(getAuthorizerInfoUrl, jsonString);
        log.info("===========授权回调方法===获取授权公众号详细Info==="+jsonObj.toString()+"===========");
//        componentAccessToken=jsonObj.get("").toString();

        j = new JSONObject();
        // 第三方平台appid
        j.put("component_appid", "wx5164815fb06a2dd5");
        // 授权用户的appid
        j.put("authorizer_appid", "wxeebaa266872cb997");
        String apiGetAuthorizerInfo = WechatConstants.API_GET_AUTHORIZER_INFO.replace("COMPONENT_ACCESS_TOKEN", componentAccessToken);
        jsonObj = WechatUtils.doPoststr(apiGetAuthorizerInfo, jsonString);
        log.info("===========授权回调方法===获取授权公众号详细Info==="+jsonObj.toString()+"===========");
    }
}
