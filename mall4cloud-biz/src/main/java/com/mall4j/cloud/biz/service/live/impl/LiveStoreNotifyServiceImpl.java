package com.mall4j.cloud.biz.service.live.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.biz.wx.wx.util.WXBizMsgCrypt;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.controller.wx.live.event.NotifyEventManager;
import com.mall4j.cloud.biz.service.live.LiveStoreNotifyService;
import com.mall4j.cloud.common.bean.WxOpen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class LiveStoreNotifyServiceImpl implements LiveStoreNotifyService {

    @Autowired
    private FeignShopConfig feignShopConfig;

    private WxOpen getWxOpen(){
        WxOpen wxOpen=feignShopConfig.getWxOpen();
        return wxOpen;
    }

    @Override
    public String authEvent(String signature, String timestamp, String nonce, String echostr, String encryptType, String msgSignature, String postdata) {

        log.info("【小程序回调事件接收URL】请求参数：signature：【{}】，timestamp：【{}】，nonce：【{}】，echostr：【{}】，encryptType：【{}】，msgSignature：【{}】，postdata：【{}】", signature, timestamp, nonce, echostr, encryptType, msgSignature, postdata);
        //配置url验证
        if (StrUtil.isEmpty(postdata)) {
            log.info("【小程序回调事件接收URL】消息体为空直接返回验证成功，返回随机数，echostr：{}", echostr);
            return echostr;
        }
        String xml = "";
        //接收事件url
        try{
            //这个类是微信官网提供的解密类,需要用到消息校验Token 消息加密Key和服务平台appid
            WxOpen wxOpen=getWxOpen();
            WXBizMsgCrypt pc = new WXBizMsgCrypt(wxOpen.getToken(), wxOpen.getAesKey(), wxOpen.getAppId());
//            //加密模式：需要解密
            xml = pc.decryptMsg(msgSignature, timestamp, nonce, postdata);
            log.info("【小程序回调事件接收URL】解密后xml：【{}】", xml);

            //将xml转为map
            Map<String, String> resultMap = WechatUtils.xmlToMap(xml);
            log.info("【小程序回调事件接收URL】resultMap：【{}】", JSON.toJSONString(resultMap));

            //授权事件类型
            String event = MapUtil.getStr(resultMap, "Event");
            //如果event为空，可能为客服消息，将消息转发到客服
            if(StrUtil.isEmpty(event)){
                return returnKefuString(resultMap);
            }
            NotifyEventManager.getInstance().getNotifyEventService(event).doEvent(xml);
            return "success";
        }catch (Exception e){
            log.error("【小程序回调事件接收URL】执行异常。异常:{},接收参数:{}",e.getMessage(),xml);
        }finally {
            log.info("【小程序回调事件接收URL】执行结束。接收参数:{}",xml);
        }

        return "error";
    }

    private String returnKefuString(Map<String, String> resultMap){

        String xml = "<xml> \n" +
                "  <ToUserName><![CDATA[{}]]></ToUserName>  \n" +
                "  <FromUserName><![CDATA[{}]]></FromUserName>  \n" +
                "  <CreateTime>{}</CreateTime>  \n" +
                "  <MsgType><![CDATA[transfer_customer_service]]></MsgType>\n" +
                "</xml>";
        String ToUserName = resultMap.get("ToUserName");
        String FromUserName = resultMap.get("FromUserName");
        String CreateTime = resultMap.get("CreateTime");
        String returnStr = StrUtil.format(xml,FromUserName,ToUserName,CreateTime);
        log.info("转发客服消息，到小程序客服。返回字符串:{}",returnStr);
        return returnStr;
    }

//    public static void main(String[] args) throws Exception {
//
//        String xml = "<xml>\n" +
//                "<ToUserName><![CDATA[gh_b7fbaed1e621]]></ToUserName>\n" +
//                "<FromUserName><![CDATA[opnL_42_XuixEj3qefQsp5P76SJs]]></FromUserName>\n" +
//                "<CreateTime>1656406428</CreateTime>\n" +
//                "<MsgType><![CDATA[text]]></MsgType>\n" +
//                "<Content><![CDATA[123]]></Content>\n" +
//                "<MsgId>23714037122341937</MsgId>\n" +
//                "</xml>";
//
//        Map<String, String> resultMap = WechatUtils.xmlToMap(xml);
//
//        String xml1 = "<xml> \n" +
//                "  <ToUserName><![CDATA[{}]]></ToUserName>  \n" +
//                "  <FromUserName><![CDATA[{}]]></FromUserName>  \n" +
//                "  <CreateTime>{}</CreateTime>  \n" +
//                "  <MsgType><![CDATA[transfer_customer_service]]></MsgType>\n" +
//                "</xml>";
//        String ToUserName = resultMap.get("ToUserName");
//        String FromUserName = resultMap.get("FromUserName");
//        String CreateTime = resultMap.get("CreateTime");
//        System.out.println(StrUtil.format(xml1,ToUserName,FromUserName,CreateTime));
//    }



    @Override
    public String authEventTest(String postdata) {

        String xml = postdata;
        //接收事件url
        try{
            log.info("【小程序回调事件接收URL】解密后xml：【{}】", xml);

            //将xml转为map
            Map<String, String> resultMap = WechatUtils.xmlToMap(xml);
            log.info("【小程序回调事件接收URL】resultMap：【{}】", JSON.toJSONString(resultMap));

            //授权事件类型
            String event = MapUtil.getStr(resultMap, "Event");
            NotifyEventManager.getInstance().getNotifyEventService(event).doEvent(xml);
            return "success";
        }catch (Exception e){
            log.error("【小程序回调事件接收URL】执行异常。异常:{},接收参数:{}",e.getMessage(),xml);
        }finally {
            log.info("【小程序回调事件接收URL】执行结束。接收参数:{}",xml);
        }

        return "error";
    }

//    public static void main(String[] args) throws Exception {
//        String xml = "<xml>     \n" +
//                "     <ToUserName>gh_abcdefg</ToUserName> \n" +
//                "     <FromUserName>oABCD</FromUserName>      \n" +
//                "     <CreateTime>12344555555</CreateTime>     \n" +
//                "     <MsgType>event</MsgType>      \n" +
//                "     <Event>open_product_order_pay</Event>\n" +
//                "     <order_info>\n" +
//                "          <out_order_id>123456</out_order_id>\n" +
//                "          <order_id>1234567</order_id>\n" +
//                "          <transaction_id>42000000123123</transaction_id>\n" +
//                "          <pay_time>2021-12-30 22:31:00</pay_time>\n" +
//                "     </order_info>\n" +
//                "</xml>";
//
////        Map<String, String> resultMap = WechatUtils.xmlToMap(xml);
//
//        System.out.println(11);
//    }
}
