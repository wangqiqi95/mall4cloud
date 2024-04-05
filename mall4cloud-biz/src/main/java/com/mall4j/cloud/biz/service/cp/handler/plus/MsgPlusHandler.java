package com.mall4j.cloud.biz.service.cp.handler.plus;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.biz.service.cp.WxCpUserNotifyPlusService;
import com.mall4j.cloud.biz.util.TestWxMsgPushUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.constant.WxCpConsts;
import me.chanjar.weixin.cp.message.WxCpMessageHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *https://developer.work.weixin.qq.com/document/path/90240
 * @author hwy
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MsgPlusHandler implements WxCpMessageHandler {

    private final WxCpUserNotifyPlusService wxCpUserService;
//    private final CrmManager crmManager;
    private  final WxCpService wxCpService;
    private  final TestWxMsgPushUtil testWxMsgPushUtil;

    @Override
    public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService cpService, WxSessionManager sessionManager) {
        log.info("-MsgHandler context-"+ JSON.toJSONString(context));
        log.info("-MsgHandler wxMessage-"+ JSON.toJSONString(wxMessage));
//        if(!StrUtil.equals(wxMessage.getAgentId(),String.valueOf(WxCpConfiguration.getAgentId()))){
//            log.info("MsgHandler事件处理器失败，消息推送自建应用id【{}】与当前不匹配【{}】",wxMessage.getAgentId(),WxCpConfiguration.getAgentId());
//            return null;
//        }
        switch (wxMessage.getEvent()){
            case WxCpConsts.EventType.SUBSCRIBE://邀请成员加入企业，成员加入
                String userId=wxMessage.getFromUserName();
                WxCpUser wxCpUser=getWxCpUser(userId);
                log.info("-MsgHandler 邀请成员加入企业，成员加入: {}-"+ JSON.toJSONString(wxCpUser));
                wxCpUserService.update(userId,
                        wxCpUser.getNewUserId(),
                        wxCpUser.getMobile(),
                        String.valueOf(wxCpUser.getStatus()),
                        String.valueOf(System.currentTimeMillis())
                        ,wxCpUser.getAvatar(),wxCpUser.getDepartIds());
            break;
            case WxCpConsts.EventType.UNSUBSCRIBE://成员取消关注事件【成员退出企业】
                 userId=wxMessage.getFromUserName();
                log.info("-MsgHandler 成员取消关注事件【成员退出企业】: {}-"+ userId);
                wxCpUserService.delete(userId,wxMessage.getCreateTime().toString());
                break;
        }
        //TODO 推送生成环境
        testWxMsgPushUtil.MsgHandler(JSON.toJSONString(wxMessage));
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
            log.info("----getWxCpUser back -------：【"+JSON.toJSONString(wxCpUser)+"】");
            return wxCpUser;
        }catch (WxErrorException e){
            log.error("",e);
        }
        return null;
    }

}
