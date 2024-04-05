package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class SellerInfo  {
    @JsonProperty("service_agent_type")
    private List<Integer> serviceAgentType;
    @JsonProperty("service_agent_path")
    private String serviceAgentPath;
    @JsonProperty("service_agent_phone")
    private String serviceAgentPhone;
    @JsonProperty("default_receiving_address")
    private ReturnAddr defaultReceivingAddress;
}
