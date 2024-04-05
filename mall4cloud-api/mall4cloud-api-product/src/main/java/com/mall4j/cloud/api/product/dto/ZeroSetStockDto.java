package com.mall4j.cloud.api.product.dto;

import lombok.Data;

import java.util.List;

/**
 * 库存置零DTO
 * @date 2023/3/13
 */
@Data
public class ZeroSetStockDto {

    private Long spuId;

    private List<Long> skuId;
}
