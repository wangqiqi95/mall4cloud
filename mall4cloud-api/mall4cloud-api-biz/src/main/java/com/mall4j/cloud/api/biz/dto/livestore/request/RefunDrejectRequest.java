package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RefunDrejectRequest {

    /**
     * 外部售后单号
     */
    @JsonProperty("aftersale_id")
    private String aftersaleId;
}
