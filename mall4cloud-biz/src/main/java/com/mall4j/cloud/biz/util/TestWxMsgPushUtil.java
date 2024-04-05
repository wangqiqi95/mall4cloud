package com.mall4j.cloud.biz.util;

import cn.hutool.http.HttpUtil;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Component
@RefreshScope
public class TestWxMsgPushUtil {

//    @Value("${scrm.biz.openPushProdCpMsg}")
    private boolean openPushProdCpMsg=false;

    private static final Logger logger = LoggerFactory.getLogger(TestWxMsgPushUtil.class);

    private static String DOMAIN="https://scrmapi-prod.scrm.editage.cn/scrm_biz/p/cp/test/msg/push";
    private static String DOMAIN_EXTERNALCONTACTCHANGEHANDLER=DOMAIN+"/ua/ExternalContactChangeHandler";
    private static String DOMAIN_MSGHANDLER=DOMAIN+"/ua/MsgHandler";
    private static String DOMAIN_CHATCHANGEHANDLER=DOMAIN+"/ua/ChatChangeHandler";
    private static String DOMAIN_CONTACTCHANGEHANDLER=DOMAIN+"/ua/ContactChangeHandler";
    private static String DOMAIN_SESSION_MSG=DOMAIN+"/ua/cp/session/message/callback?msg_signature=MSG_SIGNATURE&timestamp=TIMESTAMP&nonce=NONCE";

    public static void main(String[] strings){
        String json="{\"agentId\":\"1000002\",\"allFieldsMap\":{\"ChangeType\":\"del_follow_user\",\"UserID\":\"354\",\"CreateTime\":\"1709536590\",\"Event\":\"change_external_contact\",\"ExternalUserID\":\"wmmXETLgAAHPhpWEEFswUlsJ5y7m9-RQ\",\"ToUserName\":\"wwf98894bf27379e85\",\"FromUserName\":\"sys\",\"MsgType\":\"event\"},\"approvalInfo\":{},\"changeType\":\"del_follow_user\",\"createTime\":1709536590,\"event\":\"change_external_contact\",\"extAttrs\":{\"items\":[]},\"externalUserId\":\"wmmXETLgAAHPhpWEEFswUlsJ5y7m9-RQ\",\"fromUserName\":\"sys\",\"msgType\":\"event\",\"scanCodeInfo\":{},\"sendLocationInfo\":{},\"sendPicsInfo\":{\"picList\":[]},\"toUserName\":\"wwf98894bf27379e85\",\"userId\":\"354\"}";

        TestWxMsgPushUtil testWxMsgPushUtil=new TestWxMsgPushUtil();
        testWxMsgPushUtil.ExternalContactChangeHandler(json);
    }

    @Async
    public void ExternalContactChangeHandler(String body){
        if(!openPushProdCpMsg){
            return;
        }
        long start = System.currentTimeMillis();
        String result=null;
        try {
            HttpUtil.post(DOMAIN_EXTERNALCONTACTCHANGEHANDLER,body);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("ExternalContactChangeHandler 异常，url为:{},  json参数为:{}, 异常信息:{}, 共耗时: {}",DOMAIN_EXTERNALCONTACTCHANGEHANDLER, body, e,
                    System.currentTimeMillis() - start);
        }finally {
            logger.info("ExternalContactChangeHandler 结束，url为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", DOMAIN_EXTERNALCONTACTCHANGEHANDLER, body, result,
                    System.currentTimeMillis() - start);
        }
    }

    @Async
    public  void MsgHandler(String body){
        if(!openPushProdCpMsg){
            return;
        }
        long start = System.currentTimeMillis();
        String result=null;
        try {
            HttpUtil.post(DOMAIN_MSGHANDLER,body);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("MsgHandler 异常，url为:{},  json参数为:{}, 异常信息:{}, 共耗时: {}",DOMAIN_MSGHANDLER, body, e,
                    System.currentTimeMillis() - start);
        }finally {
            logger.info("MsgHandler 结束，url为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", DOMAIN_MSGHANDLER, body, result,
                    System.currentTimeMillis() - start);
        }
    }

    @Async
    public  void ChatChangeHandler(String body){
        if(!openPushProdCpMsg){
            return;
        }
        long start = System.currentTimeMillis();
        String result=null;
        try {
            HttpUtil.post(DOMAIN_CHATCHANGEHANDLER,body);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("ChatChangeHandler 异常，url为:{},  json参数为:{}, 异常信息:{}, 共耗时: {}",DOMAIN_CHATCHANGEHANDLER, body, e,
                    System.currentTimeMillis() - start);
        }finally {
            logger.info("ChatChangeHandler 结束，url为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", DOMAIN_CHATCHANGEHANDLER, body, result,
                    System.currentTimeMillis() - start);
        }
    }

    @Async
    public  void ContactChangeHandler(String body){
        if(!openPushProdCpMsg){
            return;
        }
        long start = System.currentTimeMillis();
        String result=null;
        try {
            HttpUtil.post(DOMAIN_CONTACTCHANGEHANDLER,body);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("ContactChangeHandler 异常，url为:{},  json参数为:{}, 异常信息:{}, 共耗时: {}",DOMAIN_CONTACTCHANGEHANDLER, body, e,
                    System.currentTimeMillis() - start);
        }finally {
            logger.info("ContactChangeHandler 结束，url为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", DOMAIN_CONTACTCHANGEHANDLER, body, result,
                    System.currentTimeMillis() - start);
        }
    }

    @Async
    public  void msgCallback(String sRespData,
                                   String msgSignature,
                                   String timestamp,
                                   String nonce){
        if(!openPushProdCpMsg){
            return;
        }
        long start = System.currentTimeMillis();
        String result=null;
        try {
            DOMAIN_SESSION_MSG=DOMAIN_SESSION_MSG.replace("MSG_SIGNATURE",msgSignature)
            .replace("TIMESTAMP",timestamp).replace("NONCE",nonce);
            HttpUtil.post(DOMAIN_SESSION_MSG,sRespData);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("msgCallback 异常，url为:{},  json参数为:{}, 异常信息:{}, 共耗时: {}",DOMAIN_CONTACTCHANGEHANDLER, sRespData, e,
                    System.currentTimeMillis() - start);
        }finally {
            logger.info("msgCallback 结束，url为:{}, json参数为:{}, 请求响应为:{}, 共耗时: {}", DOMAIN_CONTACTCHANGEHANDLER, sRespData, result,
                    System.currentTimeMillis() - start);
        }
    }

}
