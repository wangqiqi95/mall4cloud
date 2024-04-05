package com.mall4j.cloud.biz.service.impl.handler;

import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.WechatEventHandler;
import com.mall4j.cloud.biz.service.WeixinActoinLogsService;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import com.mall4j.cloud.common.bean.WxMp;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 取消关注公众号
 */
@Service("wx_unsubscribe_event_handler")
public class WxUnSubscribeEventHandler implements WechatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxUnSubscribeEventHandler.class);

    @Autowired
    private WeixinWebAppService weixinWebAppService;
    @Autowired
    private WeixinActoinLogsService actoinLogsService;
    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws WxErrorException {
        String xml=msgDTO.getXml();
        logger.info("WxUnSubscribeEventHandler------->exec："+xml);
        WxMpXmlMessage wxMessage=WechatUtils.parseXmlMessage(xml);
        try {

            //获取授权公众号信息
            WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(wxMessage.getToUser());
            if(weixinWebApp==null){
                logger.info("WxSubscribeEventHandler 公众号未授权 --->"+wxMessage.getToUser());
                return WechatConstants.TICKET_SUCCESS;
            }

            //保存消息推送日志
            actoinLogsService.saveWeixinActoinLogs(wxMessage,weixinWebApp.getAppId());

            WxMp wxMp=new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());

            WxMpUser wxMpUser=wxConfig.getWxMpService(wxMp).getUserService().userInfo(wxMessage.getFromUser());
            logger.info("WxMpUser----->"+wxMpUser.toString());

            //修改会员关注公众号状态
            UserWeixinccountFollowDTO followDTO=new UserWeixinccountFollowDTO();
            followDTO.setAppId(weixinWebApp.getWeixinAppId());
            followDTO.setType(weixinWebApp.getCrmType());
            followDTO.setOpenId(wxMpUser.getOpenId());
            followDTO.setStatus(2);
            followDTO.setUnionId(wxMpUser.getUnionId());
            followDTO.setUpdateTime(new Date());
            followDTO.setUnFollowTime(WechatUtils.formatDate(wxMessage.getCreateTime().toString()));
            userFeignClient.followWeixinAccount(followDTO);

//            String accessToken=wxConfig.getWxMpService(wxMp).getAccessToken(true);
//            WxOAuth2AccessToken auth2AccessToken=new WxOAuth2AccessToken();
//            auth2AccessToken.setAccessToken(accessToken);
//            auth2AccessToken.setOpenId(wxMessage.getFromUser());
//            WxOAuth2UserInfo wxOAuth2UserInfo=wxConfig.getWxMpService(wxMp).getOAuth2Service().getUserInfo(auth2AccessToken,"zh_CN");
//            logger.info("wxOAuth2UserInfo----->"+wxOAuth2UserInfo.toString());

//            String url = WechatConstants.GET_MP_USER_INFO.replace("ACCESS_TOKEN", wxConfig.getWxMpService(wxMp).getAccessToken()).replace("OPENID", wxMessage.getFromUser());
//            logger.info("GET_MP_USER_INFO request url={}", new Object[]{url});
//            JSONObject jsonObj = WechatUtils.doGetstr(url);
//            logger.info("GET_MP_USER_INFO response jsonStr={}", new Object[]{jsonObj});

            return WechatConstants.TICKET_SUCCESS;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return WechatConstants.FAILED;
        }
    }
}
