package com.mall4j.cloud.biz.service.impl.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.user.dto.UserWeixinccountFollowDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowVO;
import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.biz.wx.wx.api.WechatOpenApi;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.model.WeixinConfig;
import com.mall4j.cloud.biz.model.WeixinSubscribe;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.biz.vo.WeixinMaTemplateVO;
import com.mall4j.cloud.biz.vo.WeixinStoreSubscribeVO;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 关注公众号
 */
@Service("wx_subscribe_event_handler")
public class WxSubscribeEventHandler implements WechatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxSubscribeEventHandler.class);

    @Autowired
    private WeixinWebAppService weixinWebAppService;
    @Autowired
    private WeixinSubscribeService weixinSubscribeService;
    @Autowired
    private WeixinStoreSubscribeService weixinStoreSubscribeService;
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
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws WxErrorException {
        String xml=msgDTO.getXml();
        logger.info("WxSubscribeEventHandler------->exec：" + xml);
        WxMpXmlMessage wxMessage = WechatUtils.parseXmlMessage(xml);
        try {

            //获取授权公众号信息
            WeixinWebApp weixinWebApp = weixinWebAppService.queryByAppid(wxMessage.getToUser());
            if (weixinWebApp == null) {
                logger.info("WxSubscribeEventHandler 公众号未授权 --->" + wxMessage.getToUser());
                return WechatConstants.TICKET_SUCCESS;
            }

            //保存消息推送日志
            actoinLogsService.saveWeixinActoinLogs(wxMessage,weixinWebApp.getAppId());


            WxMp wxMp = new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
            String accessToken = wxConfig.getWxMpService(wxMp).getAccessToken();
            String fromUser = wxMessage.getFromUser();
            String appId = weixinWebApp.getWeixinAppId();

            WxMpUser wxMpUser = wxConfig.getWxMpService(wxMp).getUserService().userInfo(wxMessage.getFromUser());
            logger.info("WxMpUser----->" + wxMpUser.toString());

            //用户之前是否关注过
            // 在UserFeignClient@followWeixinAccount方法中
            // 如果之前用户关注过，则只会更新用户的关注状态，
            // 如果没有关注过该公众号，则会插入一条新数据进去
            // 取关操作时，只是把状态更新成未关注，并未删除掉关注的数据
            // 此方法必须要在 userFeignClient@followWeixinAccount 之前，否则会出现用户一直处于关注状态
            // 故采用Null值来判断，用户之前是否关注

            // 可能导致不激活关注有礼的活动的情况
            // 1.用户重复关注
            // 2.公众号未绑定开放平台
            boolean isVeryFirstTimeToFollowUp = true;
            ServerResponseEntity<UserWeixinAccountFollowVO> userWeixinAccountFollow = userFeignClient.getUserWeixinAccountFollow(wxMpUser.getUnionId(), weixinWebApp.getWeixinAppId());
            if (userWeixinAccountFollow.isSuccess()) {
                UserWeixinAccountFollowVO data = userWeixinAccountFollow.getData();
                if (data != null && data.getStatus()==1) {
                    isVeryFirstTimeToFollowUp = false;
                    logger.info("之前关注过，不再发送活动，用户UnionId:{}，WechatAppId:{}", wxMpUser.getUnionId(), weixinWebApp.getWeixinAppId());
                }
            } else {
                isVeryFirstTimeToFollowUp = false;
                logger.error("公众号({})未绑定微信公众平台，导致获取不到全局UnionId，所以不调用关注有礼", weixinWebApp.getWeixinAppId());
            }
            logger.info("--getUserWeixinAccountFollow 关注信息--{}  isVeryFirstTimeToFollowUp-{}", JSON.toJSONString(userWeixinAccountFollow),isVeryFirstTimeToFollowUp);
            //修改会员关注公众号状态
            UserWeixinccountFollowDTO followDTO = new UserWeixinccountFollowDTO();
            followDTO.setAppId(weixinWebApp.getWeixinAppId());
            followDTO.setType(weixinWebApp.getCrmType());
            followDTO.setOpenId(wxMpUser.getOpenId());
            followDTO.setStatus(1);
            followDTO.setUnionId(wxMpUser.getUnionId());
            followDTO.setTicket(wxMessage.getTicket());
            followDTO.setCreateTime(WechatUtils.formatDate(wxMessage.getCreateTime().toString()));
            userFeignClient.followWeixinAccount(followDTO);

            //是否扫门店二维码关注
            if (StrUtil.isNotBlank(wxMessage.getTicket())) {
                WxScanEventHandler handler = SpringContextUtils.getBean("wx_scan_event_handler", WxScanEventHandler.class);
                msgDTO.setVeryFirstTimeToFollowUp(isVeryFirstTimeToFollowUp);
                msgDTO.setUserWeixinAccountFollowVO(userWeixinAccountFollow.getData());
                handler.exec(msgDTO);
                logger.info("--扫码关注规则回复isDefaultRep--" + handler.isDefaultRep());
                // TODO 远程调用关注有礼活动
                if (handler.isDefaultRep()) {
                    return WechatConstants.TICKET_SUCCESS;
                }
            }


            //回复门店内容
            String eventKey = wxMessage.getEventKey();
            boolean replyDefMsg = true;
            //门店回复开关
//            WeixinConfig weixinConfigStore = weixinConfigService.getWeixinConfigByKey(appId, WechatConstants.WxConfigConstants.MP_SUBSCRIBE_STORE_OPEN);
//            Integer openStateStore = weixinConfigStore != null ? Integer.parseInt(weixinConfigStore.getParamValue()) : 2;
//            if (weixinConfigStore != null && openStateStore == 1) {
//                //校验是否首次关注
//                if (StringUtils.isNotEmpty(eventKey)) {
//                    eventKey = eventKey.split("_")[1];
//                    logger.info("--eventKey--->" + eventKey);
//                    WeixinStoreSubscribeVO storeSubscribeVO = weixinStoreSubscribeService.getStoreSubscribeByparam(appId, eventKey);
//                    if (storeSubscribeVO != null) {
//                        logger.info("--关注门店回复--->");
//                        replyDefMsg = false;
//                        List<WeixinAutoResponse> weixinAutoResponses = autoResponseService.getWeixinAutos(storeSubscribeVO.getId(), WxAutoDataSrcType.SUBSCRIBE_STORE.value());
//                        sendMsg(weixinAutoResponses, fromUser, accessToken, eventKey);
//                        // 调用关注有礼活动发送接口
//                        if (isVeryFirstTimeToFollowUp) {
//                            logger.info("--调用关注有礼活动（有门店版本）--->");
//                        }
//                        return WechatConstants.TICKET_SUCCESS;
//                    }
//                }
//            }

            //获取默认关注回复开关
            WeixinConfig weixinConfig = weixinConfigService.getWeixinConfigByKey(appId, WechatConstants.WxConfigConstants.MP_SUBSCRIBE_OPEN);
            Integer openState = weixinConfig != null ? Integer.parseInt(weixinConfig.getParamValue()) : 2;
            //获取默认内容
            if (replyDefMsg && openState == 1) {
                WeixinSubscribe weixinSubscribe = weixinSubscribeService.getWeixinSubscribe(weixinWebApp.getWeixinAppId(), 0);
                if (weixinSubscribe != null) {
                    logger.info("--关注默认回复--->");
                    List<WeixinAutoResponse> weixinAutoResponses = autoResponseService.getWeixinAutos(weixinSubscribe.getSubscribeId(), WxAutoDataSrcType.SUBSCRIBE.value());
                    sendMsg(weixinAutoResponses, fromUser, accessToken, null);
                    // 调用关注有礼活动发送接口
                    if (isVeryFirstTimeToFollowUp) {
                        logger.info("--调用关注有礼活动（无门店版本）--->");
                    }
                }
            }


            return WechatConstants.TICKET_SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return WechatConstants.FAILED;
        }
    }


    private void sendMsg(List<WeixinAutoResponse> weixinAutoResponses, String fromUser, String accessToken, String storeId) {
        //发送客服消息
        if (weixinAutoResponses != null && weixinAutoResponses.size() > 0) {
            weixinAutoResponses.stream().peek(item -> {
                WeixinTemplageManagerVO managerVO = templateManagerService.getTemplate(item.getTemplateId(), item.getMsgType());
                if (managerVO != null) {
                    if (managerVO.getTextTemplate() != null) {
                        //需要处理超文本内容【超链接】
                        String templateContent= SlognUtils.getTextHerf(managerVO.getTextTemplate().getTemplateContent(),managerVO.getTextTemplate().getTextHerfs());
                        WechatOpenApi.sendTextMsg(templateContent,fromUser,accessToken);
                    }
                    if (managerVO.getImgTemplate() != null) {
                        WechatOpenApi.sendImgMsg(managerVO.getImgTemplate().getMediaId(), fromUser, accessToken);
                    }
                    if (managerVO.getMaTemplate() != null) {
                        WeixinMaTemplateVO weixinMaTemplateVO = managerVO.getMaTemplate();

                        String path = weixinMaTemplateVO.getMaAppPath();
                        if (StrUtil.isNotBlank(storeId)) {
                            if (path.split("[?]").length > 1) {
                                path = path + "&storeId=" + storeId;
                            } else {
                                path = path + "?storeId=" + storeId;
                            }
                        }
                        WechatOpenApi.sendMiniprogrampageMsg(weixinMaTemplateVO.getCardTitle(),
                                weixinMaTemplateVO.getMaAppId(),
                                path,
                                weixinMaTemplateVO.getThumbMediaId(),
                                fromUser, accessToken);
                    }
                    if (managerVO.getNewsTemplate() != null) {

                    }
                }
            }).collect(Collectors.toList());
        }
    }

}
