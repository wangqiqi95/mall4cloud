package com.mall4j.cloud.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO
 *
 * @author gmq
 * @date 2022-09-20 10:58:25
 */
@Data
public class ESkuPriceLogDTO {
    private static final long serialVersionUID = 1L;

    private List<SkuPriceLogDTO> skuPriceLogDTOS=new ArrayList<>();

    private String fromRemarks;

    public ESkuPriceLogDTO(){}

    public ESkuPriceLogDTO(List<SkuPriceLogDTO> skuPriceLogDTOS,String fromRemarks){
        this.fromRemarks=fromRemarks;
        this.skuPriceLogDTOS=skuPriceLogDTOS;
    }

}
