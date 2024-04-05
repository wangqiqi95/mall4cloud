package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 生产支付参数对象
 */
@Data
public class PaymentparamsRequest {
    /**
     * 微信侧订单id
     */
    @JsonProperty("order_id")
    private String orderId;
    /**
     * 商家自定义订单ID
     */
    @JsonProperty("out_order_id")
    private String outOrderId;
    /**
     * 用户的openid
     */
    @JsonProperty("openid")
    private String openId;
}
