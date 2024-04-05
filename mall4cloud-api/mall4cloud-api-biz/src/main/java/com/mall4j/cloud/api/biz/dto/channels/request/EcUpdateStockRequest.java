package com.mall4j.cloud.api.biz.dto.channels.request;

import lombok.Data;

@Data
public class EcUpdateStockRequest {
    //内部商品ID
    private String product_id;
    //内部sku_id
    private String sku_id;
    //修改类型。1: 增加；2:减少；3:设置。
    private Integer diff_type;
    //增加、减少或者设置的库存值。
    private Integer num;
}
