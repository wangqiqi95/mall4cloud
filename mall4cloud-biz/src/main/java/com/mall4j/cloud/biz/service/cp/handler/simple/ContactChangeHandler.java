package com.mall4j.cloud.biz.service.cp.handler.simple;

import com.mall4j.cloud.biz.service.cp.WxCpUserNotifyPlusService;
import com.mall4j.cloud.biz.service.cp.WxCpUserNotifyService;
import com.mall4j.cloud.common.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.constant.WxCpConsts;
import me.chanjar.weixin.cp.message.WxCpMessageHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

;

/**
 * 通讯录变更事件处理器.
 * @author hwy
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContactChangeHandler implements WxCpMessageHandler {
  private final WxCpUserNotifyService wxCpUserService;
  @Override
  public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService, WxSessionManager sessionManager) {
    String content = "收到通讯录变更事件，内容：" + Json.toJsonString(wxMessage);
    log.info(content);
    switch (wxMessage.getChangeType()){
      case WxCpConsts.ContactChangeType.CREATE_USER:
        wxCpUserService.create(wxMessage.getUserId(),wxMessage.getMobile(),wxMessage.getStatus());
        break;
      case  WxCpConsts.ContactChangeType.UPDATE_USER:
        wxCpUserService.update(wxMessage.getUserId(),wxMessage.getNewUserId(),wxMessage.getMobile(),wxMessage.getStatus());
        break;
      case WxCpConsts.ContactChangeType.DELETE_USER:
        wxCpUserService.delete(wxMessage.getUserId());
        break;
      default: log.info(wxMessage.getChangeType()+"未处理");
    }
    return null;
  }

}
