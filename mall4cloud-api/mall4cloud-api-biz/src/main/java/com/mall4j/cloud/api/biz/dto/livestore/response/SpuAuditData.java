
package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SpuAuditData {

    @JsonProperty("create_time")
    private String createTime;
    /**
     * 商家自定义商品ID
     */
    @JsonProperty("out_product_id")
    private String outProductId;
    /**
     * 交易组件平台内部商品ID
     */
    @JsonProperty("product_id")
    private Long productId;
    /**
     * sku数组
     */
    private List<Skus> skus;

}
