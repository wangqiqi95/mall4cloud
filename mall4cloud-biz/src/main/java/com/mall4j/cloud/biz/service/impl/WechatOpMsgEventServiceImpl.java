package com.mall4j.cloud.biz.service.impl;


import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.biz.controller.wx.open.WechatOpController;
import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.wx.util.WXBizMsgCrypt;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.wx.wx.constant.WxMessageConstants;
import com.mall4j.cloud.biz.service.WechatEventHandler;
import com.mall4j.cloud.biz.service.WechatOpMsgEventService;
import com.mall4j.cloud.common.bean.WxOpen;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;

/**
 * 消息事件推送处理
 * @Date 2021年12月30日, 0030 14:50
 * @Created by eury
 */
//@Slf4j
@Service
public class WechatOpMsgEventServiceImpl implements WechatOpMsgEventService {
    private static final Logger log = LoggerFactory.getLogger(WechatOpMsgEventServiceImpl.class);

    @Autowired
    private FeignShopConfig feignShopConfig;

    /**
     * 消息监听执行
     * xml消息响应微信服务器需要将消息体加密，否则响应不成功
     * 第三方平台配置：WxOpen
     * 消息加密/解密：WXBizMsgCrypt
     * @param xml
     * @param resultMap
     * @param request
     * @param response
     * @return
     */
    @Override
    public String doBackEvent(String xml,Map<String, String> resultMap,
                        HttpServletRequest request,
                        HttpServletResponse response){
        try {
            String msgType = MapUtil.getStr(resultMap, "MsgType");
            String event = MapUtil.getStr(resultMap,"Event");

            WxCpMsgDTO msgDTO=new WxCpMsgDTO();
            msgDTO.setXml(xml);
            WxOpen wxOpen=getWxOpen();//第三方平台信息
            //这个类是微信官网提供的解密类,需要用到消息校验Token 消息加密Key和服务平台appid
            WXBizMsgCrypt pc = new WXBizMsgCrypt(wxOpen.getToken(), wxOpen.getAesKey(), wxOpen.getAppId());

            if(WxMessageConstants.REQ_MESSAGE_TYPE_EVENT.equals(msgType)){ //事件推送
                // 微信开放平台消息事件分发
                if(WxMessageConstants.EVENT_TYPE_SUBSCRIBE.equals(event)){//关注
                    WechatEventHandler handler = SpringContextUtils.getBean("wx_subscribe_event_handler", WechatEventHandler.class);
                    return handler.exec(msgDTO);
                }else if(WxMessageConstants.EVENT_TYPE_UNSUBSCRIBE.equals(event)) {//取消关注
                    WechatEventHandler handler = SpringContextUtils.getBean("wx_unsubscribe_event_handler", WechatEventHandler.class);
                    return handler.exec(msgDTO);
                }else if(WxMessageConstants.EVENT_TYPE_SCAN.equals(event)) {//扫码
                    WechatEventHandler handler = SpringContextUtils.getBean("wx_scan_event_handler", WechatEventHandler.class);
                    return handler.exec(msgDTO);
                }else if(WxMessageConstants.EVENT_TYPE_CLICK.equals(event)) {//菜单点击
                    WechatEventHandler handler = SpringContextUtils.getBean("wx_click_event_handler", WechatEventHandler.class);
                    return handler.exec(msgDTO);
                }else if(WxMessageConstants.EVENT_TEMPLATESENDJOBFINISH.equals(event)) {//事件为模板消息发送结束
                    WechatEventHandler handler = SpringContextUtils.getBean("wx_templatesendjobfinish_event_handler", WechatEventHandler.class);
                    return handler.exec(msgDTO);
                }else if(WxMessageConstants.EVENT_TYPE_VIEW.equals(event)){
                    return WechatConstants.TICKET_SUCCESS;
                }else if(WxMessageConstants.view_miniprogram.equals(event)){
                return WechatConstants.TICKET_SUCCESS;
                }else{
                    // 视频号事件处理
                    if (SpringContextUtils.containsBean(event + "Handler")) {
                        WechatEventHandler handler = SpringContextUtils.getBean(event + "Handler", WechatEventHandler.class);
                        return handler.exec(msgDTO);
                    }else{
                        //默认回复
                        return replyEventMessage(xml,pc);
                    }
                }
            }else if(WxMessageConstants.REQ_MESSAGE_TYPE_TEXT.equals(msgType)){//文本消息
                String content = MapUtil.getStr(resultMap,"Content");
                return processTextMessage(xml,content,pc,response);
            }else{
                //默认回复
                return replyEventMessage(xml,pc);
            }
        }catch (Exception e){
            log.info("【微信开放平台消息事件接收URL】处理异常：{}", e);
            return WechatConstants.FAILED;
        }
    }

