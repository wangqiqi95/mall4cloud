package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryDetail {

    /**
     * 1: 正常快递, 2: 无需快递, 3: 线下配送, 4: 用户自提，视频号场景目前只支持 1，正常快递
     */
    @JsonProperty("delivery_type")
    private Integer deliveryType;

    /**
     * 是否发货完成
     */
    @JsonProperty("finish_all_delivery")
    private Integer finishAllDelivery;


    /**
     * 发货完成时间
     */
    @JsonProperty("ship_done_time")
    private String shipDoneTime;

    /**
     *
     */
    @JsonProperty("delivery_list")
    private List<DeliveryInfo> deliveryList;
}
