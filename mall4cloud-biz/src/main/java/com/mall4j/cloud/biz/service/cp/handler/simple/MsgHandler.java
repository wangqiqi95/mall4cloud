package com.mall4j.cloud.biz.service.cp.handler.simple;

import com.mall4j.cloud.common.util.Json;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.message.WxCpMessageHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * @author hwy
 */
@Slf4j
@Component
public class MsgHandler implements WxCpMessageHandler {

    @Override
    public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService, WxSessionManager sessionManager) {
        final String msgType = wxMessage.getMsgType();
        log.info("-MsgHandler context-"+ Json.toJsonString(context));
        log.info("-MsgHandler wxMessage-"+ Json.toJsonString(wxMessage));
        if (msgType == null) {
            // 如果msgType没有，就自己根据具体报文内容做处理
        }
        return null;

    }

}
