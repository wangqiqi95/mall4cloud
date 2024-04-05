package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 电子价签商品DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:24:29
 */
@Data
public class ElPriceProdDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("商品skuid")
    private Long skuId;

}
