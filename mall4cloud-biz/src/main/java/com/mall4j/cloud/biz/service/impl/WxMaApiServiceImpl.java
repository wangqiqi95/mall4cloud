package com.mall4j.cloud.biz.service.impl;


import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import com.mall4j.cloud.biz.wx.wx.constant.WechatConstants;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.api.biz.dto.ma.WxMaGenerateSchemeRequest;
import com.mall4j.cloud.biz.service.WxMaApiService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.json.GsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.binarywang.wx.miniapp.constant.WxMaApiUrlConstants.Scheme.GENERATE_SCHEME_URL;

/**
 * @Date 2022年02月14日, 0030 14:48
 * @Created by gmq
 */
@Slf4j
@Service
public class WxMaApiServiceImpl implements WxMaApiService {

    @Autowired
    private WxConfig wxConfig;

    private static final String ERR_CODE = "errcode";

    /**
     * 授权获取手机号
     * @param code
     * @param encryptedData
     * @param ivStr
     * @return
     */
    @Override
    public ServerResponseEntity<String> getPhoneNumber(String code, String encryptedData, String ivStr,Integer authType) {
        try {
            log.info("【授权获取手机号】请求参数：code：【{}】，encryptedData：【{}】，ivStr：【{}】，authType：【{}】",code, encryptedData,ivStr, authType);
            String mobile=null;

            if(code.equals("undefined")) code=null;

            if(authType==1 && StrUtil.isNotBlank(code)){
                String assessToken=wxConfig.getWxMaToken();
                JSONObject j = new JSONObject();
                j.put("code", code);
                String jsonString=j.toJSONString();
                String apiGetAuthorizerInfo = WechatConstants.GET_USER_PHONE_NUMBER.replace("ACCESS_TOKEN", assessToken);
                log.info("授权获取手机号，请求参数：【{}】，请求路径：【{}】", jsonString, apiGetAuthorizerInfo);
                JSONObject jsonObj = WechatUtils.doPoststr(apiGetAuthorizerInfo, jsonString);
                log.info("授权获取手机号 返回参数: "+jsonObj);
                if (jsonObj != null) {
                    JSONObject phoneObj= jsonObj.getJSONObject("phone_info");
                    if(phoneObj!=null){
                        mobile=phoneObj.getString("phoneNumber");
                        return ServerResponseEntity.success(mobile);
                    }
                }
                return ServerResponseEntity.showFailMsg("获取手机号失败: "+jsonObj.toJSONString());
            }else if(authType==2 && StrUtil.isNotBlank(encryptedData) && StrUtil.isNotBlank(ivStr) && StrUtil.isNotBlank(code)){
                WxMaJscode2SessionResult session = wxConfig.getWxMaService().getUserService().getSessionInfo(code);
                if (session == null) {
                    return ServerResponseEntity.showFailMsg("未获取到手机号");
                }
                //手机号
                WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxConfig.getWxMaService().getUserService().getPhoneNoInfo(
                        session.getSessionKey(),
                        encryptedData,
                        ivStr);
                mobile = wxMaPhoneNumberInfo.getPhoneNumber();
                if(mobile!=null){
                    return ServerResponseEntity.success(mobile);
                }else{
                    return ServerResponseEntity.showFailMsg("未获取到手机号");
                }
            }
            return ServerResponseEntity.showFailMsg("未获取到手机号");
        } catch (Exception e) {
            log.info(e.getMessage());
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    @Override
    public String generateScheme(WxMaGenerateSchemeRequest request) throws WxErrorException {
        String responseContent = wxConfig.getWxMaService().post(GENERATE_SCHEME_URL, request.toJson());
        JsonObject jsonObject = GsonParser.parse(responseContent);
        if (jsonObject.get(ERR_CODE).getAsInt() != 0) {
            throw new WxErrorException(WxError.fromJson(responseContent, WxType.MiniApp));
        }
        return jsonObject.get("openlink").getAsString();
    }
}
