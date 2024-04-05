package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentParams {

    @JsonProperty("timeStamp")
    private String timeStamp;
    @JsonProperty("nonceStr")
    private String nonceStr;
    @JsonProperty("package")
    private String packages;
    @JsonProperty("paySign")
    private String paySign;
    @JsonProperty("signType")
    private String signType;

}
