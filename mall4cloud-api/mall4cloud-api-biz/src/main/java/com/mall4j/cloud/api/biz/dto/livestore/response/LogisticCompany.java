package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LogisticCompany {

    @JsonProperty("delivery_id")
    private String deliveryId;

    @JsonProperty("delivery_name")
    private String deliveryName;
}
