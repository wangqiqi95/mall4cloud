package com.mall4j.cloud.coupon.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 优惠券关联活动
 *
 * @author gmq
 * @date 2022-10-17 11:20:21
 */
public class TCouponActivityCentre extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
	@TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动类型：0虚拟门店价
     */
    private Integer activitySource;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private String updateBy;

    /**
     * 0正常 1删除
     */
    private Integer delFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Integer getActivitySource() {
		return activitySource;
	}

	public void setActivitySource(Integer activitySource) {
		this.activitySource = activitySource;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "TCouponActivityCentre{" +
				"id=" + id +
				",couponId=" + couponId +
				",activityId=" + activityId +
				",activitySource=" + activitySource +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
