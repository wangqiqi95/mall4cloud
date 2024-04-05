package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-下单记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
public class DistributionBuyRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 下单人ID
     */
    private Long buyId;

    /**
     * 下单人名称
     */
    private String buyName;

    /**
     * 类型 1 导购 2威客 3会员
     */
    private Integer buyType;

    /**
     * 触点号
     */
    private String tentacleNo;

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
     * 活动名称
     */
    private String activityName;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBuyId() {
		return buyId;
	}

	public void setBuyId(Long buyId) {
		this.buyId = buyId;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public Integer getBuyType() {
		return buyType;
	}

	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
	}

	public String getTentacleNo() {
		return tentacleNo;
	}

	public void setTentacleNo(String tentacleNo) {
		this.tentacleNo = tentacleNo;
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "DistributionBuyRecord{" +
				"id=" + id +
				",buyId=" + buyId +
				",buyName=" + buyName +
				",buyType=" + buyType +
				",tentacleNo=" + tentacleNo +
				",shareId=" + shareId +
				",shareType=" + shareType +
				",activityId=" + activityId +
				",activityType=" + activityType +
				",activityName=" + activityName +
				",productId=" + productId +
				",productName=" + productName +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
