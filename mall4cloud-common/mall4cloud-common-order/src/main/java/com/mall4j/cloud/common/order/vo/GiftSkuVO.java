package com.mall4j.cloud.common.order.vo;

import lombok.Data;

import java.util.List;

@Data
public class GiftSkuVO {
    private Long skuId;

    private String skuName;

    private Integer status;

    private String properties;

}
