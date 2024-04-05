package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 限时调价活动 商铺DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:14
 */
public class TimeLimitedDiscountShopDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("")
    private Integer activityId;

    @ApiModelProperty("")
    private Long shopId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Override
	public String toString() {
		return "TimeLimitedDiscountShopDTO{" +
				"id=" + id +
				",activityId=" + activityId +
				",shopId=" + shopId +
				'}';
	}
}
