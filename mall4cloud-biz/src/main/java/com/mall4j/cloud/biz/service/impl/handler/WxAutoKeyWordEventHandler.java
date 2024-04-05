package com.mall4j.cloud.biz.service.impl.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.mall4j.cloud.api.biz.vo.TextHerf;
import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.biz.wx.wx.api.WechatOpenApi;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.WeixinAutoMsg;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.model.WeixinConfig;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.biz.vo.WeixinAutoKeywordVO;
import com.mall4j.cloud.biz.vo.WeixinMaTemplateVO;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.util.PlaceholderResolver;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 关键词回复
 */
@Service("wx_auto_key_word_handler")
public class WxAutoKeyWordEventHandler implements WechatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxAutoKeyWordEventHandler.class);

    @Autowired
    private WeixinWebAppService weixinWebAppService;
    @Autowired
    private WeixinAutoKeywordService weixinAutoKeywordService;
    @Autowired
    private WeixinAutoResponseService autoResponseService;
    @Autowired
    private WeiXinTemplateManagerService templateManagerService;
    @Autowired
    private WeixinActoinLogsService actoinLogsService;
    @Autowired
    private WeixinConfigService weixinConfigService;
    @Autowired
    private WeixinAutoMsgService weixinAutoMsgService;
    @Autowired
    private WxConfig wxConfig;

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws WxErrorException {
        String xml=msgDTO.getXml();
        logger.info("WxAutoKeyWordEventHandler------->exec："+xml);
        WxMpXmlMessage wxMessage=WechatUtils.parseXmlMessage(xml);
        try {

            //获取授权公众号信息
            WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(wxMessage.getToUser());
            if(weixinWebApp==null){
                logger.info("WxScanEventHandler 公众号未授权 --->"+wxMessage.getToUser());
                return WechatConstants.TICKET_SUCCESS;
            }

            //保存消息推送日志
            actoinLogsService.saveWeixinActoinLogs(wxMessage,weixinWebApp.getAppId());

            WxMp wxMp = new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
            String accessToken = wxConfig.getWxMpService(wxMp).getAccessToken();
            String fromUser = wxMessage.getFromUser();
            //获取对应关键词
            WeixinAutoKeywordVO weixinAutoKeywordVO=weixinAutoKeywordService.getByKeyword(weixinWebApp.getWeixinAppId(),wxMessage.getContent());
            if(weixinAutoKeywordVO!=null){//关键词回复
                //获取关键词内容
                List<WeixinAutoResponse> weixinAutoResponses=autoResponseService.getWeixinAutos(weixinAutoKeywordVO.getId(), WxAutoDataSrcType.KEY_WORD.value());

                sendMsg(weixinAutoResponses,wxMessage.getFromUser(),accessToken);
            }else{//无关键字默认回复
                WeixinConfig weixinConfig=weixinConfigService.getWeixinConfigByKey(weixinWebApp.getWeixinAppId(), WechatConstants.WxConfigConstants.MP_AUTO_MSG_OPEN);
                if(weixinConfig!=null && Integer.parseInt(weixinConfig.getParamValue())==1){
                    WeixinAutoMsg autoMsg=weixinAutoMsgService.getWeixinAutoMsg(weixinWebApp.getWeixinAppId());
                    if(autoMsg!=null){
                        List<WeixinAutoResponse> weixinAutoResponses=autoResponseService.getWeixinAutos(autoMsg.getId(), WxAutoDataSrcType.AUTO_MSG.value());

                        sendMsg(weixinAutoResponses,wxMessage.getFromUser(),accessToken);
                    }
                }
            }
            return WechatConstants.TICKET_SUCCESS;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return WechatConstants.FAILED;
        }
    }

    private void sendMsg(List<WeixinAutoResponse> weixinAutoResponses,String fromUser,String accessToken){
        //发送客服消息
        if(weixinAutoResponses!=null && weixinAutoResponses.size()>0){
            weixinAutoResponses.stream().peek(itemResponse->{
                WeixinTemplageManagerVO managerVO=templateManagerService.getTemplate(itemResponse.getTemplateId(),itemResponse.getMsgType());
                if(managerVO!=null){
                    if(managerVO.getTextTemplate()!=null){
                        //需要处理超文本内容【超链接】
                        String templateContent=SlognUtils.getTextHerf(managerVO.getTextTemplate().getTemplateContent(),managerVO.getTextTemplate().getTextHerfs());
                        WechatOpenApi.sendTextMsg(templateContent,fromUser,accessToken);
                    }
                    if(managerVO.getImgTemplate()!=null){
                        WechatOpenApi.sendImgMsg(managerVO.getImgTemplate().getMediaId(),fromUser,accessToken);
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

}
