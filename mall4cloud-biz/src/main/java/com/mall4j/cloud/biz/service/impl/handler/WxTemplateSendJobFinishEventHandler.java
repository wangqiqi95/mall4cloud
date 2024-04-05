package com.mall4j.cloud.biz.service.impl.handler;

import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.model.WeixinTmessageSendlog;
import com.mall4j.cloud.biz.service.WechatEventHandler;
import com.mall4j.cloud.biz.service.WeixinTmessageSendlogService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 消息模板发送反馈
 */
@Service("wx_templatesendjobfinish_event_handler")
@Slf4j
public class WxTemplateSendJobFinishEventHandler implements WechatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxSubscribeEventHandler.class);

    @Autowired
    private WeixinTmessageSendlogService tmessageSendlogService;
    @Override
    public String exec(WxCpMsgDTO msgDTO) throws WxErrorException {
        String xml=msgDTO.getXml();
        logger.info("WxTemplateSendJobFinishEventHandler------->exec："+xml);
        WxMpXmlMessage wxMessage=WechatUtils.parseXmlMessage(xml);
        try {
            WeixinTmessageSendlog tmessageSendlog=new WeixinTmessageSendlog();
            tmessageSendlog.setMsgId(String.valueOf(wxMessage.getMsgId()));
            tmessageSendlog.setWxStatus(wxMessage.getStatus());
            tmessageSendlog.setUpdateTime(new Date());
            tmessageSendlogService.updateWxStatus(tmessageSendlog);
            return WechatConstants.TICKET_SUCCESS;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return WechatConstants.FAILED;
        }
    }
}
