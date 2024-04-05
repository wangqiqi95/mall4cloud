package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 优惠券关联活动DTO
 */
public class TCouponActivityCentreDTO{
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("关键字搜索")
    private String name;

	@NotNull
    @ApiModelProperty(value = "活动id",required = true)
    private Long activityId;

}
