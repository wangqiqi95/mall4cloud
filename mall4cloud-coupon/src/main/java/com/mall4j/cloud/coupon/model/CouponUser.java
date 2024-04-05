package com.mall4j.cloud.coupon.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 优惠券用户关联信息
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public class CouponUser extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券用户ID
     */
    private Long couponUserId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 领券时间
     */
    private Date receiveTime;

    /**
     * 开始时间
     */
    private Date userStartTime;

    /**
     * 结束时间
     */
    private Date userEndTime;

    /**
     * 优惠券状态 0:失效 1:有效 2:使用过
     */
    private Integer status;

    /**
     * 优惠券是否被删除 0:未删除 1:已删除
     */
    private Integer isDelete;

	public Long getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(Long couponUserId) {
		this.couponUserId = couponUserId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Date getUserStartTime() {
		return userStartTime;
	}

	public void setUserStartTime(Date userStartTime) {
		this.userStartTime = userStartTime;
	}

	public Date getUserEndTime() {
		return userEndTime;
	}

	public void setUserEndTime(Date userEndTime) {
		this.userEndTime = userEndTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "CouponUser{" +
				"couponUserId=" + couponUserId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",couponId=" + couponId +
				",userId=" + userId +
				",receiveTime=" + receiveTime +
				",userStartTime=" + userStartTime +
				",userEndTime=" + userEndTime +
				",status=" + status +
				",isDelete=" + isDelete +
				'}';
	}
}
