package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 限时调价活动 sku价格DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
@Data
public class TimeLimitedDiscountSkuDTO{
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Integer activityId;

    @ApiModelProperty("")
    private Long spuId;

    @ApiModelProperty("")
    private Long skuId;

    @ApiModelProperty("售价，整数方式保存")
    private Long price;

}
