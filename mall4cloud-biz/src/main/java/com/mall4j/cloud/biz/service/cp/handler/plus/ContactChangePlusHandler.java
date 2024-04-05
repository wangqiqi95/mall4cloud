package com.mall4j.cloud.biz.service.cp.handler.plus;

 import com.alibaba.fastjson.JSON;
 import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
 import com.mall4j.cloud.biz.constant.CrmPushMsgTypeEnum;
 import com.mall4j.cloud.api.biz.dto.cp.crm.PushCDPCpMsgEventDTO;
// import com.mall4j.cloud.biz.manager.CrmManager;
 import com.mall4j.cloud.biz.service.cp.WxCpUserNotifyPlusService;
 import com.mall4j.cloud.biz.util.TestWxMsgPushUtil;
 import com.mall4j.cloud.common.util.Json;
 import lombok.RequiredArgsConstructor;
 import lombok.extern.slf4j.Slf4j;
 import me.chanjar.weixin.common.error.WxErrorException;
 import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
 import me.chanjar.weixin.cp.bean.WxCpDepart;
 import me.chanjar.weixin.cp.bean.WxCpUser;
 import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
 import me.chanjar.weixin.cp.constant.WxCpConsts;
 import me.chanjar.weixin.cp.message.WxCpMessageHandler;
 import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 通讯录变更事件处理器.
 * @author hwy
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContactChangePlusHandler implements WxCpMessageHandler {
  private final WxCpUserNotifyPlusService wxCpUserService;
//  private final CrmManager crmManager;
    private  final WxCpService wxCpService;
    private  final TestWxMsgPushUtil testWxMsgPushUtil;
      @Override
      public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService, WxSessionManager sessionManager) {
        String content = "收到通讯录变更事件，内容：" + Json.toJsonString(wxMessage);
        log.info(content);
          PushCDPCpMsgEventDTO dto=new PushCDPCpMsgEventDTO();
          dto.setChangetype(wxMessage.getChangeType());
          dto.setWxMessage(wxMessage);
          WxCpUser wxCpUser=null;
        switch (wxMessage.getChangeType()){
          case WxCpConsts.ContactChangeType.CREATE_USER://新增员工
              //eury 2023-11-08 15:48注释，需要确认是否处理该逻辑
               wxCpUser=getWxCpUser(wxMessage.getUserId());
            wxCpUserService.create(wxMessage.getUserId(),
                    wxMessage.getMobile(),
                    wxMessage.getStatus(),
                    wxMessage.getCreateTime().toString()
                    ,wxCpUser.getAvatar(),
                    wxCpUser.getDepartIds());
              dto.setMsgType(CrmPushMsgTypeEnum.WX_USER.value());
              dto.setWxCpUser(wxCpUser);
//              crmManager.pushCDPCpMsgDate(dto);
            break;
          case  WxCpConsts.ContactChangeType.UPDATE_USER://修改员工
              wxCpUser=getWxCpUser(wxMessage.getUserId());
            wxCpUserService.update(wxMessage.getUserId(),
                    wxMessage.getNewUserId(),
                    wxMessage.getMobile(),
                    wxMessage.getStatus(),
                    wxMessage.getCreateTime().toString()
                    ,wxCpUser.getAvatar(),
                    wxMessage.getDepartments());
              //TODO 通知数云
              dto.setMsgType(CrmPushMsgTypeEnum.WX_USER.value());
              dto.setWxCpUser(wxCpUser);
//              crmManager.pushCDPCpMsgDate(dto);
            break;
          case WxCpConsts.ContactChangeType.DELETE_USER://删除员工
            wxCpUserService.delete(wxMessage.getUserId(),wxMessage.getCreateTime().toString());
              //TODO 通知数云
              dto.setMsgType(CrmPushMsgTypeEnum.WX_USER.value());
              wxCpUser=new WxCpUser();
              wxCpUser.setUserId(wxMessage.getUserId());
              dto.setWxCpUser(wxCpUser);
//              crmManager.pushCDPCpMsgDate(dto);
            break;
            case WxCpConsts.SchoolContactChangeType.CREATE_DEPARTMENT://新增部门
                wxCpUserService.addDepart(wxMessage,getWxCpDepart(wxMessage.getId()));
                //TODO 通知数云
                dto.setMsgType(CrmPushMsgTypeEnum.WX_DEPART.value());
                dto.setDepart(getWxCpDepart(wxMessage.getId()));
//                crmManager.pushCDPCpMsgDate(dto);
                break;
            case  WxCpConsts.SchoolContactChangeType.UPDATE_DEPARTMENT://更新部门
                wxCpUserService.updateDepart(wxMessage,getWxCpDepart(wxMessage.getId()));
                //TODO 通知数云
                dto.setMsgType(CrmPushMsgTypeEnum.WX_DEPART.value());
                dto.setDepart(getWxCpDepart(wxMessage.getId()));
//                crmManager.pushCDPCpMsgDate(dto);
                break;
            case WxCpConsts.SchoolContactChangeType.DELETE_DEPARTMENT://删除部门
                wxCpUserService.deleteDepart(wxMessage,getWxCpDepart(wxMessage.getId()));
                //TODO 通知数云
                dto.setMsgType(CrmPushMsgTypeEnum.WX_DEPART.value());
                dto.setDepart(getWxCpDepart(wxMessage.getId()));
//                crmManager.pushCDPCpMsgDate(dto);
                break;
          default: log.info(wxMessage.getChangeType()+"未处理");
        }

          //TODO 推送生成环境
          testWxMsgPushUtil.ContactChangeHandler(JSON.toJSONString(wxMessage));

        return null;
      }

    /**
     * 获取部门详情
     * @param id
     * @return
     */
    private WxCpDepart getWxCpDepart(String id){
        try {
            log.info("----getWxCpDepart start -------id：{}",id);
            WxCpDepart depart = WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.CP_CONNECT_AGENT_ID).getDepartmentService().get(Long.parseLong(id));
            log.info("----getWxCpDepart back -------：【"+Json.toJsonString(depart)+"】");
            return depart;
        }catch (WxErrorException e){
            log.error("",e);
        }
        return null;
    }

    /**
     * 获取员工详情
     * @param userId
     * @return
     */
    private WxCpUser getWxCpUser(String userId){
        try {
            log.info("----getWxCpUser start -------id：{}",userId);
//            WxCpUser wxCpUser = WxCpConfiguration.getWxCpService(WxCpConfiguration.CP_CONNECT_AGENT_ID).getUserService().getById(userId);
            WxCpUser wxCpUser=wxCpService.getUserService().getById(userId);
            log.info("----getWxCpUser back -------：【"+Json.toJsonString(wxCpUser)+"】");
            return wxCpUser;
        }catch (WxErrorException e){
            log.error("",e);
        }
        return null;
    }

}
