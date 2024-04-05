package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 领取优惠券DTO
 *
 * @author shijing
 */
@Data
public class BatchReceiveCouponDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券ID")
    private List<Long> couponIds;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("活动来源")
    private Integer activitySource;

    @ApiModelProperty("某次领取批次号")
    private Long batchId;

}
