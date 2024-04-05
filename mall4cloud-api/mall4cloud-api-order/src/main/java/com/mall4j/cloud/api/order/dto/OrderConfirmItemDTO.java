package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangjie
 */
@Data
public class OrderConfirmItemDTO {


    @ApiModelProperty("产品ID")
    private Long spuId;

    @ApiModelProperty("SkuID")
    private Long skuId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("中台分类")
    private String category;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("商品数量")
    private Integer count;

    @ApiModelProperty("售价，加入购物车时的商品价格")
    private Long priceFee;

    @ApiModelProperty("吊牌价")
    private Long marketPrice;

    @ApiModelProperty("当前总价格(商品价格 * 数量)")
    private Long totalPriceFee;

    @ApiModelProperty("实际金额(商品价格 * 数量)")
    private Long actualTotal;

    @ApiModelProperty("当前总优惠")
    private Long totalReducePrice;
}
