package com.mall4j.cloud.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.dto.WeixinTmessageStatusDTO;
import com.mall4j.cloud.biz.model.WeixinTmessageSendlog;
import com.mall4j.cloud.biz.model.WeixinWebApp;
import com.mall4j.cloud.biz.service.WeixinTmessageSendlogService;
import com.mall4j.cloud.biz.service.WeixinWebAppService;
import com.mall4j.cloud.biz.vo.TmessageSendVO;
import com.mall4j.cloud.biz.vo.WeixinTmessageVO;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.biz.model.WeixinTmessage;
import com.mall4j.cloud.biz.mapper.WeixinTmessageMapper;
import com.mall4j.cloud.biz.service.WeixinTmessageService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信消息模板
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 16:17:14
 */
@Slf4j
@Service
public class WeixinTmessageServiceImpl implements WeixinTmessageService {

    @Autowired
    private WeixinTmessageMapper weixinTmessageMapper;

    @Autowired
    private WeixinWebAppService weixinWebAppService;

    @Autowired
    private WeixinTmessageSendlogService tmessageSendlogService;

    @Autowired
    private WxConfig wxConfig;

    @Override
    public PageVO<WeixinTmessageVO> page(PageDTO pageDTO,String appId,Integer dataSrc) {
        return PageUtil.doPage(pageDTO, () -> weixinTmessageMapper.getList(appId,dataSrc));
    }

    @Override
    public WeixinTmessage getById(String id) {
        return weixinTmessageMapper.getById(id);
    }

    @Override
    public void save(WeixinTmessage weixinTmessage) {
        weixinTmessage.setId(RandomUtil.getUniqueNumStr());
        weixinTmessage.setCreateBy(String.valueOf(AuthUserContext.get().getUserId()));
        weixinTmessage.setCreateTime(new Date());
        weixinTmessage.setDelFlag(0);
        weixinTmessage.setStatus(0);
        weixinTmessageMapper.save(weixinTmessage);
    }

    @Override
    public void update(WeixinTmessage weixinTmessage) {
        weixinTmessage.setUpdateBy(String.valueOf(AuthUserContext.get().getUserId()));
        weixinTmessage.setUpdateTime(new Date());
        weixinTmessageMapper.update(weixinTmessage);
    }

    @Override
    public void deleteById(String id) {
        weixinTmessageMapper.deleteById(id);
    }

    /**
     * 模板消息启用/禁用
     * @param weixinTmessageStatusDTO
     * @return
     */
    @Override
    public ServerResponseEntity<Void> updateStatus(WeixinTmessageStatusDTO weixinTmessageStatusDTO) {
        WeixinTmessage weixinTmessage=this.getById(weixinTmessageStatusDTO.getId());
        if(weixinTmessage==null){
            return ServerResponseEntity.showFailMsg("实体未找到");
        }
        weixinTmessage=new WeixinTmessage();
        weixinTmessage.setId(weixinTmessageStatusDTO.getId());
        weixinTmessage.setStatus(weixinTmessageStatusDTO.getStatus());
        this.update(weixinTmessage);
        return ServerResponseEntity.success();
    }

