package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-活动分享效果VO
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public class DistributionActivityShareRecordVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("活动类型 1海报 2专题 3朋友圈 4商品")
    private Integer activityType;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("分享次数")
    private Integer shareNum;

    @ApiModelProperty("浏览次数")
    private Integer browseNum;

    @ApiModelProperty("浏览人次")
    private Integer browseUserNum;

    @ApiModelProperty("加购次数")
    private Integer purchaseNum;

    @ApiModelProperty("加购人次")
    private Integer purchaseUserNum;

    @ApiModelProperty("下单次数")
    private Integer buyNum;

    @ApiModelProperty("下单人次")
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
		return "DistributionActivityShareRecordVO{" +
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
