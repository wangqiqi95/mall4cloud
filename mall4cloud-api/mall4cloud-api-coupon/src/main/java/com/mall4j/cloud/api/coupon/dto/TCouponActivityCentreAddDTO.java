package com.mall4j.cloud.api.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 优惠券关联活动DTO
 */
@Data
public class TCouponActivityCentreAddDTO {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("活动id")
	private Long activityId;

	@ApiModelProperty("活动类型：1-限时调价 2-会员日活动调价 3-虚拟门店价")
	private Integer activitySource;

	@ApiModelProperty("配置优惠券")
	private List<Long> couponIds;

	public TCouponActivityCentreAddDTO(){}

	public TCouponActivityCentreAddDTO(Long activityId,Integer activitySource,List<Long> couponIds){
		this.activityId=activityId;
		this.activitySource=activitySource;
		this.couponIds=couponIds;
	}

}
