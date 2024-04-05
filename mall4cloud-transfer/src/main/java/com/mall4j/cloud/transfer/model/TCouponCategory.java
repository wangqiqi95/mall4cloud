package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:38
 */
public class TCouponCategory extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 分类
     */
    private String category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "TCouponCategory{" +
				"id=" + id +
				",couponId=" + couponId +
				",category=" + category +
				'}';
	}
}
