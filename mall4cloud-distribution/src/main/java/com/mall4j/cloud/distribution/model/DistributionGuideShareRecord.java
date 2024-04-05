package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销数据-导购分销效果
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public class DistributionGuideShareRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 导购ID
     */
    private Long guideId;

    /**
     * 导购名称
     */
    private String guideName;

    /**
     * 导购号
     */
    private String guideNo;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

	/**
	 * 门店编号
	 */
	private String storeCode;

    /**
     * 活动类型 1海报 2专题 3朋友圈 4商品
     */
    private Integer activityType;

    /**
     * 数据日期
     */
    private Date dataTime;

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
     * 加购人次
     */
    private Integer purchaseUserNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}

	public String getGuideName() {
		return guideName;
	}

	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}

	public String getGuideNo() {
		return guideNo;
	}

	public void setGuideNo(String guideNo) {
		this.guideNo = guideNo;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
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

	public Integer getPurchaseUserNum() {
		return purchaseUserNum;
	}

	public void setPurchaseUserNum(Integer purchaseUserNum) {
		this.purchaseUserNum = purchaseUserNum;
	}

	@Override
	public String toString() {
		return "DistributionGuideShareRecord{" +
				"id=" + id +
				", guideId=" + guideId +
				", guideName='" + guideName + '\'' +
				", guideNo='" + guideNo + '\'' +
				", storeId=" + storeId +
				", storeName='" + storeName + '\'' +
				", storeCode='" + storeCode + '\'' +
				", activityType=" + activityType +
				", dataTime=" + dataTime +
				", shareNum=" + shareNum +
				", browseNum=" + browseNum +
				", browseUserNum=" + browseUserNum +
				", purchaseUserNum=" + purchaseUserNum +
				'}';
	}
}
