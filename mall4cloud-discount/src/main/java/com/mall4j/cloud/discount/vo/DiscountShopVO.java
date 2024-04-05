package com.mall4j.cloud.discount.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 满减满折活动 商铺VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-13 14:58:46
 */
@Data
public class DiscountShopVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("活动id")
    private Integer activityId;

    @ApiModelProperty("店铺id")
    private Long shopId;

}
