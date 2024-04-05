package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SkuPriceDTO {
    private Long skuId;
    private Long priceFee;
    private Long marketPriceFee;
    private Long spuId;

    private String priceCode;
    private String channelName;
    @ApiModelProperty("门店库存(用于取价判断)")
    private Integer storeSkuStock;
    @ApiModelProperty("门店保护价(用于取价判断)")
    private Long storeProtectPrice;
    @ApiModelProperty("官店保护价(用于取价判断)")
    private Long skuProtectPrice;
}
