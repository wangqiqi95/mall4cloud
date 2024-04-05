package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.common.util.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 活动sku导入参数对象
 *
 * @luzhengxiang
 * @create 2022-03-16 11:55 PM
 **/
@Data
public class ActivitySpuSkuExportDTO {

    @Excel(name = "序号")
    private Integer seq;

    @Excel(name = "商品名称")
    @ApiModelProperty("商品id")
    private String skuName;

    @Excel(name = "商品货号")
    @ApiModelProperty("商品货号")
    private String spuBarcode;

    @Excel(name = "skuCode")
    @ApiModelProperty("skuCode")
    private String priceCode;

    @Excel(name = "商品条形码")
    @ApiModelProperty("商品条形码")
    private String skuBarcode;

    @Excel(name = "商品原价")
    @ApiModelProperty("商品原价")
    private BigDecimal price;

    @Excel(name = "商品调价")
    @ApiModelProperty("商品调价")
    private BigDecimal discountPrice;

}
