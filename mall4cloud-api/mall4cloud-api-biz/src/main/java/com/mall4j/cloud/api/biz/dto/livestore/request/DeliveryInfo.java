
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryInfo {

    /**
     * 快递公司ID，通过获取快递公司列表获取，将影响物流信息查询
     */
    @JsonProperty("delivery_id")
    private String deliveryId;

    /**
     * 物流单对应的商品信息
     */
    @JsonProperty("product_info_list")
    private List<ProductInfo> productInfoList;

    /**
     * 快递单号
     */
    @JsonProperty("waybill_id")
    private String waybillId;

}
