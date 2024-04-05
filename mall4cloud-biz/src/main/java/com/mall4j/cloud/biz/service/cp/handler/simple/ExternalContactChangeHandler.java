package com.mall4j.cloud.biz.service.cp.handler.simple;

import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.service.cp.WxCpCustNotifyService;
import com.mall4j.cloud.biz.service.cp.WxCpUserNotifyService;
import com.mall4j.cloud.biz.service.cp.handler.WxCpExtConsts;
import com.mall4j.cloud.common.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.constant.WxCpConsts;
import me.chanjar.weixin.cp.message.WxCpMessageHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

;

/**
 * 外部联系人变更事件
 * @author hwy
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalContactChangeHandler implements WxCpMessageHandler {
  private final WxCpCustNotifyService wxCpCustNotifyService;
  private final WxCpUserNotifyService wxCpUserService;
  private final WeixinCpExternalManager weixinCpExternalManager;

  @Override
  public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService, WxSessionManager sessionManager) {
    String content = "外部联系人变更事件，内容：" + Json.toJsonString(wxMessage);
    log.info("--"+content);
    switch (wxMessage.getChangeType()){
      case WxCpConsts.ExternalContactChangeType.ADD_EXTERNAL_CONTACT:
      case  WxCpConsts.ExternalContactChangeType.ADD_HALF_EXTERNAL_CONTACT:
//        WxCpExternalContactInfo wxCpExternalContactInfo=getWxCpExternalContactInfo(wxMessage);
        WxCpExternalContactInfo wxCpExternalContactInfo=weixinCpExternalManager.getWxCpExternalContactInfo(wxMessage.getUserId(),wxMessage.getExternalUserId());
        wxCpCustNotifyService.create(wxMessage.getUserId(),wxMessage.getExternalUserId(),wxCpExternalContactInfo);
        wxCpCustNotifyService.sendWelcomeMsg(wxMessage.getWelcomeCode(),wxMessage.getState(),wxMessage.getUserId(),
                Objects.nonNull(wxCpExternalContactInfo)?wxCpExternalContactInfo.getExternalContact().getName():"");
        wxCpCustNotifyService.addTagToCust(wxMessage.getState(),wxMessage.getUserId(),wxMessage.getExternalUserId());
        break;
      case WxCpConsts.ExternalContactChangeType.DEL_EXTERNAL_CONTACT:
      case WxCpConsts.ExternalContactChangeType.DEL_FOLLOW_USER:
        wxCpCustNotifyService.delete(wxMessage.getUserId(),wxMessage.getExternalUserId());
        //删除消息推送
        break;
      case WxCpExtConsts.StaffExtend.TRANSFER_FAIL:
          wxCpUserService.tranSferFail(wxMessage.getUserId(),wxMessage.getExternalUserId(),wxMessage.getFailReason());
          break;
      default: log.info(wxMessage.getChangeType()+"未处理");
    }
    return null;
  }


}
