package com.mall4j.cloud.api.payment.bo;

import lombok.Data;

@Data
public class LiveStoreOrderRefundInfoVO {

    /**
     * 订单单号
     */
    private String orderNumber;
    /**
     * 微信侧售后单号
     */
    private Long aftersaleId;


}
