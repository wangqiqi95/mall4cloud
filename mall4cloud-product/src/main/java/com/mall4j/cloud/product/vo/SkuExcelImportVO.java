package com.mall4j.cloud.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @luzhengxiang
 * @create 2022-03-17 4:19 PM
 **/
@Data
public class SkuExcelImportVO {
    @ApiModelProperty(value = "商品id")
    private Long skuId;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "商品编码")
    private String skuCode;

    @ApiModelProperty(value = "商品款色")
    private String priceCode;

    @ApiModelProperty("市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty("商品调价")
    private BigDecimal discountPrice;
}
