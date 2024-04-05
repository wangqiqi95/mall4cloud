package com.mall4j.cloud.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SpuPageVO {
    @ApiModelProperty(value = "商品id")
    private Long spuId;

    @ApiModelProperty(value = "商品名称")
    private String spuName;

    @ApiModelProperty(value = "商品编码")
    private String spuCode;

    @ApiModelProperty("商品介绍主图")
    private String mainImgUrl;

    @ApiModelProperty(value = "商品售价")
    private Long priceFee;

    @ApiModelProperty(value = "市场价，整数方式保存")
    private Long marketPriceFee;

    @ApiModelProperty(value = "销量")
    private Integer saleNum;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty("积分价格")
    private Long scoreFee;

    @ApiModelProperty("erpCategoryName")
    private String erpCategoryName;

    @ApiModelProperty("店铺分类")
    private String shopCategory;

    @ApiModelProperty("平台分类")
    private String platformCategory;

    @ApiModelProperty("商品状态")
    private Integer status;

    @ApiModelProperty("是否铺货: 0否 1是")
    private Integer iphStatus;

    private Long categoryId;
    private Long shopCategoryId;
    private String deliveryMode;
    private Long deliveryTemplateId;
    private String sellingPoint;
    private Date updateTime;
    private Date createTime;

    private Integer seq;

}
