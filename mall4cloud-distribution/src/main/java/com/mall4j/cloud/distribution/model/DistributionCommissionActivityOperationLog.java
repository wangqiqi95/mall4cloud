package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 佣金配置-活动佣金-操作日志
 *
 * @author gww
 * @date 2021-12-16 16:04:31
 */
public class DistributionCommissionActivityOperationLog extends BaseModel implements Serializable{
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
     * 操作用户ID
     */
    private Long operationUserId;

    /**
     * 操作用户名称
     */
    private String operationUserName;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 历史导购佣金状态 0-禁用 1-启用
     */
    private Integer hisGuideRateStatus;

    /**
     * 历史导购佣金
     */
    private BigDecimal hisGuideRate;

    /**
     * 历史微客佣金状态 0-禁用 1-启用
     */
    private Integer hisShareRateStatus;

    /**
     * 历史微客佣金
     */
    private BigDecimal hisShareRate;

    /**
     * 历史发展佣金状态 0-禁用 1-启用
     */
    private Integer hisDevelopRateStatus;

    /**
     * 历史发展佣金
     */
    private BigDecimal hisDevelopRate;

    /**
     * 历史活动时间类型 0-长期生效 1-指定时间段生效
     */
    private Integer hisLimitTimeType;

    /**
     * 历史适用门店类型 0-所有门店 1-指定门店 
     */
    private Integer hisLimitStoreType;

    /**
     * 历史适用门店集合(多个逗号隔开)
     */
    private String hisLimitStoreIds;

	/**
	 * 历史适用门店数量(全部不需要处理)
	 */
    private Integer hisLimitStoreCount;

    /**
     * 历史商品范围类型 0-所有商品 1-指定商品 
     */
    private Integer hisLimitSpuType;

    /**
     * 历史商品集合(多个逗号隔开)
     */
    private String hisLimitSpuIds;

	/**
	 * 历史商品集合数量(全部不需要处理)
	 */
	private Integer hisLimitSpuCount;

    /**
     * 历史活动开始时间
     */
    private Date hisStartTime;

    /**
     * 历史活动结束时间
     */
    private Date hisEndTime;

    /**
     * 活动时间类型 0-长期生效 1-指定时间段生效
     */
    private Integer limitTimeType;

    /**
     * 适用门店类型 0-所有门店 1-指定门店 
     */
    private Integer limitStoreType;

    /**
     * 适用门店集合(多个逗号隔开)
     */
    private String limitStoreIds;

	/**
	 * 适用门店集合数量(全部不处理)
	 */
	private Integer limitStoreCount;

    /**
     * 商品范围类型 0-所有商品 1-指定商品 
     */
    private Integer limitSpuType;

    /**
     * 商品集合(多个逗号隔开)
     */
    private String limitSpuIds;

	/**
	 * 商品集合数量(全部不处理)
	 */
    private Integer limitSpuCount;

    /**
     * 导购佣金状态 0-禁用 1-启用
     */
    private Integer guideRateStatus;

    /**
     * 导购佣金
     */
    private Double guideRate;

    /**
     * 微客佣金状态 0-禁用 1-启用
     */
    private Integer shareRateStatus;

    /**
     * 微客佣金
     */
    private Double shareRate;

    /**
     * 发展佣金状态 0-禁用 1-启用
     */
    private Integer developRateStatus;

    /**
     * 发展佣金
     */
    private Double developRate;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

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

	public Long getOperationUserId() {
		return operationUserId;
	}

	public void setOperationUserId(Long operationUserId) {
		this.operationUserId = operationUserId;
	}

	public String getOperationUserName() {
		return operationUserName;
	}

