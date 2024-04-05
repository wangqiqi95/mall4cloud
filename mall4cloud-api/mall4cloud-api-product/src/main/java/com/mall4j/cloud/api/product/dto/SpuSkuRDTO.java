package com.mall4j.cloud.api.product.dto;

import lombok.Data;

import java.util.List;

/**
 * @author gmq
 * @date 2020-10-28 15:27:24
 */
@Data
public class SpuSkuRDTO {
    private static final long serialVersionUID = 1L;

    private List<Long> spuIds;

    private String channel;

    public SpuSkuRDTO(){

    }

    public SpuSkuRDTO(String channel,List<Long> spuIds){
        this.channel=channel;
        this.spuIds=spuIds;
    }
}
