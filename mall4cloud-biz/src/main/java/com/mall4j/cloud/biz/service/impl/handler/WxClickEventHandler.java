package com.mall4j.cloud.biz.service.impl.handler;

import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.biz.wx.wx.api.WechatOpenApi;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.model.WeixinMenu;
import com.mall4j.cloud.biz.model.WeixinWebApp;
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
 * 菜单点击
 */
@Service("wx_click_event_handler")
public class WxClickEventHandler implements WechatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxClickEventHandler.class);

    @Autowired
    private WeixinWebAppService weixinWebAppService;
    @Autowired
    private WeixinMenuService weixinMenuService;
    @Autowired
    private WeixinAutoResponseService autoResponseService;
    @Autowired
    private WeiXinTemplateManagerService templateManagerService;
    @Autowired
    private WeixinActoinLogsService actoinLogsService;
    @Autowired
    private WxConfig wxConfig;
    @Override
    public String exec(WxCpMsgDTO msgDTO) throws WxErrorException {
        String xml=msgDTO.getXml();
        logger.info("WxClickEventHandler------->exec："+xml);
        WxMpXmlMessage wxMessage=WechatUtils.parseXmlMessage(xml);
        try {

            //获取授权公众号信息
            WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(wxMessage.getToUser());
            if(weixinWebApp==null){
                logger.info("WxClickEventHandler 公众号未授权 --->"+wxMessage.getToUser());
                return WechatConstants.TICKET_SUCCESS;
            }

            //保存消息推送日志
            actoinLogsService.saveWeixinActoinLogs(wxMessage,weixinWebApp.getAppId());

            WxMp wxMp = new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
            String accessToken = wxConfig.getWxMpService(wxMp).getAccessToken();
            String fromUser = wxMessage.getFromUser();

            //根据菜单menuId 匹配点击内容
            WeixinMenu weixinMenu=weixinMenuService.getById(wxMessage.getEventKey());
            if(weixinMenu!=null){
                List<WeixinAutoResponse> weixinAutoResponses=autoResponseService.getWeixinAutos(weixinMenu.getId(), WxAutoDataSrcType.MENU.value());
                //发送客服消息
                if(weixinAutoResponses!=null && weixinAutoResponses.size()>0){
                    weixinAutoResponses.stream().peek(itemResponse->{
                        WeixinTemplageManagerVO managerVO=templateManagerService.getTemplate(itemResponse.getTemplateId(),itemResponse.getMsgType());
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
                            if(managerVO.getLinksucai()!=null){
                                WechatOpenApi.sendTextMsg(managerVO.getLinksucai().getOuterLink(),fromUser,accessToken);
                            }
                            if(managerVO.getNewsTemplate()!=null){

                            }
                        }
                    }).collect(Collectors.toList());
                }
            }

            return WechatConstants.TICKET_SUCCESS;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return WechatConstants.FAILED;
        }
    }
}
