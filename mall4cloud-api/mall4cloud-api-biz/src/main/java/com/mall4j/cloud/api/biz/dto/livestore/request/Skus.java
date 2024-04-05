
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Skus {

    /**
     * 条形码
     */
    private String barcode;
    /**
     * 市场价格,以分为单位
     */
    @JsonProperty("market_price")
    private Long marketPrice;
    /**
     * 商家自定义商品ID
     */
    @JsonProperty("out_product_id")
    private String outProductId;
    /**
     * 商家自定义skuID
     */
    @JsonProperty("out_sku_id")
    private String outSkuId;
    /**
     * 售卖价格,以分为单位
     */
    @JsonProperty("sale_price")
    private Long salePrice;

    @JsonProperty("sku_attrs")
    private List<SkuAttr> skuAttrs;
    /**
     * 商品编码
     */
    @JsonProperty("sku_code")
    private String skuCode;
    /**
     * 库存
     */
    @JsonProperty("stock_num")
    private Long stockNum;
    /**
     * sku小图
     */
    @JsonProperty("thumb_img")
    private String thumbImg;

}
