package com.mall4j.cloud.biz.service.impl.handler;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.user.constant.BuildTagFromEnum;
import com.mall4j.cloud.api.user.crm.model.UpdateTagModel;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserWeixinAccountFollowVO;
import com.mall4j.cloud.biz.dto.WxCpMsgDTO;
import com.mall4j.cloud.biz.wx.cp.utils.SlognUtils;
import com.mall4j.cloud.biz.wx.wx.api.WechatOpenApi;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.wx.wx.constant.WxAutoDataSrcType;
import com.mall4j.cloud.biz.wx.wx.constant.WxQRSceneSrcType;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.WeixinQrcodeScanRecordDTO;
import com.mall4j.cloud.biz.model.WeixinAutoResponse;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.*;
import com.mall4j.cloud.biz.vo.WeixinAutoScanVO;
import com.mall4j.cloud.biz.vo.WeixinMaTemplateVO;
import com.mall4j.cloud.biz.vo.WeixinQrcodeVO;
import com.mall4j.cloud.biz.vo.WeixinTemplageManagerVO;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.response.ServerResponseEntity;
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
 * 扫码回复
 */
@Service("wx_scan_event_handler")
@Slf4j
public class WxScanEventHandler implements WechatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxScanEventHandler.class);

    @Autowired
    private WeixinWebAppService weixinWebAppService;
    @Autowired
    private WeixinQrcodeService qrcodeService;
    @Autowired
    private WeixinAutoScanService weixinAutoScanService;
    @Autowired
    private WeixinAutoResponseService autoResponseService;
    @Autowired
    private WeiXinTemplateManagerService templateManagerService;
    @Autowired
    private WeixinQrcodeScanRecordService qrcodeScanRecordService;
    @Autowired
    private WeixinActoinLogsService actoinLogsService;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private WxConfig wxConfig;
