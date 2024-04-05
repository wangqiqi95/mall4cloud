package com.mall4j.cloud.discount.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 满减满折活动 商铺
 *
 * @author FrozenWatermelon
 * @date 2022-03-13 14:58:46
 */
public class DiscountShop extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer activityId;

    /**
     * 
     */
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
		return "DiscountShop{" +
				"id=" + id +
				",activityId=" + activityId +
				",shopId=" + shopId +
				'}';
	}
}
