package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.request.OrderDetailInfo;
import com.mall4j.cloud.api.biz.dto.livestore.request.OrderInfo;
import lombok.Data;

@Data
public class GetOrderResponse extends BaseResponse {


    @JsonProperty("order")
    private OrderInfo orderInfo;


}
