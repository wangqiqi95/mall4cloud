package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 佣金配置-活动佣金-门店
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public class DistributionCommissionActivityStore extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 门店ID
     */
    private Long storeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Override
	public String toString() {
		return "DistributionCommissionActivityStore{" +
				"id=" + id +
				",activityId=" + activityId +
				",orgId=" + orgId +
				",storeId=" + storeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
