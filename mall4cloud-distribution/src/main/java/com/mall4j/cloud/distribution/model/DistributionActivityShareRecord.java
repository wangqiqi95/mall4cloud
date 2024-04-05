package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分销推广-活动分享效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public class DistributionActivityShareRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

	/**
	 * spuId
	 */
	private String spuCode;

	/**
	 * spuId
	 */
	private String spuName;

    /**
     * 活动类型 1海报 2专题 3朋友圈 4商品
     */
    private Integer activityType;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 分享次数
     */
    private Integer shareNum;

    /**
     * 浏览次数
     */
    private Integer browseNum;

    /**
     * 浏览人次
     */
    private Integer browseUserNum;

    /**
     * 加购次数
     */
    private Integer purchaseNum;

    /**
     * 加购人次
     */
    private Integer purchaseUserNum;

    /**
     * 下单次数
     */
    private Integer buyNum;

    /**
     * 下单人次
     */
    private Integer buyUserNum;

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

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public Integer getShareNum() {
		return shareNum;
	}

	public void setShareNum(Integer shareNum) {
		this.shareNum = shareNum;
	}

	public Integer getBrowseNum() {
		return browseNum;
	}

	public void setBrowseNum(Integer browseNum) {
		this.browseNum = browseNum;
	}

	public Integer getBrowseUserNum() {
		return browseUserNum;
	}

	public void setBrowseUserNum(Integer browseUserNum) {
		this.browseUserNum = browseUserNum;
	}

	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public Integer getPurchaseUserNum() {
		return purchaseUserNum;
	}

	public void setPurchaseUserNum(Integer purchaseUserNum) {
		this.purchaseUserNum = purchaseUserNum;
	}

	public Integer getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}

	public Integer getBuyUserNum() {
		return buyUserNum;
	}

	public void setBuyUserNum(Integer buyUserNum) {
		this.buyUserNum = buyUserNum;
	}

	@Override
	public String toString() {
		return "DistributionActivityShareRecord{" +
				"id=" + id +
				",activityId=" + activityId +
				",activityType=" + activityType +
				",activityName=" + activityName +
				",shareNum=" + shareNum +
				",browseNum=" + browseNum +
				",browseUserNum=" + browseUserNum +
				",purchaseNum=" + purchaseNum +
				",purchaseUserNum=" + purchaseUserNum +
				",buyNum=" + buyNum +
				",buyUserNum=" + buyUserNum +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
