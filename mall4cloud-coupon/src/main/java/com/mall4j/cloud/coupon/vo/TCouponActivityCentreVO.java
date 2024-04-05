package com.mall4j.cloud.coupon.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 优惠券关联活动VO
 *
 * @author gmq
 * @date 2022-10-17 11:20:21
 */
public class TCouponActivityCentreVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("优惠券id")
    private Long couponId;

    @ApiModelProperty("活动id")
    private Long activityId;

    @ApiModelProperty("活动类型：0虚拟门店价")
    private Integer activitySource;

    @ApiModelProperty("")
    private String createBy;

    @ApiModelProperty("")
    private String updateBy;

    @ApiModelProperty("0正常 1删除")
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
		return "TCouponActivityCentreVO{" +
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
