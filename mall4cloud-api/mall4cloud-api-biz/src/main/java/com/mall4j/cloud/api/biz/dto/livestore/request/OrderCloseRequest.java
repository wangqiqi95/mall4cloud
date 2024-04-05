package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderCloseRequest {

    /**
     * 商家自定义订单ID，与 order_id 二选一
     */
    @JsonProperty("out_order_id")
    private String outOrderId;

    /**
     * 用户的openid
     */
    @JsonProperty("openid")
    private String openid;
}
