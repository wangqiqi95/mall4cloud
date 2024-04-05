package com.mall4j.cloud.biz.service.impl.handler;

import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.biz.wx.wx.api.WechatOpenApi;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.*;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.biz.vo.WeixinMaTemplateVO;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.bean.WxMp;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认回复消息
 */
@Service("wx_default_event_handler")
@Slf4j
public class WxDefaultEventHandler implements WechatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxDefaultEventHandler.class);

    @Autowired
    private WeixinWebAppService weixinWebAppService;
    @Autowired
    private WeixinAutoMsgService weixinAutoMsgService;
    @Autowired
    private WeixinAutoResponseService autoResponseService;
    @Autowired
    private WeiXinTemplateManagerService templateManagerService;
    @Autowired
    private WeixinConfigService weixinConfigService;
    @Autowired
    private WeixinActoinLogsService actoinLogsService;
    @Autowired
    private WxConfig wxConfig;

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws WxErrorException {
        String xml=msgDTO.getXml();
        logger.info("WxDefaultEventHandler------->exec："+xml);
        WxMpXmlMessage wxMessage=WechatUtils.parseXmlMessage(xml);
        try {
            //获取授权公众号信息
            WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(wxMessage.getToUser());
            if(weixinWebApp==null){
                logger.info("WxDefaultEventHandler 公众号未授权 --->"+wxMessage.getToUser());
                return WechatConstants.TICKET_SUCCESS;
            }
            //保存消息推送日志
            actoinLogsService.saveWeixinActoinLogs(wxMessage,weixinWebApp.getAppId());

            WxMp wxMp=new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
            String accessToken=wxConfig.getWxMpService(wxMp).getAccessToken();
            String fromUser=wxMessage.getFromUser();
            //获取默认关注回复开关
            WeixinConfig weixinConfig=weixinConfigService.getWeixinConfigByKey(weixinWebApp.getWeixinAppId(), WechatConstants.WxConfigConstants.MP_AUTO_MSG_OPEN);
            Integer openState=weixinConfig!=null?Integer.parseInt(weixinConfig.getParamValue()):2;
            //获取回复内容
            WeixinAutoMsg weixinAutoMsg=weixinAutoMsgService.getWeixinAutoMsg(weixinWebApp.getWeixinAppId());
            if(weixinAutoMsg!=null && openState==1){
                List<WeixinAutoResponse> weixinAutoResponses=autoResponseService.getWeixinAutos(weixinAutoMsg.getId(), WxAutoDataSrcType.AUTO_MSG.value());
                //发送客服消息
                if(weixinAutoResponses!=null && weixinAutoResponses.size()>0){
                    weixinAutoResponses.stream().peek(item->{
                        WeixinTemplageManagerVO managerVO=templateManagerService.getTemplate(item.getTemplateId(),item.getMsgType());
                        if(managerVO!=null){
                            if(managerVO.getTextTemplate()!=null){
                                //需要处理超文本内容【超链接】
                                String templateContent= SlognUtils.getTextHerf(managerVO.getTextTemplate().getTemplateContent(),managerVO.getTextTemplate().getTextHerfs());
                                WechatOpenApi.sendTextMsg(templateContent,fromUser,accessToken);
                            }
                            if(managerVO.getImgTemplate()!=null){
                                WechatOpenApi.sendImgMsg(managerVO.getImgTemplate().getMediaId(),wxMessage.getFromUser(),accessToken);
                            }
                            if(managerVO.getMaTemplate()!=null){
                                WeixinMaTemplateVO weixinMaTemplateVO=managerVO.getMaTemplate();
                                WechatOpenApi.sendMiniprogrampageMsg(weixinMaTemplateVO.getCardTitle(),
                                        weixinMaTemplateVO.getMaAppId(),
                                        weixinMaTemplateVO.getMaAppPath(),
                                        weixinMaTemplateVO.getThumbMediaId(),
                                        fromUser,accessToken);
                            }
                            if(managerVO.getNewsTemplate()!=null){

                            }
                        }
                    }).collect(Collectors.toList());
                }
            }

            return WechatConstants.TICKET_SUCCESS;

//            String content = "";
//            Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
//            StringBuffer sb = new StringBuffer();
//            sb.append("<xml>");
//            sb.append("<ToUserName><![CDATA["+wxMessage.getFromUser()+"]]></ToUserName>");
//            sb.append("<FromUserName><![CDATA["+wxMessage.getToUser()+"]]></FromUserName>");
//            sb.append("<CreateTime>"+createTime+"</CreateTime>");
//            sb.append("<MsgType><![CDATA[text]]></MsgType>");
//            sb.append("<Content><![CDATA["+content+"]]></Content>");
//            sb.append("</xml>");
//            String replyMsg = sb.toString();
//
//            logger.info("WxDefaultEventHandler------->exec back："+replyMsg);
//            return replyMsg;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return WechatConstants.FAILED;
        }
    }
}
