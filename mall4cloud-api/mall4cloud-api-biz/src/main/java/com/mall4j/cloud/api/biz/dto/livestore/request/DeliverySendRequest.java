
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeliverySendRequest {

    /**
     * 快递信息，delivery_type=1时必填
     */
    @JsonProperty("delivery_list")
    private List<DeliveryInfo> deliveryList;
    /**
     * 发货完成标志位, 0: 未发完, 1:已发完
     */
    @JsonProperty("finish_all_delivery")
    private Integer finishAllDelivery;
    private String openid;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("out_order_id")
    private String outOrderId;
    @JsonProperty("ship_done_time")
    private String shipDoneTime;
}
