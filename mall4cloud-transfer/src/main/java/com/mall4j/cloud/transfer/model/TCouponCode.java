package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 优惠券码关联表
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:38
 */
public class TCouponCode extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 关联优惠券id
     */
    private Long couponId;

    /**
     * 券码
     */
    private String couponCode;

    /**
     * 状态（0：未核销/1：已核销）
     */
    private Integer status;

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

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TCouponCode{" +
				"id=" + id +
				",couponId=" + couponId +
				",couponCode=" + couponCode +
				",status=" + status +
				'}';
	}
}
