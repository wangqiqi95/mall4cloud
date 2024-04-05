package com.mall4j.cloud.biz.service.cp.handler.simple;

import com.mall4j.cloud.biz.service.cp.WxCpChatNotifyPlusService;
import com.mall4j.cloud.biz.service.cp.WxCpChatNotifyService;
import com.mall4j.cloud.biz.service.cp.handler.WxCpExtConsts;
import com.mall4j.cloud.biz.wx.cp.constant.ChatUpdateDetailEunm;
import com.mall4j.cloud.common.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.message.WxCpMessageHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

;

/**
 * 群变更事件处理器.
 * @author hwy
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatChangeHandler implements WxCpMessageHandler {
  private final WxCpChatNotifyService wxCpChatNotifyService;
  private  final static   String  UPDATE_DETAIL ="UpdateDetail";
  @Override
  public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService, WxSessionManager sessionManager) {
    log.info("群变更事件处理器，内容：" + Json.toJsonString(wxMessage));
    switch (wxMessage.getChangeType()){
      case WxCpExtConsts.ExternalChatChangeType.ADD_CHAT:
        wxCpChatNotifyService.create(wxMessage.getChatId());
        break;
      case WxCpExtConsts.ExternalChatChangeType.UPDATE_CHAT:
        Map<String,Object> allField = wxMessage.getAllFieldsMap();
        String updateDetail = (String)allField.get(UPDATE_DETAIL);
        //新增客户
        if(ChatUpdateDetailEunm.ADD.getCode().equals(updateDetail)){
          wxCpChatNotifyService.addCust(wxMessage.getChatId());
          //删除客户
        }else if(ChatUpdateDetailEunm.DEL.getCode().equals(updateDetail)){
          wxCpChatNotifyService.deleteCust(wxMessage.getChatId());
          //变更群名
        }else if(ChatUpdateDetailEunm.CHANGE_NAME.getCode().equals(updateDetail)){
          wxCpChatNotifyService.changeName(wxMessage.getChatId());
          //变更群主
        }else if(ChatUpdateDetailEunm.CHANGE_OWNER.getCode().equals(updateDetail)){
          wxCpChatNotifyService.changeOwner(wxMessage.getChatId());
          //变更通知
        }else{

        }
        break;
      case WxCpExtConsts.ExternalChatChangeType.DEL_CHAT:
        wxCpChatNotifyService.delete(wxMessage.getChatId());
        break;
      default: log.info(wxMessage.getChangeType()+"未处理");
    }
    return null;
  }

}
