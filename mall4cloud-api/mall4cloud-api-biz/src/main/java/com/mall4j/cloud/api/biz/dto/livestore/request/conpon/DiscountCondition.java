package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class DiscountCondition {
    private Integer product_cnt;
    private Long product_price;

    private String[] out_product_ids;
    private TradeinInfo tradein_info;
}
