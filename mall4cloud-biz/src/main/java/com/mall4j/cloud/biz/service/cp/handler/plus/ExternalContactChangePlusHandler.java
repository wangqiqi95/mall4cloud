package com.mall4j.cloud.biz.service.cp.handler.plus;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.biz.manager.WeixinCpExternalManager;
import com.mall4j.cloud.biz.service.cp.WxCpCustNotifyPlusService;
import com.mall4j.cloud.biz.service.cp.WxCpUserNotifyPlusService;
import com.mall4j.cloud.biz.service.cp.handler.WxCpExtConsts;
import com.mall4j.cloud.biz.util.TestWxMsgPushUtil;
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

;

/**
 * 外部联系人变更事件
 * @author hwy
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalContactChangePlusHandler implements WxCpMessageHandler {
  private final WxCpCustNotifyPlusService wxCpCustNotifyService;
  private final WxCpUserNotifyPlusService wxCpUserService;
  private final WeixinCpExternalManager weixinCpExternalManager;
  private  final TestWxMsgPushUtil testWxMsgPushUtil;

  @Override
  public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService, WxSessionManager sessionManager) {
    String content = "外部联系人变更事件，内容：" + Json.toJsonString(wxMessage);
    log.info("--"+content);
//    if(!StrUtil.equals(wxMessage.getAgentId(),String.valueOf(WxCpConfiguration.getAgentId()))){
//      log.info("外部联系人变更事件失败，消息推送自建应用id【{}】与当前不匹配【{}】",wxMessage.getAgentId(),WxCpConfiguration.getAgentId());
//      return null;
//    }
    switch (wxMessage.getChangeType()){
      case WxCpConsts.ExternalContactChangeType.ADD_EXTERNAL_CONTACT://【添加企业客户事件】
      case  WxCpConsts.ExternalContactChangeType.ADD_HALF_EXTERNAL_CONTACT://【外部联系人添加了配置了客户联系功能且开启了免验证的成员时（此时成员尚未确认添加对方为好友）】
        WxCpExternalContactInfo wxCpExternalContactInfo=weixinCpExternalManager.getWxCpExternalContactInfo(wxMessage.getUserId(),wxMessage.getExternalUserId());
        wxCpCustNotifyService.create(wxMessage,wxCpExternalContactInfo);//处理员工与客户关系数据
        break;
      case WxCpConsts.ExternalContactChangeType.EDIT_EXTERNAL_CONTACT://【编辑企业客户事件】
        wxCpExternalContactInfo=weixinCpExternalManager.getWxCpExternalContactInfo(wxMessage.getUserId(),wxMessage.getExternalUserId());
        wxCpCustNotifyService.update(wxMessage,wxCpExternalContactInfo);
        break;
      case WxCpConsts.ExternalContactChangeType.DEL_EXTERNAL_CONTACT://删除企业客户事件【成员删除外部联系人】
        wxCpCustNotifyService.delete(wxMessage,wxMessage.getExternalUserId(),3);//双方删除【企微接口获取不到外部联系人信息】
        break;
      case WxCpConsts.ExternalContactChangeType.DEL_FOLLOW_USER://删除跟进成员事件【成员被外部联系人删除】
        wxCpCustNotifyService.delete(wxMessage,wxMessage.getExternalUserId(),2);//单方面解除绑定
        //删除消息推送
        break;
      case WxCpExtConsts.StaffExtend.TRANSFER_FAIL://【企业将客户分配给新的成员接替后，客户添加失败时回调该事件】
          wxCpUserService.tranSferFail(wxMessage,wxMessage.getFailReason());
          break;
      case WxCpExtConsts.ExternalChatUpdateType.MSG_AUDIT_APPROVED://外部联系人同意开启会话存档
        //TODO 处理外部联系人同意开启会话存档
        break;
      default: log.info(wxMessage.getChangeType()+"未处理");
    }

    //TODO 通知数云
//    PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
//    dto.setWxMessage(wxMessage);
//    dto.setChangetype(wxMessage.getChangeType());
//    dto.setMsgType(CrmPushMsgTypeEnum.WX_EXTERNAL.value());
//    if(wxMessage.getChangeType().equals(WxCpConsts.ExternalContactChangeType.DEL_EXTERNAL_CONTACT)){//双方删除【企微接口获取不到外部联系人信息】
//      WxCpExternalContactInfo wxCpExternalContactInfo=new WxCpExternalContactInfo();
//      ExternalContact externalContact=new ExternalContact();
//      externalContact.setExternalUserId(wxMessage.getExternalUserId());
//      wxCpExternalContactInfo.setExternalContact(externalContact);
//      dto.setWxCpExternalContactInfo(wxCpExternalContactInfo);
//    }else{
//      dto.setWxCpExternalContactInfo(weixinCpExternalManager.getWxCpExternalContactInfo(wxMessage.getUserId(),wxMessage.getExternalUserId()));
//    }
//    crmManager.pushCDPCpMsgDate(dto);

    //TODO 推送生成环境
    testWxMsgPushUtil.ExternalContactChangeHandler(JSON.toJSONString(wxMessage));

    return null;
  }





}
