package com.mall4j.cloud.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ErpSkuPriceDTO {
    private String skuCode;

    private String storeCode;
    private Long storeId;

    private Integer marketPrice;

    private Integer price;

    private Integer activityPrice;

    private String priceCode;

    //crm 字段
    @ApiModelProperty(value = "价格类型 1-吊牌价 2-保护价 3-活动价")
    private Integer priceType;

    @ApiModelProperty(value = "商品编码（用来标识商品）", required = true)
    private String productCode;

    /**
     * 仅限内容日志存储使用
     */
    private String remark;
}
