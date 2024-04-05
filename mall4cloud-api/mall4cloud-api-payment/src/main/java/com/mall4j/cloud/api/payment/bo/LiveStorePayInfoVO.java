package com.mall4j.cloud.api.payment.bo;

import lombok.Data;

import java.security.KeyStore;

@Data
public class LiveStorePayInfoVO {

    /**
     * 订单单号
     */
    private String orderNumber;
    /**
     * 微信支付单号
     */
    private String transactionId;
    /**
     * 支付时间
     */
    private String payTime;

}
