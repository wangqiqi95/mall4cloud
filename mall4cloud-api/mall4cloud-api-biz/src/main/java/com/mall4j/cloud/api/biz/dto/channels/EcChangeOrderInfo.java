package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

@Data
public class EcChangeOrderInfo {
    //商品id
    private String product_id;
    //商品sku
    private String sku_id;
    //订单中该商品修改后的总价，以分为单位
    private Long change_price;
}
