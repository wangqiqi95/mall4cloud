package com.mall4j.cloud.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Administrator
 * @Description:
 * @Date: 2022-03-15 11:38
 * @Version: 1.0
 */
@Data
public class TagActRelationProdVO {
    @ApiModelProperty("活动id")
    private Long actId;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("货号")
    private String spuCode;

    @ApiModelProperty("售价")
    private Long priceFee;
}
