package com.mall4j.cloud.payment.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import com.alipay.api.*;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.common.bean.Alipay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 支付宝支付设置
 *
 * @author FrozenWatermelon
 */
@Component
public class AliPayConfig {

    @Autowired
    private FeignShopConfig feignShopConfig;

    public AlipayClient getAlipayClient() throws AlipayApiException {

        Alipay alipay = feignShopConfig.getAlipay();

        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
        //设置应用Id
        certAlipayRequest.setAppId(alipay.getAppId());
        //设置应用私钥
        certAlipayRequest.setPrivateKey(alipay.getAppPrivateKey());
        //设置请求格式，固定值json
        certAlipayRequest.setFormat("json");
        //设置字符集
        certAlipayRequest.setCharset(CharsetUtil.UTF_8);
        //设置签名类型
        certAlipayRequest.setSignType(AlipayConstants.SIGN_TYPE_RSA2);

        //设置应用公钥证书路径
        certAlipayRequest.setCertPath(FileUtil.getAbsolutePath(alipay.getAppCertPath()));
        //设置支付宝公钥证书路径
        certAlipayRequest.setAlipayPublicCertPath(FileUtil.getAbsolutePath(alipay.getAlipayCertPath()));
        //设置支付宝根证书路径
        certAlipayRequest.setRootCertPath(FileUtil.getAbsolutePath(alipay.getAlipayRootCertPath()));
        //构造client
        return new DefaultAlipayClient(certAlipayRequest);
    }
}
