package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class BuygetInfo {
    private String buy_out_product_id;
    private Integer buy_product_cnt;
    private String get_out_product_id;
    private Integer get_product_cnt;

}
