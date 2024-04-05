package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * DTO
 *
 * @author FrozenWatermelon
 * @date 2020-11-11 13:49:06
 */
@Data
public class SpuExtensionStockDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("实际库存")
    private Integer actualStock;

    @ApiModelProperty("可售卖库存")
    private Integer stock;

}
