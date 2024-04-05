package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销数据-导购分销效果VO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public class DistributionGuideShareRecordVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("导购ID")
    private Long guideId;

    @ApiModelProperty("导购名称")
    private String guideName;

    @ApiModelProperty("导购号")
    private String guideNo;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("活动类型 1海报 2专题 3朋友圈 4商品")
    private Integer activityType;

    @ApiModelProperty("数据日期")
    private Date dataTime;

    @ApiModelProperty("分享次数")
    private Integer shareNum;

    @ApiModelProperty("浏览次数")
    private Integer browseNum;

    @ApiModelProperty("浏览人次")
    private Integer browseUserNum;

    @ApiModelProperty("加购人次")
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
		return "DistributionGuideShareRecordVO{" +
				"id=" + id +
				",guideId=" + guideId +
				",guideName=" + guideName +
				",guideNo=" + guideNo +
				",storeId=" + storeId +
				",storeName=" + storeName +
				",activityType=" + activityType +
				",dataTime=" + dataTime +
				",shareNum=" + shareNum +
				",browseNum=" + browseNum +
				",browseUserNum=" + browseUserNum +
				",purchaseUserNum=" + purchaseUserNum +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
