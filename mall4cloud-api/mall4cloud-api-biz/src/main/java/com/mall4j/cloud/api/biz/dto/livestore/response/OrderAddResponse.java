package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

@Data
public class OrderAddResponse extends BaseResponse {

    /**
     * 订单 id 信息
     */
    @JsonProperty("data")
    private OrderAddIdInfo data;


}
