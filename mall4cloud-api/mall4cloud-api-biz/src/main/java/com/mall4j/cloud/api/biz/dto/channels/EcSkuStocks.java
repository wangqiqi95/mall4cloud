package com.mall4j.cloud.api.biz.dto.channels;

import lombok.Data;

import java.util.List;

@Data
public class EcSkuStocks {
    //通用库存数量
    private Integer normal_stock_num;

    //限时抢购库存数量
    private Integer limited_discount_stock_num;

    //库存总量：通用库存数量 + 限时抢购库存数量 + 区域库存总量
    private Integer total_stock_num;

    private List<EcWarehouseStocks>  warehouse_stocks;

}
