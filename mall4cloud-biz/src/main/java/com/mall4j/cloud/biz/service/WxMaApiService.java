package com.mall4j.cloud.biz.service;

import com.mall4j.cloud.api.biz.dto.ma.WxMaGenerateSchemeRequest;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * @Date 2022年02月14日, 0030 14:48
 * @Created by gmq
 */
public interface WxMaApiService {

    /**
     * 授权获取手机号码
     */
    ServerResponseEntity<String> getPhoneNumber(String code, String encryptedData, String ivStr,Integer authType);

    /**
     * 获取scheme码
     */
    String generateScheme(WxMaGenerateSchemeRequest request) throws WxErrorException;

}
