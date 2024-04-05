package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderAddIdInfo {
    /**
     * 交易组件平台订单ID
     */
    @JsonProperty("order_id")
    private Long orderId;

    /**
     * 交易组件平台订单ID
     */
    @JsonProperty("out_order_id")
    private String outOrderId;
}
