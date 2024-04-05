package com.mall4j.cloud.biz.service.channels;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.biz.wx.wx.util.WXBizMsgCrypt;
import com.mall4j.cloud.biz.controller.wx.live.event.NotifyEventManager;
import com.mall4j.cloud.common.bean.WxMiniApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChannelsNotifyService {

    @Autowired
    private FeignShopConfig feignShopConfig;

    private WxMiniApp getWxOpen(){
        WxMiniApp wxMiniApp = feignShopConfig.getWxEcApp();
        return wxMiniApp;
    }

    public String authEvent(String signature, String timestamp, String nonce, String echostr, String encryptType, String msgSignature, String postdata) {

        log.info("【视频号4回调事件接收URL】请求参数：signature：【{}】，timestamp：【{}】，nonce：【{}】，echostr：【{}】，encryptType：【{}】，msgSignature：【{}】，postdata：【{}】", signature, timestamp, nonce, echostr, encryptType, msgSignature, postdata);
        //配置url验证
        if (StrUtil.isEmpty(postdata)) {
            log.info("【视频号4回调事件接收URL】消息体为空直接返回验证成功，返回随机数，echostr：{}", echostr);
            return echostr;
        }
        String jsonString = "";
        //接收事件url
        try{
            //这个类是微信官网提供的解密类,需要用到消息校验Token 消息加密Key和服务平台appid
            WxMiniApp wxOpen = getWxOpen();
            log.info("wxOpen:[{}]",wxOpen);
            WXBizMsgCrypt pc = new WXBizMsgCrypt(wxOpen.getToken(), wxOpen.getAesKey(), wxOpen.getAppId());
//            //加密模式：需要解密
            jsonString = pc.decryptMsgByJson(msgSignature, timestamp, nonce, postdata);
            log.info("【视频号4回调事件接收URL】解密后 jsonString：【{}】", jsonString);

//            //将xml转为map
//            Map<String, String> resultMap = WechatUtils.xmlToMap(xml);
//            log.info("【小程序回调事件接收URL】resultMap：【{}】", JSON.toJSONString(resultMap));


            //授权事件类型
            String event = JSON.parseObject(jsonString).getString("Event");
            NotifyEventManager.getInstance().getNotifyEventService(event).doEvent(jsonString);
            return "success";
        }catch (Exception e){
            log.error("【视频号4回调事件接收URL】执行异常。异常:{},接收参数:{}",e.getMessage(),jsonString);
            e.printStackTrace();
            //throw new RuntimeException(e.getMessage());
        }finally {
            log.info("【视频号4回调事件接收URL】执行结束。接收参数:{}",jsonString);
        }

        return "error";
    }

    public String authEventTest(String postdata) {

        log.info("【视频号4回调事件接收URL】请求参数：postdata：【{}】",  postdata);
        String jsonString = postdata;
        //接收事件url
        try{
            //这个类是微信官网提供的解密类,需要用到消息校验Token 消息加密Key和服务平台appid
            WxMiniApp wxOpen = getWxOpen();
            WXBizMsgCrypt pc = new WXBizMsgCrypt(wxOpen.getToken(), wxOpen.getAesKey(), wxOpen.getAppId());
//            //加密模式：需要解密
//            jsonString = pc.decryptMsg(msgSignature, timestamp, nonce, postdata);
//            log.info("【视频号回调事件接收URL】解密后 jsonString：【{}】", jsonString);
//
//            //将xml转为map
//            Map<String, String> resultMap = WechatUtils.xmlToMap(xml);
//            log.info("【小程序回调事件接收URL】resultMap：【{}】", JSON.toJSONString(resultMap));


            //授权事件类型
            String event = JSON.parseObject(jsonString).getString("Event");
            NotifyEventManager.getInstance().getNotifyEventService(event).doEvent(jsonString);
            return "success";
        }catch (Exception e){
            log.error("【视频号4回调事件接收URL】执行异常。异常:{},接收参数:{}",e.getMessage(),jsonString);
        }finally {
            log.info("【视频号4回调事件接收URL】执行结束。接收参数:{}",jsonString);
        }

        return "error";
    }


}
