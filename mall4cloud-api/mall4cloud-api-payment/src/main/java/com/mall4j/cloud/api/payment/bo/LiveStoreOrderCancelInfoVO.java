package com.mall4j.cloud.api.payment.bo;

import lombok.Data;

@Data
public class LiveStoreOrderCancelInfoVO {

    /**
     * 订单单号
     */
    private String orderNumber;
    /**
     * 0，用户取消；1，超时取消；2，全部商品售后完成,订单取消
     */
    private Integer cancelType;

}
