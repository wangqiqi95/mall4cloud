package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcFreightProductInfo {
    //商品id
    private Long product_id;
    //sku_id
    private Long sku_id;
    //商品数量
    private Integer product_cnt;
}
