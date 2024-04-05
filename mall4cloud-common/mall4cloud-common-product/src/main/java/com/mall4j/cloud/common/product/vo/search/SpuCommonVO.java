package com.mall4j.cloud.common.product.vo.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpuCommonVO implements Serializable {

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
    private Long saleNum;


    /**
     * 状态 1:enable, 0:disable, -1:deleted
     */
    @ApiModelProperty(value = "状态 1:enable, 0:disable, -1:deleted")
    private Integer status;

    @ApiModelProperty(value = "是否会员日")
    private Boolean memberPriceFlag=Boolean.FALSE;

}
