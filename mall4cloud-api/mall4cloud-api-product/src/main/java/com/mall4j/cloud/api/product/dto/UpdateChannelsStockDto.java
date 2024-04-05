package com.mall4j.cloud.api.product.dto;

import lombok.Data;

import java.util.List;

/**
 * @date 2023/3/16
 */
@Data
public class UpdateChannelsStockDto {

    private Long SpuId;

    private List<UpdateChannelsSkuStockDto> skuStockDtos;

    public UpdateChannelsStockDto(Long spuId, List<UpdateChannelsSkuStockDto> skuStockDtos) {
        SpuId = spuId;
        this.skuStockDtos = skuStockDtos;
    }

    public UpdateChannelsStockDto() {
    }
}
