package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分享推广-分享记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public class DistributionShareRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 分享人ID
     */
    private Long shareId;

    /**
     * 类型 1 导购 2威客 3会员
     */
    private Integer shareType;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动类型 1海报 2专题 3朋友圈 4商品
     */
    private Integer activityType;

	/**
	 * 活动/商品名称
	 */
	private String activityName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
	}

	public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Override
	public String toString() {
		return "DistributionShareRecord{" +
				"id=" + id +
				", shareId=" + shareId +
				", shareType=" + shareType +
				", activityId=" + activityId +
				", activityType=" + activityType +
				", activityName='" + activityName + '\'' +
				'}';
	}
}