	public void setOperationUserName(String operationUserName) {
		this.operationUserName = operationUserName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getHisGuideRateStatus() {
		return hisGuideRateStatus;
	}

	public void setHisGuideRateStatus(Integer hisGuideRateStatus) {
		this.hisGuideRateStatus = hisGuideRateStatus;
	}

	public BigDecimal getHisGuideRate() {
		return hisGuideRate;
	}

	public void setHisGuideRate(BigDecimal hisGuideRate) {
		this.hisGuideRate = hisGuideRate;
	}

	public BigDecimal getHisShareRate() {
		return hisShareRate;
	}

	public void setHisShareRate(BigDecimal hisShareRate) {
		this.hisShareRate = hisShareRate;
	}

	public BigDecimal getHisDevelopRate() {
		return hisDevelopRate;
	}

	public void setHisDevelopRate(BigDecimal hisDevelopRate) {
		this.hisDevelopRate = hisDevelopRate;
	}

	public Integer getHisShareRateStatus() {
		return hisShareRateStatus;
	}

	public void setHisShareRateStatus(Integer hisShareRateStatus) {
		this.hisShareRateStatus = hisShareRateStatus;
	}


	public Integer getHisDevelopRateStatus() {
		return hisDevelopRateStatus;
	}

	public void setHisDevelopRateStatus(Integer hisDevelopRateStatus) {
		this.hisDevelopRateStatus = hisDevelopRateStatus;
	}


	public Integer getHisLimitTimeType() {
		return hisLimitTimeType;
	}

	public void setHisLimitTimeType(Integer hisLimitTimeType) {
		this.hisLimitTimeType = hisLimitTimeType;
	}

	public Integer getHisLimitStoreType() {
		return hisLimitStoreType;
	}

	public void setHisLimitStoreType(Integer hisLimitStoreType) {
		this.hisLimitStoreType = hisLimitStoreType;
	}

	public String getHisLimitStoreIds() {
		return hisLimitStoreIds;
	}

	public void setHisLimitStoreIds(String hisLimitStoreIds) {
		this.hisLimitStoreIds = hisLimitStoreIds;
	}

	public Integer getHisLimitSpuType() {
		return hisLimitSpuType;
	}

	public void setHisLimitSpuType(Integer hisLimitSpuType) {
		this.hisLimitSpuType = hisLimitSpuType;
	}

	public String getHisLimitSpuIds() {
		return hisLimitSpuIds;
	}

	public void setHisLimitSpuIds(String hisLimitSpuIds) {
		this.hisLimitSpuIds = hisLimitSpuIds;
	}

	public Date getHisStartTime() {
		return hisStartTime;
	}

	public void setHisStartTime(Date hisStartTime) {
		this.hisStartTime = hisStartTime;
	}

	public Date getHisEndTime() {
		return hisEndTime;
	}

	public void setHisEndTime(Date hisEndTime) {
		this.hisEndTime = hisEndTime;
	}

	public Integer getLimitTimeType() {
		return limitTimeType;
	}

	public void setLimitTimeType(Integer limitTimeType) {
		this.limitTimeType = limitTimeType;
	}

	public Integer getLimitStoreType() {
		return limitStoreType;
	}

	public void setLimitStoreType(Integer limitStoreType) {
		this.limitStoreType = limitStoreType;
	}

	public String getLimitStoreIds() {
		return limitStoreIds;
	}

	public void setLimitStoreIds(String limitStoreIds) {
		this.limitStoreIds = limitStoreIds;
	}

	public Integer getLimitSpuType() {
		return limitSpuType;
	}

	public void setLimitSpuType(Integer limitSpuType) {
		this.limitSpuType = limitSpuType;
	}

	public String getLimitSpuIds() {
		return limitSpuIds;
	}

	public void setLimitSpuIds(String limitSpuIds) {
		this.limitSpuIds = limitSpuIds;
	}

	public Integer getGuideRateStatus() {
		return guideRateStatus;
	}

	public void setGuideRateStatus(Integer guideRateStatus) {
		this.guideRateStatus = guideRateStatus;
	}

	public Double getGuideRate() {
		return guideRate;
	}

	public void setGuideRate(Double guideRate) {
		this.guideRate = guideRate;
	}

	public Integer getShareRateStatus() {
		return shareRateStatus;
	}

	public void setShareRateStatus(Integer shareRateStatus) {
		this.shareRateStatus = shareRateStatus;
	}

	public Double getShareRate() {
		return shareRate;
	}

	public void setShareRate(Double shareRate) {
		this.shareRate = shareRate;
	}

	public Integer getDevelopRateStatus() {
		return developRateStatus;
	}

	public void setDevelopRateStatus(Integer developRateStatus) {
		this.developRateStatus = developRateStatus;
	}

	public Double getDevelopRate() {
		return developRate;
	}

	public void setDevelopRate(Double developRate) {
		this.developRate = developRate;
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

	public Integer getHisLimitStoreCount() {
		return hisLimitStoreCount;
	}

	public void setHisLimitStoreCount(Integer hisLimitStoreCount) {
		this.hisLimitStoreCount = hisLimitStoreCount;
	}

	public Integer getHisLimitSpuCount() {
		return hisLimitSpuCount;
	}

	public void setHisLimitSpuCount(Integer hisLimitSpuCount) {
		this.hisLimitSpuCount = hisLimitSpuCount;
	}

	public Integer getLimitStoreCount() {
		return limitStoreCount;
	}

	public void setLimitStoreCount(Integer limitStoreCount) {
		this.limitStoreCount = limitStoreCount;
	}

	public Integer getLimitSpuCount() {
		return limitSpuCount;
	}

	public void setLimitSpuCount(Integer limitSpuCount) {
		this.limitSpuCount = limitSpuCount;
	}

	@Override
	public String toString() {
		return "DistributionCommissionActivityOperationLog{" +
				"id=" + id +
				",activityId=" + activityId +
				",operationUserId=" + operationUserId +
				",operationUserName=" + operationUserName +
				",orgId=" + orgId +
				",hisGuideRateStatus=" + hisGuideRateStatus +
				",hisGuideRate=" + hisGuideRate +
				",hisShareRateStatus=" + hisShareRateStatus +
				",hisShareRate=" + hisShareRate +
				",hisDevelopRateStatus=" + hisDevelopRateStatus +
				",hisDevelopRate=" + hisDevelopRate +
				",hisLimitTimeType=" + hisLimitTimeType +
				",hisLimitStoreType=" + hisLimitStoreType +
				",hisLimitStoreIds=" + hisLimitStoreIds +
				",hisLimitSpuType=" + hisLimitSpuType +
				",hisLimitSpuIds=" + hisLimitSpuIds +
				",hisStartTime=" + hisStartTime +
				",hisEndTime=" + hisEndTime +
				",limitTimeType=" + limitTimeType +
				",limitStoreType=" + limitStoreType +
				",limitStoreIds=" + limitStoreIds +
				",limitSpuType=" + limitSpuType +
				",limitSpuIds=" + limitSpuIds +
				",guideRateStatus=" + guideRateStatus +
				",guideRate=" + guideRate +
				",shareRateStatus=" + shareRateStatus +
				",shareRate=" + shareRate +
				",developRateStatus=" + developRateStatus +
				",developRate=" + developRate +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
