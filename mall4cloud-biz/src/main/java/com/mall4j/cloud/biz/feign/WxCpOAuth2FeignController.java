package com.mall4j.cloud.biz.feign;

import cn.binarywang.wx.miniapp.util.crypt.WxMaCryptUtils;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.feign.WxCpApiFeignClient;
import com.mall4j.cloud.api.biz.feign.WxCpAuth2FeignClient;
import com.mall4j.cloud.api.biz.vo.WxCpUserInfoVO;
import com.mall4j.cloud.biz.config.WxCpConfigurationPlus;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMaJsCode2SessionResult;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.WxCpUserDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: hwy
 * @Description: 通过code获取用户详细信息
 * @Date: 2022-01-18 17:37
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class WxCpOAuth2FeignController implements WxCpAuth2FeignClient {
    private  final WxCpService wxCpService;
    private final WxCpApiFeignClient wxCpApiFeignClient;
    private final String AUTH_GETUSERINFO="https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";
    private final String AUTH_GETUSERINFO_DETAIL="https://qyapi.weixin.qq.com/cgi-bin/auth/getuserdetail?access_token=ACCESS_TOKEN";

    @Override
    public ServerResponseEntity<WxCpUserInfoVO> getUserInfo(String code)  {
        try {
            WxCpMaJsCode2SessionResult wxCpOauth2UserInfo = wxCpService.jsCode2Session(code);
            WxCpUser wxCpUser = wxCpService.getUserService().getById(wxCpOauth2UserInfo.getUserId());
            WxCpUserInfoVO response = new WxCpUserInfoVO();
            BeanUtils.copyProperties(wxCpUser, response);
            response.setUserid(wxCpOauth2UserInfo.getUserId());
            response.setSessionKey(wxCpOauth2UserInfo.getSessionKey());
            return ServerResponseEntity.success(response);

        }catch (WxErrorException wxe){
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    public ServerResponseEntity<WxCpUserInfoVO> getUserInfo2(String sessionKey, String encryptedData, String ivStr) {
        try {
//            WxCpMaJsCode2SessionResult wxCpOauth2UserInfo = wxCpService.jsCode2Session(code);
//            WxCpUser wxCpUser = wxCpService.getUserService().getById(wxCpOauth2UserInfo.getUserId());
            WxCpUserInfoVO response = new WxCpUserInfoVO();
//            BeanUtils.copyProperties(wxCpUser, response);
            log.info("获取的手机号对象1:sessionKey：{},encryptedData：{},ivStr:{}",sessionKey,encryptedData,ivStr);
//            WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxConfig.getWxMaService().getUserService().getPhoneNoInfo(sessionKey,encryptedData,ivStr);
            String result = WxMaCryptUtils.decrypt(sessionKey, encryptedData, ivStr);
            log.info("获取的手机号对象:{}",result);
            String moblie = JSONObject.parseObject(result).getString("mobile");
            response.setMobile(moblie);
            return ServerResponseEntity.success(response);
        }catch (Exception wxe){
            wxe.printStackTrace();
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    public ServerResponseEntity<WxCpUserInfoVO> getUserInfoById(String userId) {
        try {
            WxCpUser wxCpUser =  WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getUserService().getById(userId);
            WxCpUserInfoVO response = new WxCpUserInfoVO();
            BeanUtils.copyProperties(wxCpUser, response);
            response.setUserid(userId);
            return ServerResponseEntity.success(response);

        }catch (WxErrorException wxe){
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
    }

    private WxCpUserDetail getWxCpUserDetail(String user_ticket){
        if(StrUtil.isEmpty(user_ticket)){
            log.info("getWxCpUserDetail user_ticket is null");
            return null;
        }
        try {
            WxCpUserDetail wxCpUserDetail= WxCpConfigurationPlus.getWxCpService(WxCpConfigurationPlus.getAgentId()).getOauth2Service().getUserDetail(user_ticket);
            return wxCpUserDetail;
        }catch (WxErrorException e){
            log.error("getWxCpUserDetail error {}",e);
        }
       return null;
    }
}
