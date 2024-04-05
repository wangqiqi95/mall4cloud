
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductInfo {

    /**
     * 商家自定义商品ID
     */
    @JsonProperty("out_product_id")
    private String outProductId;
    /**
     * 商家自定义sku ID, 如果没有则不填
     */
    @JsonProperty("out_sku_id")
    private String outSkuId;
    /**
     * 参与售后的商品数量
     */
    @JsonProperty("product_cnt")
    private Long productCnt;

    /**
     * 完成发货时间，finish_all_delivery = 1 必传
     */
    @JsonProperty("ship_done_time")
    private String shipDoneTime;

}
