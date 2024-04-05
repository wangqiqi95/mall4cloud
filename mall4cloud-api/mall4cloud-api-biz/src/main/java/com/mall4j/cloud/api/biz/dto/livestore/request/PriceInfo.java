package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PriceInfo {
    /**
     * 运费
     */
    @JsonProperty("freight")
    private Long freight;

    /**
     * 折扣费用
     */
    @JsonProperty("discounted_price")
    private Long discountedPrice;

    /**
     * 其他费用
     */
    @JsonProperty("additional_price")
    private Long additionalPrice;

    /**
     * 其他费用说明
     */
    @JsonProperty("additional_remarks")
    private String additionalRemarks;

    /**
     * 订单总价
     */
    @JsonProperty("order_price")
    private Long orderPrice;


}
