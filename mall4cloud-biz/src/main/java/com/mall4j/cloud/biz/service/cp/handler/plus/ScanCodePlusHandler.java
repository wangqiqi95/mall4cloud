package com.mall4j.cloud.biz.service.cp.handler.plus;

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
public class ScanCodePlusHandler implements WxCpMessageHandler {

    @Override
    public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService, WxSessionManager sessionManager) {
        final String msgType = wxMessage.getMsgType();
        log.info("-ScanCodeHandler context-"+ Json.toJsonString(context));
        log.info("-ScanCodeHandler wxMessage-"+ Json.toJsonString(wxMessage));
        if (msgType == null) {
        }
        return null;

    }

}
