package com.mall4j.cloud.coupon.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 优惠券用户关联信息VO
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public class CouponUserVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券用户ID")
    private Long couponUserId;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("领券时间")
    private Date receiveTime;

    @ApiModelProperty("开始时间")
    private Date userStartTime;

    @ApiModelProperty("结束时间")
    private Date userEndTime;

    @ApiModelProperty("优惠券状态 0:失效 1:有效 2:使用过")
    private Integer status;

    @ApiModelProperty("优惠券是否被删除 0:未删除 1:已删除")
    private Integer isDelete;

	@ApiModelProperty("优惠券信息")
	private CouponVO couponVO;

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

	public CouponVO getCouponVO() {
		return couponVO;
	}

	public void setCouponVO(CouponVO couponVO) {
		this.couponVO = couponVO;
	}

	@Override
	public String toString() {
		return "CouponUserVO{" +
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
				",couponVO=" + couponVO +
				'}';
	}
}
