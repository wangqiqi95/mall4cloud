package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 限时调价活动 spu价格DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
@Data
public class TimeLimitedDiscountSpuDTO{
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Integer activityId;

    @ApiModelProperty("")
    private Integer spuId;

    @ApiModelProperty("参与方式 0 按货号 1按条码 2按skuCode")
    private Integer participationMode;

    @ApiModelProperty("售价，整数方式保存")
    private Long price;

    @ApiModelProperty("sku优惠列表")
    private List<TimeLimitedDiscountSkuDTO> skus;

}
