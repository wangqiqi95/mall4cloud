package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 佣金配置-活动佣金-商品
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public class DistributionCommissionActivitySpu extends BaseModel implements Serializable{
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
     * 商品ID
     */
    private Long spuId;

    /**
     * 导购佣金状态 0-禁用 1-启用
     */
    private Integer guideRateStatus;

    /**
     * 导购佣金
     */
    private BigDecimal guideRate;

    /**
     * 微客佣金状态 0-禁用 1-启用
     */
    private Integer shareRateStatus;

    /**
     * 微客佣金
     */
    private BigDecimal shareRate;

	/**
	 * 发展佣金状态 0-禁用 1-启用
	 */
	private Integer developRateStatus;

	/**
	 * 发展佣金
	 */
	private BigDecimal developRate;

	/**
	 * 活动时间类型 0-长期生效 1-指定时间段生效
	 */
	private Integer limitTimeType;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

	/**
	 * 活动优先级
	 */
	private Integer priority;


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

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getGuideRateStatus() {
		return guideRateStatus;
	}

	public void setGuideRateStatus(Integer guideRateStatus) {
		this.guideRateStatus = guideRateStatus;
	}

	public BigDecimal getGuideRate() {
		return guideRate;
	}

	public void setGuideRate(BigDecimal guideRate) {
		this.guideRate = guideRate;
	}

	public Integer getShareRateStatus() {
		return shareRateStatus;
	}

	public void setShareRateStatus(Integer shareRateStatus) {
		this.shareRateStatus = shareRateStatus;
	}

	public BigDecimal getShareRate() {
		return shareRate;
	}

	public void setShareRate(BigDecimal shareRate) {
		this.shareRate = shareRate;
	}

	public Integer getLimitTimeType() {
		return limitTimeType;
	}

	public void setLimitTimeType(Integer limitTimeType) {
		this.limitTimeType = limitTimeType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getDevelopRateStatus() {
		return developRateStatus;
	}

	public void setDevelopRateStatus(Integer developRateStatus) {
		this.developRateStatus = developRateStatus;
	}

	public BigDecimal getDevelopRate() {
		return developRate;
	}

	public void setDevelopRate(BigDecimal developRate) {
		this.developRate = developRate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "DistributionCommissionActivitySpu{" +
				"id=" + id +
				",activityId=" + activityId +
				",orgId=" + orgId +
				",spuId=" + spuId +
				",guideRateStatus=" + guideRateStatus +
				",guideRate=" + guideRate +
				",shareRateStatus=" + shareRateStatus +
				",shareRate=" + shareRate +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
