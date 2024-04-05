package com.mall4j.cloud.biz.service.impl.handler;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.service.WechatEventHandler;
import com.mall4j.cloud.common.bean.WxOpen;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * 测试回复文本消息
 */
@Service("wx_TESTCOMPONENT_MSG_TYPE_TEXT_event_handler")
@Slf4j
public class WxTestComplateMsgTypeTextEventHandler implements WechatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxTestComplateMsgTypeTextEventHandler.class);

    @Autowired
    private FeignShopConfig feignShopConfig;

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws WxErrorException {
        String xml=msgDTO.getXml();
        logger.info("WxTestComplateMsgTypeTextEventHandler------->exec："+xml);
        WxMpXmlMessage wxMessage=WechatUtils.parseXmlMessage(xml);
        try {
            String content = wxMessage.getContent()+"_callback";
            Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
            StringBuffer sb = new StringBuffer();
            sb.append("<xml>");
            sb.append("<ToUserName><![CDATA["+wxMessage.getFromUser()+"]]></ToUserName>");
            sb.append("<FromUserName><![CDATA["+wxMessage.getToUser()+"]]></FromUserName>");
            sb.append("<CreateTime>"+createTime+"</CreateTime>");
            sb.append("<MsgType><![CDATA[text]]></MsgType>");
            sb.append("<Content><![CDATA["+content+"]]></Content>");
            sb.append("</xml>");
            String replyMsg = sb.toString();
            logger.info("WxTestComplateMsgTypeTextEventHandler------->exec back replyMsg："+replyMsg);
//            String returnvaleue = "";
//            try {
//                WxOpen wxOpen=getWxOpen();
//                //这个类是微信官网提供的解密类,需要用到消息校验Token 消息加密Key和服务平台appid
//                WXBizMsgCrypt pc = new WXBizMsgCrypt(wxOpen.getToken(), wxOpen.getAesKey(), wxOpen.getAppId());
//                //加密模式：需要解密
//                returnvaleue = pc.encryptMsg(replyMsg,  createTime.toString(), RandomUtil.getRandomStr(7));
//            } catch (AesException e) {
//                e.printStackTrace();
//            }
//            logger.info("WxTestComplateMsgTypeTextEventHandler------->exec back returnvaleue："+returnvaleue);
            return replyMsg;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return WechatConstants.FAILED;
        }
    }

    /**
     * 第三方平台账号信息
     * @return
     */
    WxOpen getWxOpen(){
        String json= RedisUtil.get(WechatConstants.WX_OPEN_INFO);
        if(StringUtils.isNotEmpty(json)){
            return JSON.parseObject(json,WxOpen.class);
        }
        WxOpen wxOpen=feignShopConfig.getWxOpen();
        RedisUtil.set(WechatConstants.WX_OPEN_INFO, JSON.toJSONString(wxOpen), WechatConstants.WXOPEN_EXPIRE_TIME);
        return wxOpen;
    }
}
