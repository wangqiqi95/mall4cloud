package com.mall4j.cloud.api.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 领取优惠券DTO
 *
 * @author shijing
 */
@Data
public class ReceiveCouponDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("活动来源")
    private Integer activitySource;

}
