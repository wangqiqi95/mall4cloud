package com.mall4j.cloud.biz.service.cp.handler.plus;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.biz.constant.CrmPushMsgTypeEnum;
import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.service.cp.handler.WxCpExtConsts;
import com.mall4j.cloud.biz.util.TestWxMsgPushUtil;
import com.mall4j.cloud.biz.wx.cp.constant.ChatUpdateDetailEunm;
import com.mall4j.cloud.biz.service.cp.WxCpChatNotifyPlusService;
import com.mall4j.cloud.common.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.external.WxCpUserExternalGroupChatInfo;
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
public class ChatChangePlusHandler implements WxCpMessageHandler {
  private final WxCpChatNotifyPlusService wxCpChatNotifyService;
//  private final CrmManager crmManager;
  private final WeixinCpExternalManager weixinCpExternalManager;
  private  final static   String  UPDATE_DETAIL ="UpdateDetail";
  private  final TestWxMsgPushUtil testWxMsgPushUtil;
  @Override
  public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService, WxSessionManager sessionManager) {
    log.info("群变更事件处理器，内容：" + Json.toJsonString(wxMessage));
//    if(!StrUtil.equals(wxMessage.getAgentId(),String.valueOf(WxCpConfiguration.getAgentId()))){
//      log.info("群变更事件处理器失败，消息推送自建应用id【{}】与当前不匹配【{}】",wxMessage.getAgentId(),WxCpConfiguration.getAgentId());
//      return null;
//    }
    PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
    dto.setWxMessage(wxMessage);
    dto.setChangetype(wxMessage.getChangeType());
    dto.setMsgType(CrmPushMsgTypeEnum.WX_GROUP.value());

    switch (wxMessage.getChangeType()) {
      case WxCpExtConsts.ExternalChatChangeType.ADD_CHAT:
        wxCpChatNotifyService.create(wxMessage.getChatId());
        break;
      case WxCpExtConsts.ExternalChatChangeType.UPDATE_CHAT:
        Map<String, Object> allField = wxMessage.getAllFieldsMap();
        String updateDetail = (String) allField.get(UPDATE_DETAIL);
        if (ChatUpdateDetailEunm.ADD.getCode().equals(updateDetail)) {
          //新增客户
          wxCpChatNotifyService.addCust(wxMessage.getChatId());
        } else if (ChatUpdateDetailEunm.DEL.getCode().equals(updateDetail)) {
          //删除客户
          wxCpChatNotifyService.deleteCust(wxMessage,wxMessage.getChatId(), wxMessage.getQuitScene());
        } else if (ChatUpdateDetailEunm.CHANGE_NAME.getCode().equals(updateDetail)) {
          //变更群名
          wxCpChatNotifyService.changeName(wxMessage.getChatId());
        } else if (ChatUpdateDetailEunm.CHANGE_OWNER.getCode().equals(updateDetail)) {
          //变更群主
          wxCpChatNotifyService.changeOwner(wxMessage.getChatId());
        }else if(ChatUpdateDetailEunm.CHANGE_NOTICE.getCode().equals(updateDetail)){
          //群公告
          wxCpChatNotifyService.changeChaneNotice(wxMessage.getChatId());
        }else{
          //变更通知

        }
        break;
      case WxCpExtConsts.ExternalChatChangeType.DEL_CHAT://解散
        wxCpChatNotifyService.delete(wxMessage.getChatId());
        break;
      default: log.info(wxMessage.getChangeType()+"未处理");
    }
    //TODO 推送生成环境
    testWxMsgPushUtil.ChatChangeHandler(JSON.toJSONString(wxMessage));
    return null;
  }

  /**
   * 获取企微群详情
   * @param groupId
   * @return
   */
  private WxCpUserExternalGroupChatInfo.GroupChat getChatDetail(String groupId){
    return weixinCpExternalManager.getChatDetail(groupId);
  }

}
