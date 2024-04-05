package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 限时调价活动 spu价格VO
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 13:29:18
 */
@Data
public class TimeLimitedDiscountSpuVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("")
    private Integer activityId;

    @ApiModelProperty("")
    private Integer spuId;

    @ApiModelProperty("参与方式 0 按货号 1按条码 2按skuCode")
    private Integer participationMode;

    @ApiModelProperty("售价，整数方式保存")
    private Long price;

    @ApiModelProperty("sku优惠列表")
    private List<TimeLimitedDiscountSkuVO> skus;

}