    /**
     * 发送模板消息
     * @param tmessageSendVO
     * @return
     */
    @Override
    public JSONObject sendTemplateMsg(TmessageSendVO tmessageSendVO) {

        //组合消息内容 发送
        JSONObject result=this.createSendTemplateMsg(tmessageSendVO);

        if(result!=null){
            //保存发送日志
            WeixinTmessageSendlog tmessageSendlog=new WeixinTmessageSendlog();
            tmessageSendlog.setId(RandomUtil.getUniqueNumStr());
            tmessageSendlog.setOpenId(tmessageSendVO.getTouser());
            tmessageSendlog.setDataJson(tmessageSendVO.getData());
            tmessageSendlog.setTemplateId(tmessageSendVO.getTemplateId());
            tmessageSendlog.setAppId(tmessageSendVO.getMpAppId());
            tmessageSendlog.setMaAppId(tmessageSendVO.getAppId());
            tmessageSendlog.setTmessageId(tmessageSendVO.getTmessageId());
            tmessageSendlog.setMiniprogram(tmessageSendVO.getMiniProgram());
            tmessageSendlog.setPagepath(tmessageSendVO.getPagePath());
            tmessageSendlog.setCreateTime(new Date());
            tmessageSendlog.setCreateBy(String.valueOf(AuthUserContext.get().getUserId()));
            tmessageSendlog.setDelFlag(0);
            String errcode = result.getString("errcode");
            if ("0".equals(errcode)) {
                tmessageSendlog.setMsgId(result.getString("msgid"));
                tmessageSendlog.setStatus(0);
            } else {
                tmessageSendlog.setStatus(1);
            }
            tmessageSendlogService.save(tmessageSendlog);
        }

        return result;
    }

//    @Override
//    public JSONObject createSendTemplateMsg(TmessageSendVO tmessageSendVO) {
//        JSONObject result = null;
//        if(StringUtils.isEmpty(tmessageSendVO.getTouser())) {
//            log.info("发送模板消息失败---接收者openid为空");
//            return result;
//        }
//        if(StringUtils.isEmpty(tmessageSendVO.getTemplateId())) {
//            log.info("发送模板消息失败---模板ID为空");
//            return result;
//        }
//        if(StringUtils.isEmpty(tmessageSendVO.getMpAppId())) {
//            log.info("发送模板消息失败---mpAppId为空");
//            return result;
//        }
//        WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(tmessageSendVO.getMpAppId());
//        if(weixinWebApp==null){
//            log.info("发送模板消息失败----"+tmessageSendVO.getMpAppId()+"---公众号未授权");
//            return result;
//        }
//        if(StringUtils.isEmpty(tmessageSendVO.getData())) {
//            log.info("发送模板消息失败---模板数据为空");
//            return result;
//        }
//        try {
//            WxMp wxMp=new WxMp();
//            wxMp.setAppId(weixinWebApp.getWeixinAppId());
//            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
//            WxMpTemplateMessage wxMpTemplateMessage=new WxMpTemplateMessage();
//            wxMpTemplateMessage.setTemplateId(tmessageSendVO.getTemplateId());
//            wxMpTemplateMessage.setToUser(tmessageSendVO.getTouser());
//            wxMpTemplateMessage.setUrl(tmessageSendVO.getUrl());
//            //小程序信息
//            if(StringUtils.isNotEmpty(tmessageSendVO.getMiniProgram())){
//                WxMpTemplateMessage.MiniProgram miniProgram=JSON.parseObject(tmessageSendVO.getMiniProgram(),WxMpTemplateMessage.MiniProgram.class);
//                miniProgram.setAppid(tmessageSendVO.getAppId());
//                miniProgram.setPagePath(tmessageSendVO.getPagePath());
//                wxMpTemplateMessage.setMiniProgram(miniProgram);
//            }
//            //模板数据为空
//            List<WxMpTemplateData> templateDataList=JSON.parseArray(tmessageSendVO.getData(), WxMpTemplateData.class);
//            wxMpTemplateMessage.setData(templateDataList);
//
//            String back=wxConfig.getWxMpService(wxMp).getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
//            result=JSON.parseObject(back);
//
//            return result;
//        }catch (Exception e){
//            e.printStackTrace();
//            log.info("发送模板消息失败"+e.getMessage());
//            return result;
//        }
//
//    }

