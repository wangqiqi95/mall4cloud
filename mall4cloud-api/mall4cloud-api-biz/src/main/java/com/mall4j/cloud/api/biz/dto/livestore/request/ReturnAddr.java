package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReturnAddr {
    @JsonProperty("receiver_name")
    private String receiverName;
    @JsonProperty("detailed_address")
    private String detailedAddress;
    @JsonProperty("tel_number")
    private String telNumber;
    @JsonProperty("country")
    private String country;
    @JsonProperty("province")
    private String province;
    @JsonProperty("city")
    private String city;
    @JsonProperty("town")
    private String town;
}
