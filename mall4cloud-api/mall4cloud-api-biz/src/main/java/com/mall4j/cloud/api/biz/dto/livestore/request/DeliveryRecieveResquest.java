
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import lombok.Data;

@Data
public class DeliveryRecieveResquest extends BaseResponse {

    private String openid;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("out_order_id")
    private String outOrderId;

}
