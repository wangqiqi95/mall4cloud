package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcAfterSaleProductInfo {
    //商品spuid
    private String product_id;
    //商品skuid
    private String sku_id;
    //售后数量
    private Integer count;
}
