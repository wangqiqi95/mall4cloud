package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.common.util.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠券sku导入参数对象
 *
 * @luzhengxiang
 * @create 2022-03-16 11:55 PM
 **/
@Data
public class CouponSkuImportDTO {

    @Excel(name = "排序值")
    private Integer seq;

    @Excel(name = "商品名称")
    private String skuName;

    @Excel(name = "商品SKUcode")
    @ApiModelProperty("商品SKUcode")
    private String priceCode;

//    @Excel(name = "商品原价")
    @ApiModelProperty("商品原价")
    private BigDecimal price;

}