    private String getTimeStamp(){
        Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
        return createTime.toString();
    }

    /**
     * 第三方平台账号信息
     * @return
     */
    WxOpen getWxOpen(){
        String json=RedisUtil.get(WechatConstants.WX_OPEN_INFO);
        if(StringUtils.isNotEmpty(json)){
            log.info("redis content WxOpenInfo----->"+json);
            return JSON.parseObject(json,WxOpen.class);
        }
        WxOpen wxOpen=feignShopConfig.getWxOpen();
        RedisUtil.set(WechatConstants.WX_OPEN_INFO, JSON.toJSONString(wxOpen), WechatConstants.WXOPEN_EXPIRE_TIME);
        return wxOpen;
    }

    /*************微信开放平台消息事件接收URL start************************************************************/

    public String replyEventMessage(String xml,WXBizMsgCrypt pc) throws Exception{
        WechatEventHandler handler = SpringContextUtils.getBean("wx_default_event_handler", WechatEventHandler.class);
        WxCpMsgDTO msgDTO=new WxCpMsgDTO();
        msgDTO.setXml(xml);
        String replyMsg=handler.exec(msgDTO);
        //加密模式：需要解密
        String returnvaleue = pc.encryptMsg(replyMsg,  getTimeStamp(),RandomUtil.getRandomStr(7));
        return returnvaleue;
    }

    public String processTextMessage(String xml,String content,WXBizMsgCrypt pc,HttpServletResponse response) throws Exception{
        if("TESTCOMPONENT_MSG_TYPE_TEXT".equals(content)){//普通消息验证
            WechatEventHandler handler = SpringContextUtils.getBean("wx_TESTCOMPONENT_MSG_TYPE_TEXT_event_handler", WechatEventHandler.class);
            WxCpMsgDTO msgDTO=new WxCpMsgDTO();
            msgDTO.setXml(xml);
            String replyMsg=handler.exec(msgDTO);
            //加密模式：需要解密
            String returnvaleue = pc.encryptMsg(replyMsg,  getTimeStamp(),RandomUtil.getRandomStr(7));
            log.info("--TESTCOMPONENT_MSG_TYPE_TEXT--{}",returnvaleue);
            return returnvaleue;
        }else if(StringUtils.startsWithIgnoreCase(content, "QUERY_AUTH_CODE")){//api文本消息验证
            output(response, "");
            WechatEventHandler handler = SpringContextUtils.getBean("wx_QUERY_AUTH_CODE_event_handler", WechatEventHandler.class);
            WxCpMsgDTO msgDTO=new WxCpMsgDTO();
            msgDTO.setXml(xml);
            String replyMsg=handler.exec(msgDTO);
            //加密模式：需要解密
            String returnvaleue = pc.encryptMsg(replyMsg,  getTimeStamp(),RandomUtil.getRandomStr(7));
            return returnvaleue;
        }else{
            //匹配关键词回复
            WechatEventHandler handler = SpringContextUtils.getBean("wx_auto_key_word_handler", WechatEventHandler.class);
            WxCpMsgDTO msgDTO=new WxCpMsgDTO();
            msgDTO.setXml(xml);
            return handler.exec(msgDTO);
//            WechatEventHandler handler = SpringContextUtils.getBean("wx_auto_key_word_handler", WechatEventHandler.class);
//            String replyMsg=handler.exec(xml);
//            //加密模式：需要解密
//            String returnvaleue = pc.encryptMsg(replyMsg,  getTimeStamp(),RandomUtil.getRandomStr(7));
//            return returnvaleue;
        }
    }

    /**
     * 工具类：回复微信服务器"文本消息"
     * @param response
     * @param returnvaleue
     */
    public void output(HttpServletResponse response, String returnvaleue){
        try {
            PrintWriter pw = response.getWriter();
            pw.write(returnvaleue);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*************微信开放平台消息事件接收URL end************************************************************/
}
