
package com.mall4j.cloud.api.biz.dto.livestore.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Skus {

    /**
     * 商家自定义skuID
     */
    @JsonProperty("out_sku_id")
    private String outSkuId;
    /**
     * 交易组件平台自定义skuID
     */
    @JsonProperty("sku_id")
    private Long skuId;

    /**
     * 库存
     */
    @JsonProperty("stock_num")
    private Integer stockNum;

}
