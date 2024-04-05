package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AfterSaleProductInfo {
    /**
     * 外部商品ID
     */
    @JsonProperty("out_product_id")
    private String outProductId;

    /**
     * 微信侧商品ID
     */
    @JsonProperty("product_id")
    private Long productId;

    /**
     * 外部sku ID
     */
    @JsonProperty("out_sku_id")
    private String outSkuId;

    /**
     * 微信侧sku ID
     */
    @JsonProperty("sku_id")
    private Long skuId;
    /**
     * 商品数量
     */
    @JsonProperty("product_cnt")
    private Integer productCnt;
}