    @Override
    public JSONObject createSendTemplateMsg(TmessageSendVO tmessageSendVO) {
        JSONObject result = null;
        if(StringUtils.isEmpty(tmessageSendVO.getTouser())) {
            log.info("发送模板消息---接收者openid为空");
            return result;
        }
        if(StringUtils.isEmpty(tmessageSendVO.getTemplateId())) {
            log.info("发送模板消息---模板ID为空");
            return result;
        }
        if(StringUtils.isEmpty(tmessageSendVO.getMpAppId())) {
            log.info("发送模板消息---公众号appId为空");
            return result;
        }
        WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(tmessageSendVO.getMpAppId());
        if(weixinWebApp==null){
            log.info(tmessageSendVO.getMpAppId()+"---公众号未授权");
            return result;
        }
        if(StringUtils.isEmpty(tmessageSendVO.getData())) {
            log.info("发送模板消息---模板数据为空");
            return result;
        }
        Map<String,Object> param = new HashMap<String,Object>();
        //接收者openid
        param.put("touser", tmessageSendVO.getTouser());
        //模板ID
        param.put("template_id", tmessageSendVO.getTemplateId());
        //模板数据
        JSONObject dataJson = JSONObject.parseObject(tmessageSendVO.getData());
        param.put("data",(Map) dataJson);
        //模板跳转链接
        if(StringUtils.isNotEmpty(tmessageSendVO.getUrl())) {
            param.put("url", tmessageSendVO.getUrl());
        }
        //跳小程序所需数据
        if(StringUtils.isNotEmpty(tmessageSendVO.getMiniProgram())) {
            param.put("miniprogram", tmessageSendVO.getMiniProgram());
        }
        //所需跳转到的小程序appid
        if(StringUtils.isNotEmpty(tmessageSendVO.getAppId())) {
            param.put("appid", tmessageSendVO.getAppId());
        }
        //所需跳转到小程序的具体页面路径
        if(StringUtils.isNotEmpty(tmessageSendVO.getPagePath())) {
            param.put("pagepath", tmessageSendVO.getPagePath());
        }
        try {
            WxMp wxMp=new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
            String accessToken=wxConfig.getWxMpService(wxMp).getAccessToken();
            if (accessToken != null) {
                String requestUrl = WechatConstants.SEND_TEMPLATE_MSG_URL;
                requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
                String obj = JSON.toJSONString(param);
                log.info("发送模板消息方法执行前json参数 :{obj :" + obj.toString() + "}");
                result=WechatUtils.doPoststr(requestUrl, obj);
                log.info("发送模板消息方法执行后json参数 :{result :" + result.toString() + "}");
            }else{
                log.info("发送模板消息失败，accessToken为空");
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            log.info("发送模板消息失败"+e.getMessage());
            return result;
        }
    }

    /**
     * 获取微信公众号全部消息模板列表
     * @param appId
     * @return
     */
    @Override
    public List<WxMpTemplate> getAllPrivateTemplate(String appId) {
        WeixinWebApp weixinWebApp=weixinWebAppService.queryByAppid(appId);
        if(weixinWebApp==null){
            log.info(appId+"---公众号未授权");
            return null;
        }
        try {
            WxMp wxMp=new WxMp();
            wxMp.setAppId(weixinWebApp.getWeixinAppId());
            wxMp.setSecret(weixinWebApp.getWeixinAppSecret());
//            String accessToken=wxConfig.getWxMpService(wxMp).getAccessToken();
            List<WxMpTemplate> wxMpTemplates=wxConfig.getWxMpService(wxMp).getTemplateMsgService().getAllPrivateTemplate();
//            if (accessToken != null) {
//                String requestUrl = WechatConstants.GET_ALL_PRIVATE_TEMPLATE;
//                requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
//                JSONObject result=WechatUtils.doGetstr(requestUrl);
//                log.info("获取微信公众号全部消息模板列表 :{result :" + result.toString() + "}");
//
//                if(result.containsKey("template_list")) {
//                    JSONArray jsonArray=result.getJSONArray("template_list");
//                    List<WeixinTmessageAllVO> tmessageVOS=JSON.parseArray(jsonArray.toJSONString(),WeixinTmessageAllVO.class);
//                    return tmessageVOS;
//                }
//            }else{
//                log.info("获取微信公众号全部消息模板列表，accessToken为空");
//            }
            return wxMpTemplates;
        }catch (Exception e){
            e.printStackTrace();
            log.info("获取微信公众号全部消息模板列表"+e.getMessage());
            return null;
        }
    }

}
