package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 分销推广-推荐商品
 *
 * @author gww
 * @date 2021-12-24 16:01:22
 */
public class DistributionRecommendSpu extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

	/**
	 * 商品ID
	 */
	private Long spuId;

    /**
     * 有效期-开始时间
     */
    private Date startTime;

    /**
     * 有效期-结束时间
     */
    private Date endTime;

    /**
     * 适用门店类型 0-所有门店 1-指定门店
     */
    private Integer limitStoreType;

    /**
     * 适用门店集合(逗号隔开)
     */
    private String limitStoreIds;

    /**
     * 状态 1启用 0禁用
     */
    private Integer status;

    /**
     * 权重值
     */
    private Integer sort;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "DistributionRecommendSpu{" +
				"id=" + id +
				",spuId=" + spuId +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",limitStoreType=" + limitStoreType +
				",limitStoreIds=" + limitStoreIds +
				",status=" + status +
				",sort=" + sort +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
