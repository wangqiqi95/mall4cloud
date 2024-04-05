package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AfterSaleInfo {

    /**
     * 售后单号
     */
    @JsonProperty("aftersale_id")
    private Long aftersaleId;
}
