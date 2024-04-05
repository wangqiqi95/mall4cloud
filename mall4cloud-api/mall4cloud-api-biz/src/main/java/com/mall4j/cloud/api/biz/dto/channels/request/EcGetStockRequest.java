package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

@Data
public class EcGetStockRequest {
    //内部商品ID
    private String product_id;
    //内部sku_id
    private String sku_id;
}
