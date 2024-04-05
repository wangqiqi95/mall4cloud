package com.mall4j.cloud.payment.config;


import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.EntPayServiceImpl;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.common.bean.WxApp;
import com.mall4j.cloud.common.bean.WxMiniApp;
import com.mall4j.cloud.common.bean.WxMp;
import com.mall4j.cloud.common.bean.WxPay;
import com.mall4j.cloud.common.constant.PayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 通过微信配置获取微信的支付信息，登陆信息等
 * @author FrozenWatermelon
 */
@Component
public class WxConfig {

    @Autowired
    private FeignShopConfig feignShopConfig;

    /**
     * 根据支付方式，获取微信支付信息
     * @param payType
     * @return
     */
    public WxPayService getWxPayService(PayType payType) {

        String appid;
        // 小程序支付
        if (Objects.equals(payType, PayType.WECHATPAY)) {
            WxMiniApp wxMiniApp = feignShopConfig.getWxMiniApp();
            appid = wxMiniApp.getAppId();
        } else if (Objects.equals(payType, PayType.WECHATPAY_APP)) {
            WxApp wxApp = feignShopConfig.getWxApp();
            appid = wxApp.getAppId();
        } else {
            WxMp wxMp = feignShopConfig.getWxMp();
            appid = wxMp.getAppId();
        }

        WxPayConfig payConfig = getWxPay(appid);
        payConfig.setSignType(WxPayConstants.SignType.HMAC_SHA256);

        WxPayService wxPayService = new WxPayServiceImpl();

        wxPayService.setConfig(payConfig);
        return wxPayService;
    }


    public EntPayService getEntPayService() {
        String appid;
        WxMiniApp wxMiniApp = feignShopConfig.getWxMiniApp();
        // 小程序支付
        if (wxMiniApp != null && StrUtil.isNotBlank(wxMiniApp.getAppId())) {

            appid = wxMiniApp.getAppId();
        } else {
            WxMp wxMp = feignShopConfig.getWxMp();
            appid = wxMp.getAppId();
        }

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(getWxPay(appid));
        return new EntPayServiceImpl(wxPayService);
    }

    private WxPayConfig getWxPay(String appid) {
        WxPay wxPay = feignShopConfig.getWxPay();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appid);
        payConfig.setMchId(wxPay.getMchId());
        payConfig.setMchKey(wxPay.getMchKey());
        payConfig.setKeyPath(wxPay.getKeyPath());
        return payConfig;
    }

}
