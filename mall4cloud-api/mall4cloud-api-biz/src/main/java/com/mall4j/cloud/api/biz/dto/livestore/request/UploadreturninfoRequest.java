package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UploadreturninfoRequest {
    /**
     * 商家自定义订单ID
     */
    @JsonProperty("out_aftersale_id")
    private String outAftersaleId;
    /**
     * 与out_aftersale_id二选一
     */
    @JsonProperty("aftersale_id")
    private Long aftersaleId;
    /**
     * 用户openid
     */
    @JsonProperty("openid")
    private String openid;
    /**
     * 快递公司ID
     */
    @JsonProperty("delivery_id")
    private String deliveryId;
    /**
     * 快递单号
     */
    @JsonProperty("waybill_id")
    private String waybillId;
    /**
     * 快递公司名字
     */
    @JsonProperty("delivery_name")
    private String deliveryName;

}
