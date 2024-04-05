package com.mall4j.cloud.common.order.dto;

import lombok.Data;

@Data
public class GiftProdDTO {
    private Long spuId;
    private Long skuId;
    private Integer count;
    private Long giftActivityId;
    private String skuName;
}