//    @Autowired
//    private CrmManager crmManager;

    private boolean defaultRep=false;

    public boolean isDefaultRep() {
        return defaultRep;
    }

    public void setDefaultRep(boolean defaultRep) {
        this.defaultRep = defaultRep;
    }

    @Override
    public String exec(WxCpMsgDTO msgDTO) throws WxErrorException {
        String xml=msgDTO.getXml();
//        Boolean isVeryFirstTimeToFollowUp=msgDTO.getVeryFirstTimeToFollowUp();
        logger.info("WxScanEventHandler------->exec："+xml);
        WxMpXmlMessage wxMessage=Objects.isNull(msgDTO.getWxMessage())?WechatUtils.parseXmlMessage(xml):msgDTO.getWxMessage();
        logger.info("WxScanEventHandler------->exec："+JSON.toJSONString(wxMessage));

        this.setDefaultRep(false);

        try {

            //获取授权公众号信息
            WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(wxMessage.getToUser());
            if(weixinWebApp==null){
                logger.info("WxScanEventHandler 公众号未授权 --->"+wxMessage.getToUser());
                return WechatConstants.TICKET_SUCCESS;
            }

            //保存消息推送日志
            actoinLogsService.saveWeixinActoinLogs(wxMessage,weixinWebApp.getAppId());


            WxMp wxMp=new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
            String accessToken=wxConfig.getWxMpService(wxMp).getAccessToken();
            String fromUser=wxMessage.getFromUser();
            //根据ticket获取二维码信息
            WeixinQrcodeVO weixinQrcode=qrcodeService.getByTicket(weixinWebApp.getWeixinAppId(),wxMessage.getTicket());
            if(weixinQrcode!=null){
                //获取扫码回复内容
                List<WeixinAutoScanVO> weixinAutoScanVOS=weixinAutoScanService.getReplyListByQrcode(weixinWebApp.getWeixinAppId(),weixinQrcode.getId());
                if(weixinAutoScanVOS!=null && weixinAutoScanVOS.size()>0){

                    this.setDefaultRep(true);

                    WxMpUser wxMpUser=this.getWxMpUser(wxMp,wxMessage.getFromUser());
                    if(Objects.isNull(msgDTO.getVeryFirstTimeToFollowUp())){
                        ServerResponseEntity<UserWeixinAccountFollowVO> userWeixinAccountFollow = userFeignClient.getUserWeixinAccountFollow(wxMpUser.getUnionId(),weixinWebApp.getWeixinAppId());
                        if (userWeixinAccountFollow.isSuccess()) {
                            msgDTO.setUserWeixinAccountFollowVO(userWeixinAccountFollow.getData());
                        }
                    }
                    if(Objects.isNull(msgDTO.getVeryFirstTimeToFollowUp())){
                        msgDTO.setVeryFirstTimeToFollowUp(isVeryFirstTimeToFollowUp(wxMpUser.getUnionId(),weixinWebApp.getWeixinAppId()));
                    }

                    logger.info("WxMpUser----->" + wxMpUser.toString());
                    weixinAutoScanVOS.stream().peek(item->{
                        //区分回复内容-交互类型（用户交互类型: 0全部 1:已关注用户 2：未关注用户）
                        Integer type=item.getType();
                        boolean canSendMsg=false;

                        log.info("---wxMpUser---->"+wxMpUser.toString());
                        logger.info("--type:{}  isVeryFirstTimeToFollowUp-{}",type,msgDTO.getVeryFirstTimeToFollowUp());
                        if(type==1){
                            if(!msgDTO.getVeryFirstTimeToFollowUp()){//已经关注过需要回复
                                canSendMsg=true;
                            }
                        }else if(type==2){
                            UserWeixinAccountFollowVO data = msgDTO.getUserWeixinAccountFollowVO();
                            if (data == null) {
                                canSendMsg=true;
                                logger.info("首次关注需要发送消息，UserWeixinAccountFollowVO:{}", JSON.toJSONString(data));
                            }
                        }else{
                            canSendMsg=true;
                        }

                        //统计扫码用户信息，用于扫码自动回复管理-数据详情
                        saveScanDate(weixinWebApp.getWeixinAppId(),item.getId(),wxMpUser,wxMessage,weixinQrcode);

                        //给客户打标签
                        addTagToCust(wxMpUser.getUnionId(),item.getTags());

                        logger.info("扫码动作是否需要发送消息canSendMsg：{}",canSendMsg);

                        if(!canSendMsg){
                            return;
                        }

                        //回复内容-发送客服消息
                        List<WeixinAutoResponse> weixinAutoResponses=autoResponseService.getWeixinAutos(item.getId(), WxAutoDataSrcType.QR_SCAN.value());
                        logger.info("扫码回复内容条数：{}",weixinAutoResponses.size());
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
                                        String path=weixinMaTemplateVO.getMaAppPath();
                                        if(StringUtils.isNotEmpty(weixinQrcode.getStoreId())){
                                            if(path.split("[?]").length>1){
                                                path=path+"&storeId="+weixinQrcode.getStoreId();
                                            }else{
                                                path=path+"?storeId="+weixinQrcode.getStoreId();
                                            }
                                        }
                                        WechatOpenApi.sendMiniprogrampageMsg(weixinMaTemplateVO.getCardTitle(),
                                                weixinMaTemplateVO.getMaAppId(),
                                                path,
                                                weixinMaTemplateVO.getThumbMediaId(),
                                                fromUser,accessToken);
                                    }
                                    if(managerVO.getNewsTemplate()!=null){

                                    }
                                }
                            }).collect(Collectors.toList());
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

    private void saveScanDate(String appId,String autoScanId,WxMpUser wxMpUser,WxMpXmlMessage wxMessage,WeixinQrcodeVO weixinQrcode){
        try {
            WeixinQrcodeScanRecordDTO scanRecordDTO=new WeixinQrcodeScanRecordDTO();
            scanRecordDTO.setAppId(appId);
            scanRecordDTO.setAutoScanId(autoScanId);
            scanRecordDTO.setOpenid(wxMessage.getFromUser());
            scanRecordDTO.setNickName(Base64.decodeStr(wxMpUser.getNickname()));
            scanRecordDTO.setHeadimgurl(wxMpUser.getHeadImgUrl());
            scanRecordDTO.setQrcodeId(weixinQrcode.getId());
            scanRecordDTO.setTicket(wxMessage.getTicket());
            scanRecordDTO.setSceneId(String.valueOf(weixinQrcode.getSceneId()));
            scanRecordDTO.setSceneSrc(WxQRSceneSrcType.scan_auto_msg.value());
            scanRecordDTO.setScanTime(WechatUtils.formatDate(wxMessage.getCreateTime().toString()));
            scanRecordDTO.setIsScanSubscribe("0");
            qrcodeScanRecordService.saveQrcodeScanRecord(scanRecordDTO);
        }catch (Exception e){
            log.error("saveScanDate---->"+e.getMessage());
        }
    }

    private WxMpUser getWxMpUser(WxMp wxMp,String openId){
       try {
           return wxConfig.getWxMpService(wxMp).getUserService().userInfo(openId);
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
    }

    /**
     * TODO 给客户打标签
     */
    public void addTagToCust(String externalUserUnionId ,String tags) {
        try{
            if(StrUtil.isEmpty(tags)){
                log.info("----公众号自动回复 cust addTagToCust -------：【faile】tags isEmpty");
                return;
            }
            UpdateTagModel updateTagModel=new UpdateTagModel();
            updateTagModel.setQiWeiUserUnionIds(ListUtil.toList(externalUserUnionId));
            updateTagModel.setTags(tags);
            //todo 待实现
//            crmManager.updateUserTagByCrm(updateTagModel, BuildTagFromEnum.WX_MP.getCode());
            log.info("----公众号自动回复 cust addTagToCust -------：【SUCCESS】");
        }catch (Exception e){
            log.error("客户加标签失败",e);
        }
    }

    private boolean isVeryFirstTimeToFollowUp(String unionId,String weixinAppId){
        boolean isVeryFirstTimeToFollowUp = true;
        ServerResponseEntity<UserWeixinAccountFollowVO> userWeixinAccountFollow = userFeignClient.getUserWeixinAccountFollow(unionId,weixinAppId);
        if (userWeixinAccountFollow.isSuccess()) {
            UserWeixinAccountFollowVO data = userWeixinAccountFollow.getData();
            if (data != null && data.getStatus()==1) {
                isVeryFirstTimeToFollowUp = false;
                logger.info("之前关注过，不再发送活动，用户UnionId:{}，WechatAppId:{}", unionId,weixinAppId);
            }
        } else {
            isVeryFirstTimeToFollowUp = false;
            logger.error("公众号({})未绑定微信公众平台，导致获取不到全局UnionId，所以不调用关注有礼", weixinAppId);
        }
        logger.info("--getUserWeixinAccountFollow 关注信息--{}  isVeryFirstTimeToFollowUp-{}", JSON.toJSONString(userWeixinAccountFollow),isVeryFirstTimeToFollowUp);
        return isVeryFirstTimeToFollowUp;
    }

}
