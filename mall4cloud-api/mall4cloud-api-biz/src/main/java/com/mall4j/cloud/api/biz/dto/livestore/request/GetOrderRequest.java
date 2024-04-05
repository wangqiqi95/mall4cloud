package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetOrderRequest {
    /**
     * 微信侧订单id
     */
    @JsonProperty("order_id")
    private Long orderId;
    /**
     * 商家自定义订单ID
     */
    @JsonProperty("out_order_id")
    private String outOrderId;
    /**
     * 用户的openid
     */
    @JsonProperty("openid")
    private String openid;
}
