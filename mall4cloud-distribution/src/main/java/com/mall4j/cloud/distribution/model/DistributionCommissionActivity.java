package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 佣金配置-活动佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public class DistributionCommissionActivity extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 组织ID
     */
    private Long orgId;

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
     * 活动状态 0-失效 1-生效
     */
    private Integer status;

	/**
	 * 活动优先级
	 */
	private Integer priority;

    /**
     * 活动时间类型 0-长期生效 1-指定时间段生效
     */
    private Integer limitTimeType;

    /**
     * 适用门店类型 0-所有门店 1-指定门店 
     */
    private Integer limitStoreType;

    /**
     * 商品范围类型 0-所有商品 1-指定商品 
     */
    private Integer limitSpuType;

	/**
	 * 订单类型 0-普通订单 1-代购订单
	 */
	private Integer limitOrderType;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 备注
     */
    private String remark;

	/**
	 * 适用门店数量
	 */
	private Integer limitStoreNum = 0;

	/**
	 * 适用商品数量
	 */
    private Integer limitSpuNum = 0;

	/**
	 * 是否删除 0-否 1-是
	 */
	private Integer deleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getLimitSpuType() {
		return limitSpuType;
	}

	public void setLimitSpuType(Integer limitSpuType) {
		this.limitSpuType = limitSpuType;
	}

	public Integer getLimitOrderType() {
		return limitOrderType;
	}

	public void setLimitOrderType(Integer limitOrderType) {
		this.limitOrderType = limitOrderType;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getLimitStoreNum() {
		return limitStoreNum;
	}

	public void setLimitStoreNum(Integer limitStoreNum) {
		this.limitStoreNum = limitStoreNum;
	}

	public Integer getLimitSpuNum() {
		return limitSpuNum;
	}

	public void setLimitSpuNum(Integer limitSpuNum) {
		this.limitSpuNum = limitSpuNum;
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

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "DistributionCommissionActivity{" +
				"id=" + id +
				",activityName=" + activityName +
				",orgId=" + orgId +
				",guideRateStatus=" + guideRateStatus +
				",guideRate=" + guideRate +
				",shareRateStatus=" + shareRateStatus +
				",shareRate=" + shareRate +
				",status=" + status +
				",limitTimeType=" + limitTimeType +
				",limitStoreType=" + limitStoreType +
				",limitSpuType=" + limitSpuType +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",remark=" + remark +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
