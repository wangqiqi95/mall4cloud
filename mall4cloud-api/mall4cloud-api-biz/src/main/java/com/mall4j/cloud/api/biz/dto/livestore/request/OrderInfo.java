package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderInfo {

    /**
     * order_id
     */
    @JsonProperty("order_id")
    private Long orderId;

    /**
     * out_order_id
     */
    @JsonProperty("out_order_id")
    private String outOrderId;

    /**
     * status
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * path
     */
    @JsonProperty("path")
    private String path;


    /**
     * default_receiving_address
     */
    @JsonProperty("default_receiving_address")
    private AddressInfo defaultReceivingAddress;

    /**
     * order_detail
     */
    @JsonProperty("order_detail")
    private OrderDetailInfo orderDetail;

    @JsonProperty("related_aftersale_info")
    private RelatedAftersaleInfo relatedAftersaleInfo;


    @JsonProperty("delivery_detail")
    private DeliveryDetail deliveryDetail;


}
