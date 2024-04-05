package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Aftersale {

    @JsonProperty("aftersale_id")
    private String aftersaleId;

    @JsonProperty("out_aftersale_id")
    private String outAftersaleId;
}
