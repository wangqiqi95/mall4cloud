package com.mall4j.cloud.biz.service.impl.handler;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.wx.api.WechatOpenApi;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.model.WeixinOpenAccount;
import com.mall4j.cloud.biz.service.WechatEventHandler;
import com.mall4j.cloud.biz.service.WeixinOpenAccountService;
import com.mall4j.cloud.common.bean.WxOpen;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 预授权码
 */
@Service("wx_QUERY_AUTH_CODE_event_handler")
public class WxQueryAuthCodeEventHandler implements WechatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxQueryAuthCodeEventHandler.class);

    @Autowired
    private FeignShopConfig feignShopConfig;

    @Autowired
    private WeixinOpenAccountService weixinOpenAccountService;

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws WxErrorException {
        String xml=msgDTO.getXml();
        logger.info("WxQueryAuthCodeEventHandler------->exec："+xml);
        WxMpXmlMessage wxMessage=WechatUtils.parseXmlMessage(xml);
        try {
            WxOpen wxOpen=feignShopConfig.getWxOpen();
            WeixinOpenAccount openAccount=weixinOpenAccountService.queryOneByAppid(wxOpen.getAppId());
            String componentAccessToken=openAccount.getComponentAccessToken();
            String authCode=wxMessage.getContent().split(":")[1];
            logger.info("------step.2----使用客服消息接口回复粉丝------- component_access_token = "+componentAccessToken + "---------authorization_code = "+authCode);
            JSONObject authorizationInfoJson = WechatOpenApi.getApiQueryAuthInfo(wxOpen.getAppId(), authCode, componentAccessToken);
            logger.info("------step.3----使用客服消息接口回复粉丝-------------- 获取authorizationInfoJson = "+authorizationInfoJson);
            JSONObject infoJson = authorizationInfoJson.getJSONObject("authorization_info");
            String authorizer_access_token = infoJson.getString("authorizer_access_token");


            JSONObject obj = new JSONObject();
            Map<String,Object> msgMap = new HashMap<String,Object>();
            String msg = authCode + "_from_api";
            msgMap.put("content", msg);

            obj.put("touser", wxMessage.getFromUser());
            obj.put("msgtype", "text");
            obj.put("text", msgMap);
            WechatOpenApi.sendMessage(obj, authorizer_access_token);

            return WechatConstants.TICKET_SUCCESS;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return WechatConstants.FAILED;
        }
    }
}
