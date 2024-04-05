package com.mall4j.cloud.api.biz.dto.livestore.request.conpon;

import lombok.Data;

@Data
public class DiscountInfo {
    private Long discount_num;
    private Long discount_fee;

    private DiscountCondition discount_condition;
    private BuygetInfo buyget_info;
}
