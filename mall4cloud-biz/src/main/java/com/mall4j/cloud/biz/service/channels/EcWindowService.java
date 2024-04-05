package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.api.biz.dto.channels.request.EcWindowAddProductRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcWindowOffProductRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcWindowAddProductResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcWindowOffProductResponse;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.biz.wx.wx.channels.EcWindowApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.common.bean.WxMiniApp;
import com.mall4j.cloud.common.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 视频号4.0 橱窗 微信API
 * @date 2023/3/9
 */
@Service
@Slf4j
public class EcWindowService {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private FeignShopConfig feignShopConfig;

    @Autowired
    private EcWindowApi ecWindowApi;

    public EcWindowAddProductResponse addProduct(String productId, Boolean isHideForWindow){
        WxMiniApp ecApp = feignShopConfig.getWxEcApp();

        EcWindowAddProductRequest request = new EcWindowAddProductRequest();
        request.setProduct_id(productId);
        request.setAppid(ecApp.getAppId());
        request.setIs_hide_for_window(isHideForWindow);

        log.info("调用视频号4.0商品橱窗上架商品API, request param : [{}]", request);
        EcWindowAddProductResponse response = ecWindowApi.addProduct(wxConfig.getWxShopOneEcToken(), request);
        log.info("调用视频号4.0商品橱窗上架商品API, response param : [{}]", response);
        if (response == null) {
            Assert.faild("调用微信视频号4.0API失败");
        } else if (response.getErrcode() != 0) {
            Assert.faild(response.getErrmsg());
        }
        return response;
    }

    public EcWindowOffProductResponse offProduct(String productId){
        WxMiniApp ecApp = feignShopConfig.getWxEcApp();

        EcWindowOffProductRequest request = new EcWindowOffProductRequest();
        request.setProduct_id(productId);
        request.setAppid(ecApp.getAppId());

        log.info("调用视频号4.0商品橱窗下架商品API, request param : [{}]", request);
        EcWindowOffProductResponse response = ecWindowApi.offProduct(wxConfig.getWxShopOneEcToken(), request);
        log.info("调用视频号4.0商品橱窗下架商品API, response param : [{}]", response);
        if (response == null) {
            Assert.faild("调用微信视频号4.0API失败");
        } else if (response.getErrcode() != 0) {
            Assert.faild(response.getErrmsg());
        }
        return response;
    }

}
