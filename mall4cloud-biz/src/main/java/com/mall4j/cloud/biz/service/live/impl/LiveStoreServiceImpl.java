package com.mall4j.cloud.biz.service.live.impl;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.SellerInfo;
import com.mall4j.cloud.api.biz.dto.livestore.response.SellerInfoResponse;
import com.mall4j.cloud.biz.wx.wx.api.live.SellerApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.service.live.LiveStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class LiveStoreServiceImpl implements LiveStoreService {
    @Resource
    private SellerApi sellerApi;

    @Autowired
    private WxConfig wxConfig;

    @Override
    public SellerInfo getSellerInfo() {

        SellerInfoResponse sellerInfo = sellerApi.getSellerInfo(wxConfig.getWxMaToken(),"{}");
        log.info("调用微信商户信息查询 response：{}", JSONObject.toJSONString(sellerInfo));
        return sellerInfo.getData();
    }

    @Override
    public BaseResponse updateSellerInfo(SellerInfo sellerInfo) {
        log.info("调用微信商户信息编辑 request：{}", JSONObject.toJSONString(sellerInfo));
        BaseResponse baseResponse = sellerApi.updateSellerInfo(wxConfig.getWxMaToken(), sellerInfo);
        log.info("调用微信商户信息查询 response：{}", JSONObject.toJSONString(baseResponse));
        return baseResponse;
    }
}
