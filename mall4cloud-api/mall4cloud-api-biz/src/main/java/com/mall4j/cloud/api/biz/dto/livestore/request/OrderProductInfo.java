package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderProductInfo {

    /**
     * 外部商品spuid
     */
    @JsonProperty("out_product_id")
    private String outProductId;
    /**
     * 外部商品skuid
     */
    @JsonProperty("out_sku_id")
    private String outSkuId;

    /**
     * 商品个数
     */
    @JsonProperty("product_cnt")
    private Integer product_cnt;

    /**
     * 生成订单时商品的售卖价（单位：分），可以跟上传商品接口的价格不一致
     */
    @JsonProperty("sale_price")
    private Long salePrice;

    /**
     * sku总实付价
     */
    @JsonProperty("sku_real_price")
    private Long skuRealPrice;

    /**
     * 生成订单时商品的标题
     */
    @JsonProperty("title")
    private String title;
    /**
     * 生成订单时商品的头图
     */
    @JsonProperty("head_img")
    private String headImg;
    /**
     * 绑定的小程序商品路径
     */
    @JsonProperty("path")
    private String path;

}
