package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AcceptreturnRequest {

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
     * 外部售后单号
     */
    @JsonProperty("address_info")
    private AddressInfo addressInfo;

}
