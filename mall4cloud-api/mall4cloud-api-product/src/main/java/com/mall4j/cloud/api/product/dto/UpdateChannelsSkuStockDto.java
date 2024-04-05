package com.mall4j.cloud.api.product.dto;

import lombok.Data;

/**
 * @date 2023/3/16
 */
@Data
public class UpdateChannelsSkuStockDto {
    private Long skuId;

    private Integer stock;

    /**
     * type == 3 属于设置，需要单独处理
     */
    private Integer type;

    public UpdateChannelsSkuStockDto(Long skuId, Integer stock, Integer type) {
        this.skuId = skuId;
        this.stock = stock;
        this.type = type;
    }

    public UpdateChannelsSkuStockDto() {
    }
}
