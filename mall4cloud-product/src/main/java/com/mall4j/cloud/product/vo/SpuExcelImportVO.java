package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.util.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 活动sku导入返回参数
 * @luzhengxiang
 * @create 2022-03-17 4:09 PM
 **/
@Data
public class SpuExcelImportVO {

    /**
     * 序号用于返回结果排序，必填
     */
    private Integer seq;

    @ApiModelProperty(value = "商品id")
    private Long spuId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品编码")
    private String spuCode;

    @ApiModelProperty("商品介绍主图")
    private String mainImgUrl;

    @ApiModelProperty(value = "商品售价")
    private Long priceFee;

    @ApiModelProperty("商品调价")
    private BigDecimal discountPrice;

    @ApiModelProperty("参与方式 0 按货号 1按条码 2按skuCode")
    private Integer participationMode;

    @ApiModelProperty("skus")
    private List<SkuExcelImportVO> skus;

}
