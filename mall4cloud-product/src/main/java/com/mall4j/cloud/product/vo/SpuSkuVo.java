package com.mall4j.cloud.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Date 2022年5月31日, 0031 10:37
 * @Created by eury
 */
@Data
public class SpuSkuVo {

    private Long spuId;

    private String spuCode;

    @ApiModelProperty(value = "商品渠道（R线下渠道 T电商渠道 L清货渠道）")
    private String channelName;

    @ApiModelProperty(value = "折扣等级（错误数据或者-1,默认为0）")
    private String channelDiscount;

    @ApiModelProperty("市场价，整数方式保存")
    private Long marketPriceFee;

    private Long skuMarketPriceFee;

    private Long priceFee;

    private String priceCode;

}
