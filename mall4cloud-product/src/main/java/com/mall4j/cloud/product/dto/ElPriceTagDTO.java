package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 电子价签管理DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:23:15
 */
@Data
public class ElPriceTagDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "名称",required = true)
    private String name;

	@ApiModelProperty(value = "商品集合",required = true)
	private List<ElPriceProdDTO> prodList;

}
