package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 活动优惠券详情信息
 * @author shijing
 * @date 2022-01-12
 */
@Data
@ApiModel(description = "导购端送券中心详情")
public class CouponForShoppersVO {

    @ApiModelProperty(value = "活动-优惠券id")
    private Long id;

    @ApiModelProperty(value = "卡券id")
    private Long couponId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "优惠券类型（0：抵用券/1：折扣券）")
    private Short type;

    @ApiModelProperty(value = "抵用金额")
    private Long reduceAmount;

    @ApiModelProperty(value = "折扣力度")
    private BigDecimal couponDiscount;

    @ApiModelProperty(value = "优惠券封面")
    private String coverUrl;

    @ApiModelProperty(value = "优惠券描述")
    private String description;

}
